package four.group.jahadi.Service.Area;


import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ModuleServiceInArea {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    public void addModule(ObjectId userId, ObjectId areaId, List<ObjectId> moduleIds) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        List<Module> foundModules = moduleRepository.findByIds(moduleIds);
        if (foundModules.size() != moduleIds.size())
            throw new NotAccessException();

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ModuleInArea> modules = foundArea.getModules();
        moduleIds.forEach(objectId -> {
            if (modules.stream().noneMatch(moduleInArea -> moduleInArea.getModuleId().equals(objectId))) {

                Module foundModule = foundModules.stream().filter(module -> module.getId().equals(objectId))
                        .findFirst().get();

                modules.add(ModuleInArea.builder()
                        .id(new ObjectId())
                        .moduleId(objectId)
                        .moduleName(foundModule.getName())
                        .build()
                );
            }
        });

        foundArea.setModules(modules);
        tripRepository.save(wantedTrip);
    }

    public void removeModule(ObjectId userId, ObjectId areaId, List<ObjectId> moduleIds) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ModuleInArea> moduleInAreas = foundArea.getModules();
        int count = moduleInAreas.size();

        moduleInAreas.removeIf(moduleInArea -> moduleIds.contains(moduleInArea.getId()));
        if (moduleInAreas.size() != count) {
            foundArea.setModules(moduleInAreas);
            tripRepository.save(wantedTrip);
        }
    }

    public ResponseEntity<List<ModuleInArea>> modules(ObjectId userId, ObjectId areaId) {

        Area foundArea = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(InvalidIdException::new)
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ModuleInArea> modules = foundArea.getModules();

        List<ObjectId> userIds = modules.stream()
                .map(ModuleInArea::getMembers)
                .flatMap(List::stream).distinct().collect(Collectors.toList());

        List<User> users = userRepository.findBy_idIn(userIds);
        modules.forEach(moduleInArea -> {

            List<ObjectId> members = moduleInArea.getMembers();
            List<User> usersInModule = new ArrayList<>();

            members.forEach(objectId -> usersInModule.add(users.stream().filter(user -> user.getId().equals(objectId)).findFirst()
                    .get()));

            moduleInArea.setUsers(usersInModule);
        });

        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    static Object[] checkUsers(ObjectId userId, ObjectId areaId,
                               List<ObjectId> userIds, TripRepository tripRepository
    ) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ObjectId> membersInArea = foundArea.getMembers();
        AtomicBoolean hasIllegalUser = new AtomicBoolean(false);

        userIds.forEach(objectId -> {
            if (!membersInArea.contains(objectId)) {
                hasIllegalUser.set(true);
            }
        });

        if (hasIllegalUser.get())
            throw new InvalidFieldsException("مقادیر ورودی معتبر نمی باشد");

        return new Object[]{wantedTrip, foundArea};
    }

    public void addMembersToModule(ObjectId userId, ObjectId areaId,
                                   ObjectId moduleIdInArea, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        ModuleInArea foundModuleInArea = foundArea.getModules().stream()
                .filter(moduleInArea -> moduleInArea.getId().equals(moduleIdInArea)).findFirst()
                .orElseThrow(InvalidIdException::new);

        List<ObjectId> members = foundModuleInArea.getMembers();
        for (ObjectId oId : userIds) {
            if (members.contains(oId)) continue;
            members.add(oId);
        }

        foundModuleInArea.setMembers(members);
        tripRepository.save(wantedTrip);
    }

    public void removeMemberFromModule(ObjectId userId, ObjectId areaId,
                                       ObjectId moduleIdInArea, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        ModuleInArea foundModuleInArea = foundArea.getModules().stream()
                .filter(moduleInArea -> moduleInArea.getId().equals(moduleIdInArea)).findFirst()
                .orElseThrow(InvalidIdException::new);

        List<ObjectId> members = foundModuleInArea.getMembers();
        if (!members.contains(wantedUserId))
            throw new InvalidIdException();

        members.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }


    public void addSecretariesToModule(ObjectId userId, ObjectId areaId,
                                       ObjectId moduleIdInArea, List<ObjectId> userIds) {

        Object[] tmp = checkUsers(userId, areaId, userIds, tripRepository);
        Trip wantedTrip = (Trip) tmp[0];
        Area foundArea = (Area) tmp[1];

        ModuleInArea foundModuleInArea = foundArea.getModules().stream()
                .filter(moduleInArea -> moduleInArea.getId().equals(moduleIdInArea)).findFirst()
                .orElseThrow(InvalidIdException::new);

        List<ObjectId> secretaries = foundModuleInArea.getSecretaries();
        for (ObjectId oId : userIds) {
            if (secretaries.contains(oId)) continue;
            secretaries.add(oId);
        }

        foundModuleInArea.setSecretaries(secretaries);
        tripRepository.save(wantedTrip);
    }

    public void removeMemberFromSecretaries(ObjectId userId, ObjectId areaId,
                                            ObjectId moduleIdInArea, ObjectId wantedUserId) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        ModuleInArea foundModuleInArea = foundArea.getModules().stream()
                .filter(moduleInArea -> moduleInArea.getId().equals(moduleIdInArea)).findFirst()
                .orElseThrow(InvalidIdException::new);

        List<ObjectId> secretaries = foundModuleInArea.getSecretaries();
        if (!secretaries.contains(wantedUserId))
            throw new InvalidIdException();

        secretaries.remove(wantedUserId);
        tripRepository.save(wantedTrip);
    }
}
