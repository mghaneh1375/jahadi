package four.group.jahadi.Service;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Repository.EquipmentRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Utility.Utility.datePattern;
import static four.group.jahadi.Utility.Utility.getLocalDateTime;

@Service
public class EquipmentService extends AbstractService<Equipment, EquipmentData> {

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;

    @Override
    public ResponseEntity<List<Equipment>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        try {
            String name = filters.length > 1 ? (String) filters[1] : null;
            Integer minAvailable = filters.length > 2 ? (Integer) filters[2] : null;
            Integer maxAvailable = filters.length > 3 ? (Integer) filters[3] : null;
            EquipmentHealthStatus healthyStatus = filters.length > 4 && filters[4] != null
                    ? EquipmentHealthStatus.valueOf(filters[4].toString().toUpperCase())
                    : null;
            String propertyId = filters.length > 5 ? (String) filters[5] : null;
            String location = filters.length > 6 ? (String) filters[6] : null;
            EquipmentType equipmentType = filters.length > 7 && filters[7] != null ? EquipmentType.valueOf(filters[7].toString().toUpperCase()) : null;
            String rowNo = filters.length > 8 ? (String) filters[8] : null;
            String shelfNo = filters.length > 9 ? (String) filters[9] : null;
            LocalDateTime fromBuyAt = filters.length > 10 ? (LocalDateTime) filters[10] : null;
            LocalDateTime toBuyAt = filters.length > 11 ? (LocalDateTime) filters[11] : null;
            LocalDateTime fromGuaranteeExpireAt = filters.length > 12 ? (LocalDateTime) filters[12] : null;
            LocalDateTime toGuaranteeExpireAt = filters.length > 13 ? (LocalDateTime) filters[13] : null;
            return new ResponseEntity<>(
                    equipmentRepository.findByFilters(
                            groupId, name, minAvailable, maxAvailable, healthyStatus,
                            propertyId, location, equipmentType, rowNo, shelfNo,
                            fromBuyAt, toBuyAt, fromGuaranteeExpireAt, toGuaranteeExpireAt
                    ),
                    HttpStatus.OK
            );
        } catch (Exception x) {
            throw new InvalidFieldsException(x.getMessage());
        }
    }

    @Override
    public void update(ObjectId id, EquipmentData dto, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        ObjectId groupId = (ObjectId) params[1];
        boolean hasGroupAccess = (boolean) params[2];

        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        Equipment equipment = equipmentRepository.findByIdAndGroupId(id, groupId)
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
        boolean hasGroupAccess = (boolean) params[2];

        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

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

    // EXCEL FORMAT
    // A: index, B: equipmentType, C: name, D: producer, E: available,
    // F: buyAt, G: healthStatus, H: rowNo, I: shelfNo, J: location,
    // K: description, L: propertyId, M: guaranteeExpireAt, N: usedAt
    private Equipment isRowValid(Row row) {
        Equipment equipment = new Equipment();

        for (int i = 1; i <= row.getLastCellNum(); i++) {
            Object value;
            if (row.getCell(i) == null || row.getCell(i).getCellType() == CellType.BLANK)
                continue;

            try {
                value = row.getCell(i).getStringCellValue();
            } catch (Exception x) {
                value = row.getCell(i).getNumericCellValue();
                if (i == 7 || i == 8)
                    value = value.toString().replace(".0", "");
            }

            switch (i) {
                case 1:
                    equipment.setEquipmentType(
                            EquipmentType.valueOf(value.toString().toUpperCase())
                    );
                    break;
                case 2:
                    validateString(value.toString(), "نام", 2, 100);
                    equipment.setName(value.toString());
                    break;
                case 3:
                    validateString(value.toString(), "شرکت سازنده", 2, 100);
                    equipment.setProducer(value.toString());
                    break;
                case 4:
                    if (
                            ((Number) value).intValue() < 0 ||
                                    ((Number) value).intValue() > 100000
                    )
                        throw new InvalidFieldsException("مقدار موجودی باید حداقل 0 و حداکثر 100000 باشد");
                    equipment.setAvailable(((Number) value).intValue());
                    break;
                case 5:
                    if (!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ خرید نامعتبر است.");
                    equipment.setBuyAt(getLocalDateTime(Utility.convertJalaliToGregorianDate(value.toString())));
                    break;
                case 6:
                    equipment.setHealthStatus(
                            EquipmentHealthStatus.valueOf(value.toString().toUpperCase())
                    );
                    break;
                case 7:
                    validateString(value.toString(), "شماره ردیف", 1, 100);
                    equipment.setRowNo(value.toString());
                    break;
                case 8:
                    validateString(value.toString(), "شماره قفسه", 1, 100);
                    equipment.setShelfNo(value.toString());
                    break;
                case 9:
                    validateString(value.toString(), "محل انبار", 2, 100);
                    equipment.setLocation(value.toString());
                    break;
                case 10:
                    if (value.toString().length() > 1000)
                        throw new InvalidFieldsException("توضیحات باید حداکثر 1000 کاراکتر باشد");
                    equipment.setDescription(value.toString());
                    break;
                case 11:
                    if (equipment.getEquipmentType() != null &&
                            equipment.getEquipmentType().equals(EquipmentType.INFRASTRUCTURE)) {
                        validateString(value.toString(), "شماره اموال", 2, 100);
                        equipment.setPropertyId(value.toString());
                    }
                    break;
                case 12:
                    if (!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ انقضای گارانتی نامعتبر است.");
                    equipment.setGuaranteeExpireAt(getLocalDateTime(Utility.convertJalaliToGregorianDate(value.toString())));
                    break;
                case 13:
                    if (!datePattern.matcher(value.toString()).matches())
                        throw new InvalidFieldsException("فرمت تاریخ استفاده نامعتبر است.");
                    equipment.setUsedAt(getLocalDateTime(Utility.convertJalaliToGregorianDate(value.toString())));
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

    public ResponseEntity<List<ErrorRow>> batchStore(
            MultipartFile file, ObjectId userId,
            ObjectId groupId, boolean hasGroupAccess
    ) {
        if (file == null)
            throw new InvalidFieldsException("لطفا فایل را بارگذاری نمایید");
        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Equipment> equipments = new ArrayList<>();
            List<ErrorRow> errorRows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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

            if (equipments.size() > 0)
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

    public void remove(
            ObjectId equipmentId, ObjectId userId,
            ObjectId groupId, boolean hasGroupAccess
    ) {
        if (!hasGroupAccess &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        //todo: check usage in trips
        equipmentRepository.delete(
                equipmentRepository.findByIdAndGroupId(equipmentId, groupId)
                        .orElseThrow(InvalidIdException::new)
        );
    }
}
