package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Enums.Drug.*;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Repository.DrugLogRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Utility.Utility;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static four.group.jahadi.Utility.Utility.datePattern;


@Service
public class DrugService extends AbstractService<Drug, DrugData> {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DrugLogRepository drugLogRepository;

    @Override
    public ResponseEntity<List<Drug>> list(Object... filters) {
        ObjectId userId = (ObjectId) filters[0];
        try {
            String name = filters.length > 1 ? (String) filters[1] : null;
            Integer minAvailableCount = filters.length > 2 ? (Integer) filters[2] : null;
            Integer maxAvailableCount = filters.length > 3 ? (Integer) filters[3] : null;
            DrugLocation drugLocation = filters.length > 4 && filters[4] != null ? DrugLocation.valueOf(filters[4].toString().toUpperCase()) : null;
            DrugType drugType = filters.length > 5 && filters[5] != null ? DrugType.valueOf(filters[5].toString().toUpperCase()) : null;
            Date fromExpireAt = filters.length > 6 ? (Date) filters[6] : null;
            Date toExpireAt = filters.length > 7 ? (Date) filters[7] : null;
            String boxNo = filters.length > 8 ? (String) filters[8] : null;
            String shelfNo = filters.length > 9 ? (String) filters[9] : null;

            return new ResponseEntity<>(
                    drugRepository.findByFilters(
                            userId, name, minAvailableCount, maxAvailableCount,
                            drugLocation, drugType, fromExpireAt, toExpireAt,
                            boxNo, shelfNo
                    ),
                    HttpStatus.OK
            );
        }
        catch (Exception x) {
            throw new InvalidFieldsException(x.getMessage());
        }
    }

    @Override
    public ResponseEntity<Drug> findById(ObjectId id, Object... params) {
        return new ResponseEntity<>(
                drugRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public void setReplacements(ObjectId id, List<ObjectId> replacements) {

        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (drugRepository.countByIds(replacements) != replacements.size())
            throw new InvalidIdException();

        drug.setReplacements(replacements);
        drugRepository.save(drug);
    }

    public ResponseEntity<List<Drug>> findReplacements(ObjectId id) {

        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);
        List<Drug> replacements = drugRepository.findByIds(drug.getReplacements());

        return new ResponseEntity<>(
                replacements,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Drug> store(DrugData data, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        ObjectId groupId = (ObjectId) params[1];
        Drug drug = populateEntity(null, data);
        drug.setUserId(userId);
        drug.setGroupId(groupId);
        drug = drugRepository.insert(drug);
        drugLogRepository.insert(
                DrugLog
                        .builder()
                        .userId(userId)
                        .drugId(drug.getId())
                        .amount(drug.getAvailable())
                        .desc("ایجاد دارو توسط ادمین")
                        .build()
        );
        return new ResponseEntity<>(drug, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, DrugData drugData, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        Drug drug = drugRepository.findByIdAndUserId(id, userId)
                .orElseThrow(InvalidIdException::new);
        int oldAvailable = drug.getAvailable();
        drug = populateEntity(drug, drugData);
        if (drug.getAvailable() != oldAvailable) {
            DrugLog
                    .builder()
                    .drugId(drug.getId())
                    .userId(userId)
                    .amount(drug.getAvailable() - oldAvailable)
                    .desc("ویرایش موجودی دارو توسط مسئول گروه")
                    .build();
        }
        drugRepository.save(drug);
    }

    @Override
    Drug populateEntity(Drug drug, DrugData drugData) {

        if (drug == null)
            drug = new Drug();

        drug.setDrugType(drugData.getDrugType());
        drug.setName(drugData.getName());
        drug.setDose(drugData.getDose());
        drug.setExpireAt(drugData.getExpireAt());
        drug.setProducer(drugData.getProducer());
        drug.setAvailable(drugData.getAvailable());
        drug.setAvailablePack(drugData.getAvailablePack());
        drug.setPrice(drugData.getPrice());
        drug.setLocation(drugData.getLocation());
        drug.setShelfNo(drugData.getShelfNo());
        drug.setBoxNo(drugData.getBoxNo());
//        drug.setHowToUses(drugData.getHowToUses());
//        drug.setAmountOfUses(drugData.getAmountOfUses());
//        drug.setUseTimes(drugData.getUseTimes());
//        drug.setDescription(drugData.getDescription());
//        drug.setVisibility(drugData.getVisibility());
//        drug.setPriority(drugData.getPriority());

        return drug;
    }

    public void remove(ObjectId id) {
        //todo: remove logs and check for usage in active trip
        drugRepository.deleteById(id);
    }

    // EXCEL FORMAT
    // A: index, B: drugType, C: name, D: dose, E: expireAt,
    // F: producer, G: available, H: availablePack, I: price,
    // J: location, K: boxNo, L: shelfNo
    private Drug isRowValid(Row row) {
        Drug drug = new Drug();

        for (int i = 1; i <= row.getLastCellNum(); i++) {
            Object value;
            if (row.getCell(i) == null || row.getCell(i).getCellType() == CellType.BLANK)
                continue;

            try {
                value = row.getCell(i).getStringCellValue();
            } catch (Exception x) {
                value = row.getCell(i).getNumericCellValue();
                if (i == 10 || i == 11)
                    value = value.toString().replace(".0", "");
            }

            switch (i) {
                case 1:
                    drug.setDrugType(
                            DrugType.valueOf(value.toString().toUpperCase())
                    );
                    break;
                case 2:
                    validateString(value.toString(), "نام", 2, 100);
                    drug.setName(value.toString());
                    break;
                case 3:
                    validateString(value.toString(), "دوز", 2, 100);
                    drug.setDose(value.toString());
                    break;
                case 4:
                    if (!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ انقضا نامعتبر است.");
                    drug.setExpireAt(Utility.convertJalaliToGregorianDate(value.toString()));
                    break;
                case 5:
                    validateString(value.toString(), "شرکت سازنده", 2, 100);
                    drug.setProducer(value.toString());
                    break;
                case 6:
                    if (
                            ((Number) value).intValue() < 0 ||
                                    ((Number) value).intValue() > 100000
                    )
                        throw new InvalidFieldsException("مقدار موجودی باید حداقل 0 و حداکثر 100000 باشد");
                    drug.setAvailable(((Number) value).intValue());
                    break;
                case 7:
                    if (
                            ((Number) value).intValue() < 0 ||
                                    ((Number) value).intValue() > 100000
                    )
                        throw new InvalidFieldsException("مقدار موجودی پک باید حداقل 0 و حداکثر 100000 باشد");
                    drug.setAvailablePack(((Number) value).intValue());
                    break;
                case 8:
                    if (
                            ((Number) value).intValue() < 0 ||
                                    ((Number) value).intValue() > 1000000000
                    )
                        throw new InvalidFieldsException("قیمت هر دانه باید حداقل 0 و حداکثر 1000000000 باشد");
                    drug.setPrice(((Number) value).intValue());
                    break;
                case 9:
                    drug.setLocation(
                            DrugLocation.valueOf(value.toString().toUpperCase())
                    );
                    break;
                case 10:
                    validateString(value.toString(), "شماره جعبه", 1, 100);
                    drug.setBoxNo(value.toString());
                    break;
                case 11:
                    validateString(value.toString(), "شماره قفسه", 1, 100);
                    drug.setShelfNo(value.toString());
                    break;
            }
        }

        if (drug.getDrugType() == null ||
                drug.getName() == null ||
                drug.getProducer() == null ||
                drug.getAvailable() == null ||
                drug.getAvailablePack() == null ||
                drug.getExpireAt() == null ||
                drug.getDose() == null ||
                drug.getPrice() == null ||
                drug.getShelfNo() == null ||
                drug.getBoxNo() == null ||
                drug.getLocation() == null
        )
            throw new InvalidFieldsException("لطفا تمام موارد را وارد نمایید");

        return drug;
    }

    public ResponseEntity<List<ErrorRow>> batchStore(MultipartFile file, ObjectId userId, ObjectId groupId) {
        if (file == null)
            throw new InvalidFieldsException("لطفا فایل را بارگذاری نمایید");
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Drug> drugs = new ArrayList<>();
            List<ErrorRow> errorRows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                try {
                    Drug drug = isRowValid(sheet.getRow(i));
                    drug.setUserId(userId);
                    drug.setGroupId(groupId);
                    drugs.add(drug);
                } catch (Exception e) {
                    errorRows.add(
                            ErrorRow
                                    .builder()
                                    .errorMsg(e.getMessage())
                                    .rowIndex(i + 1)
                                    .build()
                    );
                }
            }

            if (drugs.size() > 0)
                drugRepository.saveAll(drugs);

            return new ResponseEntity<>(errorRows, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<DrugType[]> getDrugTypes() {
        return new ResponseEntity<>(DrugType.values(), HttpStatus.OK);
    }

    public ResponseEntity<AmountOfUse[]> getDrugAmountOfUseOptions() {
        return new ResponseEntity<>(AmountOfUse.values(), HttpStatus.OK);
    }

    public ResponseEntity<HowToUse[]> getDrugHowToUseOptions() {
        return new ResponseEntity<>(HowToUse.values(), HttpStatus.OK);
    }

    public ResponseEntity<UseTime[]> getDrugUseTimeOptions() {
        return new ResponseEntity<>(UseTime.values(), HttpStatus.OK);
    }
}
