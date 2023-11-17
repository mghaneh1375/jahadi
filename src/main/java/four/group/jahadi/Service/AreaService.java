package four.group.jahadi.Service;

import four.group.jahadi.DTO.AreaData;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area;
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
import java.util.Objects;

@Service
public class AreaService extends AbstractService<Area, AreaData> {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<Area>> list(Object... filters) {
        return null;
    }

    @Override
    public void update(ObjectId id, AreaData dto, Object... params) {

    }

    @Override
    public ResponseEntity<Area> store(AreaData dto, Object... params) {

        ObjectId tripId = (ObjectId) params[1];

        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);

        boolean hasAdminAccess = (boolean) params[0];

        if(!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(params[2]))
        )
            throw new NotAccessException();

        Area area = dto.convertToArea();

        User owner = userRepository.findById(area.getOwnerId()).orElseThrow(InvalidIdException::new);

        if(!hasAdminAccess && !Objects.deepEquals(owner.getGroupId(), params[2]))
            throw new NotAccessException();

        if(trip.getAreas().stream().anyMatch(area1 -> area1.getName().equals(area.getName())))
            throw new InvalidFieldsException("منطقه ای با نام مشابه موجود است");

        trip.getAreas().add(area);
        tripRepository.save(trip);

        return new ResponseEntity<>(area, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Area> findById(ObjectId id, Object... params) {
        return null;
    }


    public void addMembers(ObjectId userId, ObjectId groupId,
                           ObjectId tripId, List<ObjectId> userIds) {

        List<Trip> trips =
                tripRepository.findNotStartedByAreaOwnerId(new Date(), userId);

        Trip wantedTrip = trips.stream().filter(trip -> trip.getId().equals(tripId)).findFirst().orElseThrow(NotAccessException::new);

        int count = userRepository.countByIdsAndGroupId(userIds, groupId);
        if(count != userIds.size())
            throw new NotAccessException();

        wantedTrip.getAreas().stream().filter(area ->
                        area.getOwnerId().equals(userId)).findFirst()
                .ifPresent(area -> {

                    List<ObjectId> members = area.getMembers();
                    userIds.forEach(objectId -> {
                        if(members.contains(objectId)) return;
                        members.add(objectId);
                    });

                    area.setMembers(members);
                    tripRepository.save(wantedTrip);
                });
    }

    public void removeMember(ObjectId userId, ObjectId tripId,
                             ObjectId wantedUserId) {

        List<Trip> trips =
                tripRepository.findNotStartedByAreaOwnerId(new Date(), userId);

        Trip wantedTrip = trips.stream().filter(trip -> trip.getId().equals(tripId)).findFirst().orElseThrow(NotAccessException::new);

        wantedTrip.getAreas().stream().filter(area ->
                        area.getOwnerId().equals(userId)).findFirst()
                .ifPresent(area -> {
                    area.getMembers().remove(wantedUserId);
                    tripRepository.save(wantedTrip);
                });
    }

}
