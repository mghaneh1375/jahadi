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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static four.group.jahadi.Utility.Utility.datePattern;

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
            Date fromBuyAt = filters.length > 10 ? (Date) filters[10] : null;
            Date toBuyAt = filters.length > 11 ? (Date) filters[11] : null;
            Date fromGuaranteeExpireAt = filters.length > 12 ? (Date) filters[12] : null;
            Date toGuaranteeExpireAt = filters.length > 13 ? (Date) filters[13] : null;
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

    }

    @Override
    public ResponseEntity<Equipment> store(EquipmentData dto, Object... params) {
        return null;
    }

    @Override
    public ResponseEntity<Equipment> findById(ObjectId id, Object... params) {
        return null;
    }

}
