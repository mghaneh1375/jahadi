package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.ReportAccessForGroupRepository;
import four.group.jahadi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupReportService {

    private final ReportAccessForGroupRepository repository;
    private final UserRepository userRepository;

    @Cacheable(cacheNames = "groupReporters", key = "#groupId")
    public List<ObjectId> getGroupReporterUsers(ObjectId groupId) {
        return repository.findAccessesByGroupId(groupId)
                .stream()
                .map(ReportAccessForGroup::getUserId)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "groupReporters", allEntries = true)
    public ResponseEntity<ReportAccessJoinWithUser> store(ObjectId userId, Object... params) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidIdException::new);

        ObjectId groupId = (ObjectId) params[0];
        if(!Objects.equals(groupId, user.getGroupId()))
            throw new NotAccessException();

        if(!repository.existsAccessByGroupIdAndUserId(groupId, user.getId())) {
            repository.insert(
                    ReportAccessForGroup
                            .builder()
                            .userId(user.getId())
                            .groupId(groupId)
                            .build()
            );
        }

        return new ResponseEntity<>(
                repository.findAggregateByGroupIdAndUserId(groupId, user.getId()).get(0),
                HttpStatus.OK
        );
    }

    @CacheEvict(value = "groupReporters", allEntries = true)
    public void revokeAccess(ObjectId userId, ObjectId groupId) {
        repository.revokeAccessByGroupIdAndUserId(groupId, userId);
    }

}
