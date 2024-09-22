package four.group.jahadi.Service;

import four.group.jahadi.DTO.WareHouseAccessForGroupData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Models.WareHouseAccessForGroup;
import four.group.jahadi.Models.WareHouseAccessForGroupJoinWithUser;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WareHouseAccessService extends AbstractService<WareHouseAccessForGroupJoinWithUser, WareHouseAccessForGroupData>{

    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<WareHouseAccessForGroupJoinWithUser>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        return new ResponseEntity<>(
                wareHouseAccessForGroupRepository.findByGroupId(groupId),
                HttpStatus.OK
        );
    }

    @Override
    public void update(ObjectId id, WareHouseAccessForGroupData dto, Object... params) {}

    @Override
    public ResponseEntity<WareHouseAccessForGroupJoinWithUser> store(WareHouseAccessForGroupData dto, Object... params) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(InvalidIdException::new);
        ObjectId groupId = (ObjectId) params[0];
        if(!Objects.equals(groupId, user.getGroupId()))
            throw new NotAccessException();

        Optional<WareHouseAccessForGroup> access =
                wareHouseAccessForGroupRepository.findAccessByGroupIdAndUserId(groupId, user.getId());
        if(access.isPresent()) {
            access.get().setHasAccessForDrug(dto.isDrugAccess());
            access.get().setHasAccessForEquipment(dto.isEquipmentAccess());
            wareHouseAccessForGroupRepository.save(access.get());
        }
        else {
            wareHouseAccessForGroupRepository.insert(
                    WareHouseAccessForGroup
                            .builder()
                            .userId(user.getId())
                            .groupId(groupId)
                            .hasAccessForDrug(dto.isDrugAccess())
                            .hasAccessForEquipment(dto.isEquipmentAccess())
                            .build()
            );
        }

        return new ResponseEntity<>(
                wareHouseAccessForGroupRepository.findAggregateByGroupIdAndUserId(groupId, user.getId()).get(0),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<WareHouseAccessForGroupJoinWithUser> findById(ObjectId id, Object... params) {
        return null;
    }

    public void removeFromWareHouseAccesses(ObjectId userId, ObjectId groupId) {
        wareHouseAccessForGroupRepository.removeAccessByGroupIdAndUserId(groupId, userId);
    }
}
