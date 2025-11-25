package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Enums.Drug.*;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.PatientsDrugRepository;
import four.group.jahadi.Service.Area.ReportUtil;
import four.group.jahadi.Utility.PairValue;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.getExcelDate;
import static four.group.jahadi.Utility.Utility.isCellDateFormatted;


@Service
public class DrugService extends AbstractService<Drug, DrugData> {

    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugLogRepository drugLogRepository;
    @Autowired
    private DrugBookmarkRepository drugBookmarkRepository;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DrugsInAreaRepository drugsInAreaRepository;
    @Autowired
    private PatientsDrugRepository patientsDrugRepository;

    @Override
    public ResponseEntity<List<Drug>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        try {
            String name = filters.length > 1 ? (String) filters[1] : null;
            Integer minAvailableCount = filters.length > 2 ? (Integer) filters[2] : null;
            Integer maxAvailableCount = filters.length > 3 ? (Integer) filters[3] : null;
            DrugLocation drugLocation = filters.length > 4 && filters[4] != null ? DrugLocation.valueOf(filters[4].toString().toUpperCase()) : null;
            DrugType drugType = filters.length > 5 && filters[5] != null ? DrugType.valueOf(filters[5].toString().toUpperCase()) : null;
            LocalDateTime fromExpireAt = filters.length > 6 ? (LocalDateTime) filters[6] : null;
            LocalDateTime toExpireAt = filters.length > 7 ? (LocalDateTime) filters[7] : null;
            String boxNo = filters.length > 8 ? (String) filters[8] : null;
            String shelfNo = filters.length > 9 ? (String) filters[9] : null;

            return new ResponseEntity<>(
                    drugRepository.findByFilters(
                            groupId, name, minAvailableCount, maxAvailableCount,
                            drugLocation, drugType, fromExpireAt, toExpireAt,
                            boxNo, shelfNo
                    ),
                    HttpStatus.OK
            );
        } catch (Exception x) {
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
        boolean hasGroupAccess = (boolean) params[2];
        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

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
                        .groupId(groupId)
                        .desc("ایجاد دارو توسط مسئول گروه")
                        .build()
        );
        return new ResponseEntity<>(drug, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, DrugData drugData, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        ObjectId groupId = (ObjectId) params[1];
        boolean hasGroupAccess = (boolean) params[2];
        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        Drug drug = drugRepository.findByIdAndGroupId(id, groupId)
                .orElseThrow(InvalidIdException::new);
        int oldAvailable = drug.getAvailable();
        drug = populateEntity(drug, drugData);
        if (drug.getAvailable() != oldAvailable) {
            DrugLog
                    .builder()
                    .drugId(drug.getId())
                    .userId(userId)
                    .groupId(groupId)
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
        drug.setCode(drugData.getCode());
//        drug.setHowToUses(drugData.getHowToUses());
//        drug.setAmountOfUses(drugData.getAmountOfUses());
//        drug.setUseTimes(drugData.getUseTimes());
//        drug.setDescription(drugData.getDescription());
//        drug.setVisibility(drugData.getVisibility());
//        drug.setPriority(drugData.getPriority());

        return drug;
    }

    public void remove(ObjectId id, ObjectId userId, ObjectId groupId, boolean hasGroupAccess) {
        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();
        //todo: check usage in trips
        drugRepository.delete(
                drugRepository.findByIdAndGroupId(id, groupId)
                        .orElseThrow(InvalidIdException::new)
        );
    }

    // EXCEL FORMAT
    // A: index, B: drugType, C: name, D: dose, E: expireAt,
    // F: producer, G: available, H: availablePack, I: price,
    // J: location, K: boxNo, L: shelfNo, M: code
    private Drug isRowValid(Row row) {
        Drug drug = new Drug();

        for (int i = 1; i <= row.getLastCellNum(); i++) {
            if (row.getCell(i) == null || row.getCell(i).getCellType() == CellType.BLANK)
                continue;

            Object value;
            switch (row.getCell(i).getCellType()) {
                case NUMERIC:
                    if (isCellDateFormatted(row.getCell(i))) {
                        value = row.getCell(i).getDateCellValue();
                    } else {
                        value = row.getCell(i).getNumericCellValue();
                        if (i == 10 || i == 11)
                            value = value.toString().replace(".0", "");
                    }
                    break;
                case STRING:
                default:
                    value = row.getCell(i).getStringCellValue();
                    break;
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
                    drug.setExpireAt(getExcelDate(value, "فرمت تاریخ انقضا نامعتبر است."));
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
                case 12:
                    validateString(value.toString(), "کد دارو", 3, 50);
                    drug.setCode(value.toString());
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
                drug.getLocation() == null ||
                drug.getCode() == null
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

    public ResponseEntity<List<PairValue>> getDrugAmountOfUseOptions() {
        return new ResponseEntity<>(
                Arrays.stream(AmountOfUse.values())
                        .map(amountOfUse -> new PairValue(amountOfUse.name(), amountOfUse.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PairValue>> getDrugHowToUseOptions() {
        return new ResponseEntity<>(
                Arrays.stream(HowToUse.values())
                        .map(howToUse -> new PairValue(howToUse.name(), howToUse.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PairValue>> getDrugUseTimeOptions() {
        return new ResponseEntity<>(
                Arrays.stream(UseTime.values())
                        .map(useTime -> new PairValue(useTime.name(), useTime.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    private final static List<String> drugLogReportHeaders = new ArrayList<>() {
        {
            add("نام دارو");
            add("دُز دارو");
            add("نوع دارو");
            add("تولید کننده دارو");
            add("مکان دارو");
            add("شماره قفسه دارو");
            add("شماره جعبه دارو");
            add("ورودی/خروجی");
            add("تعداد");
            add("گروه");
            add("کاربر انجام دهنده");
            add("توضیحات");
            add("تاریخ");
        }
    };

    private final static List<String> drugReportHeaders = new ArrayList<>() {
        {
            add("نام گروه");
            add("نام دارو");
            add("کد دارو");
            add("دُز دارو");
            add("نوع دارو");
            add("تولید کننده دارو");
            add("مکان دارو");
            add("قیمت دارو");
            add("شماره قفسه دارو");
            add("شماره جعبه دارو");
            add("تاریخ ایجاد");
            add("تعداد موجود");
            add("تعداد پک موجود");
            add("قیمت");
            add("توضیحات");
        }
    };

    public void fillGroupIdInLogDoc() {
        List<DrugLog> logs = drugLogRepository.findByGroupIdIsNull();
        ObjectId groupId = groupRepository.findAll().get(0).getId();
        logs.forEach(drugLog -> {
            Optional<Drug> drugOptional = drugRepository.findById(drugLog.getDrugId());
            if (drugOptional.isPresent()) {
                drugLog.setGroupId(
                        drugOptional.get().getGroupId() == null
                                ? groupId
                                : drugOptional.get().getGroupId()
                );
            } else
                drugLog.setGroupId(groupId);
        });
        drugLogRepository.saveAll(logs);
    }

    public void logReport(
            ObjectId drugId, ObjectId groupId, ObjectId userId, ObjectId areaId,
            LocalDateTime from, LocalDateTime to,
            HttpServletResponse response
    ) {
        List<DrugLogJoinModel> logs = drugLogRepository.findWithJoin(
                drugId, userId, areaId, from, to, groupId
        );
        Workbook workbook = ReportUtil.createWorkbook(drugLogReportHeaders);
        Sheet sheet = workbook.getSheetAt(0);
        AtomicInteger counter = new AtomicInteger(1);

        logs.forEach(log -> log.fillExcelRow(sheet.createRow(counter.getAndIncrement())));
        ReportUtil.prepareHttpServletResponse(response, workbook, "drugLog");
    }

    public void report(ObjectId groupId, HttpServletResponse response) {
        List<DrugJoinModel> drugs = drugRepository.findAllByGroupId(groupId);
        Workbook workbook = ReportUtil.createWorkbook(drugReportHeaders);
        Sheet sheet = workbook.getSheetAt(0);
        AtomicInteger counter = new AtomicInteger(1);

        drugs.forEach(drug -> drug.fillExcelRow(sheet.createRow(counter.getAndIncrement())));
        ReportUtil.prepareHttpServletResponse(response, workbook, "drugReport");
    }

    public void removeRedundants() {
        List<ObjectId> ids =
                drugRepository.findAll()
                        .stream()
                        .map(Drug::getId)
                        .collect(Collectors.toList());

        System.out.println(ids.size());
        Set<ObjectId> used = new HashSet<>();
        patientsDrugRepository.findAllByDrugIdIsInOrGivenDrugIdIn(
                ids
        ).forEach(patientDrug -> {
            if(patientDrug.getDrugId() != null)
                used.add(patientDrug.getDrugId());
            if(patientDrug.getGivenDrugId() != null)
                used.add(patientDrug.getGivenDrugId());
        });
        System.out.println(used.size());
        List<ObjectId> notUsed = ids
                .stream()
                .filter(objectId -> !used.contains(objectId))
                .collect(Collectors.toList());

        drugRepository.deleteAllById(notUsed);
        drugLogRepository.deleteAllByDrugIdIn(notUsed);
        drugBookmarkRepository.deleteAllByDrugIdIn(notUsed);
        drugsInAreaRepository.deleteAllByDrugIdIn(notUsed);
    }
}
