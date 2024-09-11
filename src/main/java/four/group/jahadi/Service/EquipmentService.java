package four.group.jahadi.Service;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Repository.EquipmentRepository;
import four.group.jahadi.Utility.Utility;
import lombok.Builder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static four.group.jahadi.Utility.Utility.datePattern;

@Service
public class EquipmentService extends AbstractService<Equipment, EquipmentData> {

    @Autowired
    private EquipmentRepository equipmentRepository;


    @Override
    public ResponseEntity<List<Equipment>> list(Object... filters) {
        ObjectId userId = (ObjectId) filters[0];
        String name = (String) filters[1];
        Integer minAvailable = (Integer) filters[2];
        Integer maxAvailable = (Integer) filters[3];
        EquipmentHealthStatus healthyStatus =
                filters[4] == null
                        ? null
                        : EquipmentHealthStatus.valueOf(filters[4].toString().toLowerCase());
        return new ResponseEntity<>(
                equipmentRepository.findByFilters(userId, name, minAvailable, maxAvailable, healthyStatus),
                HttpStatus.OK
        );
    }

    @Override
    public void update(ObjectId id, EquipmentData dto, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        Equipment equipment = equipmentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(InvalidIdException::new);
        equipment.setEquipmentType(dto.getEquipmentType());
        equipment.setName(dto.getName());
        equipment.setProducer(dto.getProducer());
        equipment.setAvailable(dto.getAvailable());
        equipment.setBuyAt(dto.getBuyAt());
        equipment.setHealthStatus(dto.getHealthStatus());
        equipment.setRowNo(dto.getRowNo());
        equipment.setShelfNo(dto.getShelfNo());
        equipment.setLocation(dto.getLocation());
        equipment.setDescription(dto.getDescription());

        if (equipment.getEquipmentType().equals(EquipmentType.CONSUMPTION)) {
            equipment.setUsedAt(dto.getUsedAt());
            equipment.setPropertyId(null);
            equipment.setGuaranteeExpireAt(null);
        } else {
            equipment.setUsedAt(null);
            equipment.setPropertyId(dto.getPropertyId());
            equipment.setGuaranteeExpireAt(dto.getGuaranteeExpireAt());
        }
        equipmentRepository.save(equipment);
    }

    @Override
    public ResponseEntity<Equipment> store(EquipmentData dto, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        ObjectId groupId = (ObjectId) params[1];
        Equipment equipment = Equipment
                .builder()
                .equipmentType(dto.getEquipmentType())
                .name(dto.getName())
                .producer(dto.getProducer())
                .available(dto.getAvailable())
                .buyAt(dto.getBuyAt())
                .healthStatus(dto.getHealthStatus())
                .rowNo(dto.getRowNo())
                .shelfNo(dto.getShelfNo())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .userId(userId)
                .groupId(groupId)
                .build();
        if (equipment.getEquipmentType().equals(EquipmentType.CONSUMPTION))
            equipment.setUsedAt(dto.getUsedAt());
        else {
            equipment.setPropertyId(dto.getPropertyId());
            equipment.setGuaranteeExpireAt(dto.getGuaranteeExpireAt());
        }
        equipmentRepository.insert(equipment);
        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    private void validateString(String val, String key, int min, int max) {
        if (val == null || val.length() < min || val.length() > max)
            throw new InvalidFieldsException(key + " باید حداقل 2 کاراکتر و حداکثر 100 کاراکتر باشد");
    }


    // EXCEL FORMAT
    // A: equipmentType, B: name, C: producer, D: available,
    // E: buyAt, F: healthStatus, G: rowNo, H: shelfNo, I: location,
    // J: description, K: propertyId, L: guaranteeExpireAt, M: usedAt
    private Equipment isRowValid(Row row) {
        Equipment equipment = new Equipment();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Object value;
            try {
                value = row.getCell(i).getStringCellValue();
            } catch (Exception x) {
                value = row.getCell(i).getNumericCellValue();
            }
            if(value == null) continue;

            switch (i) {
                case 0:
                    equipment.setEquipmentType(
                            EquipmentType.valueOf(value.toString().toLowerCase())
                    );
                    break;
                case 1:
                    validateString(value.toString(), "نام", 2, 100);
                    equipment.setName(value.toString());
                    break;
                case 2:
                    validateString(value.toString(), "شرکت سازنده", 2, 100);
                    equipment.setProducer(value.toString());
                    break;
                case 3:
                    if (
                            ((Number) value).intValue() < 0 ||
                                    ((Number) value).intValue() > 100000
                    )
                        throw new InvalidFieldsException("مقدار موجودی باید حداقل 0 و حداکثر 100000 باشد");
                    equipment.setAvailable(((Number) value).intValue());
                    break;
                case 4:
                    if(!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ خرید نامعتبر است.");
                    equipment.setBuyAt(Utility.convertJalaliToGregorianDate(value.toString()));
                    break;
                case 5:
                    equipment.setHealthStatus(
                            EquipmentHealthStatus.valueOf(value.toString().toLowerCase())
                    );
                    break;
                case 6:
                    validateString(value.toString(), "شماره ردیف", 2, 100);
                    equipment.setRowNo(value.toString());
                    break;
                case 7:
                    validateString(value.toString(), "شماره قفسه", 2, 100);
                    equipment.setShelfNo(value.toString());
                    break;
                case 8:
                    validateString(value.toString(), "محل انبار", 2, 100);
                    equipment.setLocation(value.toString());
                    break;
                case 9:
                    if (value.toString().length() > 1000)
                        throw new InvalidFieldsException("توضیحات باید حداکثر 1000 کاراکتر باشد");
                    equipment.setLocation(value.toString());
                    break;
                case 10:
                    if(equipment.getEquipmentType() != null &&
                            equipment.getEquipmentType().equals(EquipmentType.INFRASTRUCTURE)) {
                        validateString(value.toString(), "شماره اموال", 2, 100);
                        equipment.setPropertyId(value.toString());
                    }
                    break;
                case 11:
                    if(!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ انقضای گارانتی نامعتبر است.");
                    equipment.setGuaranteeExpireAt(Utility.convertJalaliToGregorianDate(value.toString()));
                    break;
                case 12:
                    if(!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ استفاده نامعتبر است.");
                    equipment.setUsedAt(Utility.convertJalaliToGregorianDate(value.toString()));
                    break;
            }
        }

        if (equipment.getEquipmentType() == null ||
                equipment.getName() == null ||
                equipment.getProducer() == null ||
                equipment.getAvailable() == null ||
                equipment.getBuyAt() == null ||
                equipment.getHealthStatus() == null ||
                equipment.getRowNo() == null ||
                equipment.getShelfNo() == null ||
                equipment.getLocation() == null
        )
            throw new InvalidFieldsException("لطفا تمام موارد را وارد نمایید");

        if (equipment.getEquipmentType().equals(EquipmentType.INFRASTRUCTURE) &&
                (equipment.getPropertyId() == null ||
                        equipment.getGuaranteeExpireAt() == null)
        )
            throw new InvalidFieldsException("لطفا شماره اموال و تاریخ انقضای گارانتی را وارد نمایید");

        return equipment;
    }

    @Builder
    public class ErrorRow {
        private Integer rowIndex;
        private String errorMsg;
    }

    public ResponseEntity<List<ErrorRow>> batchStore(MultipartFile file, ObjectId userId, ObjectId groupId) {
        if(file == null)
            throw new InvalidFieldsException("لطفا فایل را بارگذاری نمایید");
        try {
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Equipment> equipments = new ArrayList<>();
            List<ErrorRow> errorRows = new ArrayList<>();
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                try {
                    Equipment equipment = isRowValid(sheet.getRow(i));
                    equipment.setUserId(userId);
                    equipment.setGroupId(groupId);
                    equipments.add(equipment);
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

            if(equipments.size() > 0)
                equipmentRepository.saveAll(equipments);

            return new ResponseEntity<>(errorRows, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Equipment> findById(ObjectId id, Object... params) {
        return null;
    }

    public void remove(ObjectId equipmentId, ObjectId userId) {
        equipmentRepository.delete(
                equipmentRepository.findByIdAndUserId(equipmentId, userId)
                        .orElseThrow(InvalidIdException::new)
        );
    }
}
