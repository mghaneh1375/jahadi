package four.group.jahadi.Service;

import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Repository.EquipmentRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EquipmentService extends AbstractService<Equipment> {

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
    public ResponseEntity<Equipment> findById(ObjectId id, Object... params) {
        return null;
    }

}
