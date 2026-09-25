package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.PresenceList;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.Area.PresenceListRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Service.Area.ModuleServiceInArea.checkUsers;

@Service
@RequiredArgsConstructor
public class MembersServiceInArea {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final PresenceListRepository presenceListRepository;


    @Cacheable(
            value = "regionMembers",
            key = "#userId + '_' + #areaId",
            condition = "#returnPresenceList == null || #returnPresenceList == false"
    )
    public ResponseEntity<List<User>> members(ObjectId userId, ObjectId areaId, Boolean returnPresenceList) {
        Area wantedArea = tripRepository.getMembersByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new).getAreas().stream()
                .filter(area -> area.getId().equals(areaId) && area.getOwnerId().equals(userId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<User> users = userRepository.findByIdsIn(wantedArea.getMembers())
                .stream()
                .map(user -> {
                    List<String> accesses = new ArrayList<>();
                    if (wantedArea.getTrainers().contains(user.getId()))
                        accesses.add("مسئول آموزش");
                    if (wantedArea.getInsurancers().contains(user.getId()))
                        accesses.add("مسئول بیمه");
                    if (wantedArea.getLaboratoryManager().contains(user.getId()))
                        accesses.add("مسئول آزمایشگاه");
                    if (wantedArea.getEquipmentManagers().contains(user.getId()))
                        accesses.add("مسئول تجهیزات");
                    if (wantedArea.getDispatchers().contains(user.getId()))
                        accesses.add("مسئول پذیرش");
                    if (wantedArea.getPharmacyManagers().contains(user.getId()))
                        accesses.add("مسئول داروخانه");
                    wantedArea.getModules()
                            .stream()
                            .filter(module -> module.getMembers().contains(user.getId()))
                            .forEach(module -> {
                                accesses.add(module.getModuleName());
                            });
                    user.setAreaTripAccesses(accesses);
                    return user;
                }).collect(Collectors.toList());

        if (Objects.equals(Boolean.TRUE, returnPresenceList)) {
            List<ObjectId> presenceLists =
                    presenceListRepository.getLastPresenceByUsersAndAreaId(areaId, wantedArea.getMembers());
            for (ObjectId uId : presenceLists) {
                users
                        .stream()
                        .filter(user -> user.getId().equals(uId))
                        .findFirst()
                        .ifPresent(user -> {
                            user.setPresent(true);
                        });
            }
        }

        return new ResponseEntity<>(
                users,
                HttpStatus.OK
        );
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addMembers(ObjectId userId, ObjectId groupId,
                           ObjectId areaId, List<ObjectId> userIds) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
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

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeMember(ObjectId userId, ObjectId areaId,
                             ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getMembers().remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    private ResponseEntity<List<User>> returnUsers(List<ObjectId> ids) {
        if (ids == null || ids.size() == 0)
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

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
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

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeDispatcher(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> dispatchers = area.getDispatchers();
        if (!dispatchers.contains(wantedUserId))
            throw new InvalidIdException();

        dispatchers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addTrainer(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> trainers = foundArea.getTrainers();
        if (trainers == null)
            trainers = new ArrayList<>();

        for (ObjectId uId : userIds) {
            if (trainers.contains(uId)) continue;
            trainers.add(uId);
        }

        foundArea.setTrainers(trainers);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeTrainer(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> trainers = area.getTrainers();
        if (trainers == null || !trainers.contains(wantedUserId))
            throw new InvalidIdException();

        trainers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getTrainers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getTrainers());
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addPharmacyManager(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> pharmacyManagers = foundArea.getPharmacyManagers();
        if (pharmacyManagers == null)
            pharmacyManagers = new ArrayList<>();

        for (ObjectId uId : userIds) {
            if (pharmacyManagers.contains(uId)) continue;
            pharmacyManagers.add(uId);
        }

        foundArea.setPharmacyManagers(pharmacyManagers);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removePharmacyManager(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> pharmacyManagers = area.getPharmacyManagers();
        if (pharmacyManagers == null || !pharmacyManagers.contains(wantedUserId))
            throw new InvalidIdException();

        pharmacyManagers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getPharmacyManagers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getPharmacyManagers());
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addEquipmentManager(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> equipmentManagers = foundArea.getEquipmentManagers();
        if (equipmentManagers == null)
            equipmentManagers = new ArrayList<>();

        for (ObjectId uId : userIds) {
            if (equipmentManagers.contains(uId)) continue;
            equipmentManagers.add(uId);
        }

        foundArea.setEquipmentManagers(equipmentManagers);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeEquipmentManager(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);
        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> equipmentManagers = area.getEquipmentManagers();
        if (equipmentManagers == null || !equipmentManagers.contains(wantedUserId))
            throw new InvalidIdException();

        equipmentManagers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getEquipmentManagers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getEquipmentManagers());
    }

    public ResponseEntity<List<User>> getLaboratoryManagers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getLaboratoryManager());
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addLaboratoryManager(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> laboratoryManager = foundArea.getLaboratoryManager();
        if (laboratoryManager == null)
            laboratoryManager = new ArrayList<>();

        for (ObjectId uId : userIds) {
            if (laboratoryManager.contains(uId)) continue;
            laboratoryManager.add(uId);
        }

        foundArea.setLaboratoryManager(laboratoryManager);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeLaboratoryManager(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> laboratoryManager = area.getLaboratoryManager();
        if (laboratoryManager == null || !laboratoryManager.contains(wantedUserId))
            throw new InvalidIdException();

        laboratoryManager.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }

    public ResponseEntity<List<User>> getInsurancers(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(InvalidIdException::new);

        return returnUsers(AreaUtils.findArea(trip, areaId, userId).getInsurancers());
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void addInsurancer(ObjectId userId, ObjectId areaId, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        List<ObjectId> insurancers = foundArea.getInsurancers();
        if (insurancers == null)
            insurancers = new ArrayList<>();

        for (ObjectId uId : userIds) {
            if (insurancers.contains(uId)) continue;
            insurancers.add(uId);
        }

        foundArea.setInsurancers(insurancers);
        tripRepository.save(wantedTrip);
    }

    @Caching(evict = {
            @CacheEvict(value = "modules", allEntries = true),
            @CacheEvict(value = "tabs", allEntries = true),
            @CacheEvict(value = "regionMembers", allEntries = true)
    })
    public void removeInsurancer(ObjectId userId, ObjectId areaId, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> insurancers = area.getInsurancers();
        if (insurancers == null || !insurancers.contains(wantedUserId))
            throw new InvalidIdException();

        insurancers.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }
}
