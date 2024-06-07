package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
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
                .filter(area -> area.getId().equals(areaId) && area.getOwnerId().equals(userId)).findFirst().orElseThrow(RuntimeException::new)
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

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
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

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getMembers().remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    private ResponseEntity<List<User>> returnUsers(List<ObjectId> ids) {
        if(ids == null || ids.size() == 0)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        return new ResponseEntity<>(
                userRepository.findByIdsIn(ids),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<User>> getDispatchers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getDispatchers());
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

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> dispatchers = area.getDispatchers();
        if(!dispatchers.contains(wantedUserId))
            throw new InvalidIdException();

        dispatchers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public void addTrainer(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> trainers = foundArea.getTrainers();
        if(trainers == null)
            trainers = new ArrayList<>();

        for(ObjectId uId : userIds) {
            if (trainers.contains(uId)) continue;
            trainers.add(uId);
        }

        foundArea.setTrainers(trainers);
        tripRepository.save(wantedTrip);
    }

    public void removeTrainer(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> trainers = area.getTrainers();
        if(trainers == null || !trainers.contains(wantedUserId))
            throw new InvalidIdException();

        trainers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getTrainers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getTrainers());
    }

    public void addPharmacyManager(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> pharmacyManagers = foundArea.getPharmacyManagers();
        if(pharmacyManagers == null)
            pharmacyManagers = new ArrayList<>();

        for(ObjectId uId : userIds) {
            if (pharmacyManagers.contains(uId)) continue;
            pharmacyManagers.add(uId);
        }

        foundArea.setPharmacyManagers(pharmacyManagers);
        tripRepository.save(wantedTrip);
    }

    public void removePharmacyManager(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> pharmacyManagers = area.getPharmacyManagers();
        if(pharmacyManagers == null || !pharmacyManagers.contains(wantedUserId))
            throw new InvalidIdException();

        pharmacyManagers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getPharmacyManagers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getPharmacyManagers());
    }

    public ResponseEntity<List<User>> getLaboratoryManagers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getLaboratoryManager());
    }

    public void addLaboratoryManager(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> laboratoryManager = foundArea.getLaboratoryManager();
        if(laboratoryManager == null)
            laboratoryManager = new ArrayList<>();

        for(ObjectId uId : userIds) {
            if (laboratoryManager.contains(uId)) continue;
            laboratoryManager.add(uId);
        }

        foundArea.setLaboratoryManager(laboratoryManager);
        tripRepository.save(wantedTrip);
    }

    public void removeLaboratoryManager(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> laboratoryManager = area.getLaboratoryManager();
        if(laboratoryManager == null || !laboratoryManager.contains(wantedUserId))
            throw new InvalidIdException();

        laboratoryManager.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getInsurancers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getInsurancers());
    }
    public void addInsurancer(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> insurancers = foundArea.getInsurancers();
        if(insurancers == null)
            insurancers = new ArrayList<>();

        for(ObjectId uId : userIds) {
            if (insurancers.contains(uId)) continue;
            insurancers.add(uId);
        }

        foundArea.setInsurancers(insurancers);
        tripRepository.save(wantedTrip);
    }

    public void removeInsurancer(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> insurancers = area.getInsurancers();
        if(insurancers == null || !insurancers.contains(wantedUserId))
            throw new InvalidIdException();

        insurancers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }
}
