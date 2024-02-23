package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static four.group.jahadi.Service.Area.ModuleServiceInArea.checkUsers;

@Service
public class MembersServiceInArea {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    static List<ObjectId> fetchMemberIds(TripRepository tripRepository, ObjectId userId, ObjectId areaId) {
        return tripRepository.getMembersByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new).getAreas().stream()
                .filter(area -> area.getId().equals(areaId)).findFirst().orElseThrow(RuntimeException::new)
                .getMembers();
    }

    public ResponseEntity<List<User>> members(ObjectId userId, ObjectId areaId) {
        return new ResponseEntity<>(
                userRepository.findByIdsIn(fetchMemberIds(tripRepository, userId, areaId)),
                HttpStatus.OK
        );
    }

    public void addMembers(ObjectId userId, ObjectId groupId,
                           ObjectId areaId, List<ObjectId> userIds) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        int count = userRepository.countByIdsAndGroupId(userIds, groupId);
        if (count != userIds.size())
            throw new NotAccessException();

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ObjectId> members = foundArea.getMembers();
        userIds.forEach(objectId -> {
            if (members.contains(objectId)) return;
            members.add(objectId);
        });

        foundArea.setMembers(members);
        tripRepository.save(wantedTrip);
    }

    public void removeMember(ObjectId userId, ObjectId areaId,
                             ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getMembers().remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public void addDispatchers(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> dispatchers = foundArea.getDispatchers();
        userIds.forEach(objectId -> {
            if (dispatchers.contains(objectId)) return;
            dispatchers.add(objectId);
        });

        foundArea.setDispatchers(dispatchers);
        tripRepository.save(wantedTrip);
    }

    public void removeDispatcher(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ObjectId> dispatchers = foundArea.getDispatchers();
        if(!dispatchers.contains(wantedUserId))
            throw new InvalidIdException();

        dispatchers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

}
