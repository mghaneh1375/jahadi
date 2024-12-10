package four.group.jahadi.Service.Area;


import four.group.jahadi.Enums.AccessInModuleArea;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Area.PatientForm;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Tests.Modules.ModuleSeeder.tabIcons;

@Service
public class ModuleServiceInArea {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;

    public void setModules(ObjectId userId, ObjectId areaId, List<ObjectId> moduleIds) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        List<Module> foundModules = moduleRepository.findByIds(moduleIds);
//        if (foundModules.size() != moduleIds.size())
//            throw new NotAccessException();

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ModuleInArea> modules = foundArea.getModules();
        List<ModuleInArea> newList = new ArrayList<>();
        moduleIds.forEach(objectId -> {
            Optional<ModuleInArea> moduleInAreaOptional =
                    modules.stream().filter(moduleInArea -> moduleInArea.getModuleId().equals(objectId)).findFirst();
            if (moduleInAreaOptional.isEmpty()) {
                foundModules
                        .stream()
                        .filter(module -> module.getId().equals(objectId))
                        .findFirst()
                        .ifPresent(foundModule -> newList.add(ModuleInArea.builder()
                                .id(new ObjectId())
                                .moduleId(objectId)
                                .moduleName(foundModule.getName())
                                .moduleTabName(foundModule.getTabName())
                                .build()
                        ));
            } else
                newList.add(moduleInAreaOptional.get());
        });

        foundArea.setModules(newList);
        tripRepository.save(wantedTrip);
    }

    public void removeModule(ObjectId userId, ObjectId areaId, List<ObjectId> moduleIds) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
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

    public ResponseEntity<List<ModuleInArea>> getModulesInTab(ObjectId userId, ObjectId areaId, String tabName) {

        Area foundArea = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(InvalidIdException::new)
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        boolean isOwner = foundArea.getOwnerId().equals(userId);
        List<ModuleInArea> modules = foundArea.getModules();

        List<Module> areaModules = moduleRepository.findTabNamesByIds(
                foundArea.getModules().stream().map(ModuleInArea::getModuleId).collect(Collectors.toList())
        );

        if (areaModules.size() == 0)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        List<ModuleInArea> output = new ArrayList<>();

        modules.forEach(moduleInArea -> {
            if (areaModules.stream()
                    .noneMatch(module -> module.getId().equals(moduleInArea.getModuleId()) && module.getTabName().equals(tabName)))
                return;

            List<ObjectId> members = moduleInArea.getMembers();
            List<ObjectId> secretaries = moduleInArea.getSecretaries();

            moduleInArea.setAccesses(isOwner ? Collections.singletonList(AccessInModuleArea.FULL) :
                    !members.contains(userId) && !secretaries.contains(userId) ? Collections.singletonList(AccessInModuleArea.NONE) :
                            members.contains(userId) && secretaries.contains(userId) ?
                                    List.of(AccessInModuleArea.RESPONSIBLE, AccessInModuleArea.SECRETARY) :
                                    members.contains(userId) ? Collections.singletonList(AccessInModuleArea.RESPONSIBLE) :
                                            Collections.singletonList(AccessInModuleArea.SECRETARY)
            );

            output.add(moduleInArea);
        });

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public ResponseEntity<List<ModuleInArea>> modules(ObjectId userId, ObjectId areaId) {

        Area foundArea = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(InvalidIdException::new)
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        boolean isOwner = foundArea.getOwnerId().equals(userId);

        List<ModuleInArea> modules = foundArea.getModules();
        Set<ObjectId> userIds = new HashSet<>();

        for (ModuleInArea module : modules) {
            userIds.addAll(module.getMembers());
            userIds.addAll(module.getSecretaries());
        }

        List<User> users = userRepository.findByIdsIn(new ArrayList<>(userIds));
        modules.forEach(moduleInArea -> {

            List<ObjectId> members = moduleInArea.getMembers();
            List<ObjectId> secretaries = moduleInArea.getSecretaries();
            List<User> usersInModule = new ArrayList<>();
            List<User> secretariesInModule = new ArrayList<>();

            members.forEach(objectId -> usersInModule.add(users.stream().filter(user -> user.getId().equals(objectId)).findFirst()
                    .get()));

            secretaries.forEach(objectId -> secretariesInModule.add(
                            users.stream().filter(user -> user.getId().equals(objectId)).findFirst()
                                    .get()
                    )
            );

            moduleInArea.setUsers(usersInModule);
            moduleInArea.setSecretaryUsers(secretariesInModule);

            moduleInArea.setAccesses(isOwner ? Collections.singletonList(AccessInModuleArea.FULL) :
                    !members.contains(userId) && !secretaries.contains(userId) ? Collections.singletonList(AccessInModuleArea.NONE) :
                            members.contains(userId) && secretaries.contains(userId) ?
                                    List.of(AccessInModuleArea.RESPONSIBLE, AccessInModuleArea.SECRETARY) :
                                    members.contains(userId) ? Collections.singletonList(AccessInModuleArea.RESPONSIBLE) :
                                            Collections.singletonList(AccessInModuleArea.SECRETARY)
            );
        });

        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    public ResponseEntity<List<ModuleInArea>> modulesForJahadgar(ObjectId userId, ObjectId areaId, String tabName) {

        Area foundArea = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(InvalidIdException::new)
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        List<ModuleInArea> modules = tabName == null
                ? foundArea.getModules()
                : foundArea.getModules().stream().filter(module -> Objects.equals(module.getModuleTabName(), tabName)).collect(Collectors.toList());
        modules.forEach(moduleInArea -> {
            moduleInArea.setHasSecretaryAccess(moduleInArea.getSecretaries().contains(userId));
            moduleInArea.setHasDoctorAccess(moduleInArea.getMembers().contains(userId));
        });

        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> tabs(ObjectId userId, ObjectId areaId) {

        Area foundArea = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(InvalidIdException::new)
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        HashMap<String, String> tabs = new HashMap<>();
        foundArea.getModules()
                .stream()
                .map(ModuleInArea::getModuleTabName)
                .distinct().forEach(s -> tabs.put(s, tabIcons.getOrDefault(s, "")));

        return ResponseEntity.ok(tabs);
    }

    public ResponseEntity<Module> getModule(ObjectId userId, ObjectId areaId, ObjectId moduleId) {

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        AreaUtils.findModule(
                area, moduleId, null, null
// todo: check
//                userId.equals(area.getOwnerId()) ? null : userId,
//                userId.equals(area.getOwnerId()) ? null : userId
        );

        Module module = moduleRepository.findById(moduleId).orElseThrow(UnknownError::new);
        module.getSubModules().forEach(subModule -> subModule.setQuestions(null));

        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    public ResponseEntity<SubModule> getSubModule(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId subModuleId,
            ObjectId patientId
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        AreaUtils.findModule(
                area, moduleId,
                null, null
//              todo:
//                userId.equals(area.getOwnerId()) ? null : userId,
//                userId.equals(area.getOwnerId()) ? null : userId
        );

        Module module = moduleRepository.findById(moduleId).orElseThrow(UnknownError::new);
        SubModule wantedSubModule = module.getSubModules().stream().filter(subModule -> Objects.equals(subModule.getId(), subModuleId)).findFirst()
                .orElseThrow(InvalidIdException::new);

        if (patientId != null) {
            AtomicBoolean hasForm = new AtomicBoolean(false);
            patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                    .ifPresent(patientInArea -> {
                        if (patientInArea.getReferrals() == null) return;
                        if (patientInArea.getReferrals().stream().filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                                .map(patientReferral -> patientReferral.getForms().stream().map(PatientForm::getSubModuleId).collect(Collectors.toList()))
                                .collect(Collectors.toList())
                                .stream()
                                .flatMap(List::stream)
                                .collect(Collectors.toList()).contains(subModuleId))
                            hasForm.set(true);
                    });
            wantedSubModule.setHasPatientForm(hasForm.get());
        }
        return new ResponseEntity<>(wantedSubModule, HttpStatus.OK);
    }

    static Object[] checkUsers(ObjectId userId, ObjectId areaId,
                               List<ObjectId> userIds, TripRepository tripRepository
    ) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<ObjectId> membersInArea = area.getMembers();
        AtomicBoolean hasIllegalUser = new AtomicBoolean(false);

        userIds.forEach(objectId -> {
            if (!membersInArea.contains(objectId)) {
                hasIllegalUser.set(true);
            }
        });

        if (hasIllegalUser.get())
            throw new InvalidFieldsException("مقادیر ورودی معتبر نمی باشد");

        return new Object[]{wantedTrip, area};
    }

    public synchronized void addMembersToModule(ObjectId userId, ObjectId areaId,
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

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
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


    public synchronized void addSecretariesToModule(ObjectId userId, ObjectId areaId,
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

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
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
