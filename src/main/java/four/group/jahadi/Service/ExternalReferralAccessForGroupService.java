package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.ExternalReferralAccessForGroup;
import four.group.jahadi.Models.ExternalReferralAccessJoinWithUser;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ExternalReferralAccessForGroupRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ExternalReferralAccessForGroupService {

    @Autowired
    private ExternalReferralAccessForGroupRepository repository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<ExternalReferralAccessJoinWithUser>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        return new ResponseEntity<>(
                repository.findByGroupId(groupId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ExternalReferralAccessJoinWithUser> store(ObjectId userId, Object... params) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidIdException::new);
        ObjectId groupId = (ObjectId) params[0];
        if(!Objects.equals(groupId, user.getGroupId()))
            throw new NotAccessException();

        if(!repository.existsAccessByGroupIdAndUserId(groupId, user.getId()))
            repository.insert(
                    ExternalReferralAccessForGroup
                            .builder()
                            .userId(user.getId())
                            .groupId(groupId)
                            .build()
            );

        return new ResponseEntity<>(
                repository.findAggregateByGroupIdAndUserId(groupId, user.getId()).get(0),
                HttpStatus.OK
        );
    }

    public void revokeAccess(ObjectId userId, ObjectId groupId) {
        repository.revokeAccessByGroupIdAndUserId(groupId, userId);
    }
}
