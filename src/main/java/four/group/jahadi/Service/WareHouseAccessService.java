package four.group.jahadi.Service;

import four.group.jahadi.Models.WareHouseAccessForGroupJoinWithUser;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WareHouseAccessService {

    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<WareHouseAccessForGroupJoinWithUser>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        return new ResponseEntity<>(
                wareHouseAccessForGroupRepository.findByGroupId(groupId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<WareHouseAccessForGroupJoinWithUser> findById(ObjectId id, Object... params) {
        return null;
    }

    public void removeFromWareHouseAccesses(ObjectId userId, ObjectId groupId) {
        wareHouseAccessForGroupRepository.removeAccessByGroupIdAndUserId(groupId, userId);
    }
}
