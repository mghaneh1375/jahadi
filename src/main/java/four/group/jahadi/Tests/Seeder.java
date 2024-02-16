package four.group.jahadi.Tests;

import four.group.jahadi.Enums.*;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Tests.Modules.ModuleSeeder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Seeder {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static List<User> jahadgars = new ArrayList<>();
    private static List<User> groupUsers = new ArrayList<>();
    private static List<Group> groups = new ArrayList<>();

    public void createUserWithGroup(int idx, int code) {

        User groupUser = User.builder()
                .name("group user " + idx)
                .accesses(Collections.singletonList(Access.GROUP))
                .nid("001891437" + idx)
                .password(passwordEncoder.encode("123456"))
                .color(Color.BLUE)
                .phone("0912123456" + idx)
                .fatherName("aaaa")
                .birthDay("1300/01/02")
                .university("aaaaa")
                .universityYear("1400")
                .sex(Sex.MALE)
                .bloodType(BloodType.A_PLUS)
                .status(AccountStatus.ACTIVE)
                .build();

        userRepository.insert(groupUser);

        Group group = Group.builder()
                .name("group " + idx)
                .code(code)
                .color(Color.BLUE)
                .isActive(true)
                .build();

        group.setOwner(groupUser.getId());
        groupRepository.insert(group);

        groupUser.setGroupId(group.getId());
        groupUser.setGroupName(group.getName());
        userRepository.save(groupUser);

        groupUsers.add(groupUser);
        groups.add(group);
    }

    public void createUser(int idx, Integer groupCode) {

        User user = User.builder()
                .name("jahadgar" + idx)
                .accesses(Collections.singletonList(Access.JAHADI))
                .nid("002003309" + idx)
                .password(passwordEncoder.encode("123456"))
                .color(Color.BLUE)
                .phone("0913123456" + idx)
                .fatherName("aaaa")
                .birthDay("1300/01/02")
                .university("aaaaa")
                .universityYear("1400")
                .sex(Sex.MALE)
                .bloodType(BloodType.A_PLUS)
                .status(AccountStatus.ACTIVE)
                .build();

        if (groupCode != null) {

            Optional<Group> group = groupRepository.findByCode(groupCode);

            if (group.isPresent()) {

                Group g = group.get();

                user.setGroupId(g.getId());
                user.setGroupName(g.getName());
            }
        }

        userRepository.insert(user);
        jahadgars.add(user);
    }

    private Project projectSeeder() {

        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 5);
        startDate = c.getTime();

        Date endDate = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        c2.add(Calendar.DATE, 25);
        endDate = c2.getTime();

        Project project = Project.builder()
                .groupIds(List.of(groups.get(0).getId(), groups.get(1).getId()))
                .startAt(startDate)
                .endAt(endDate)
                .name("sample project")
                .color(Color.BLUE)
                .build();

        projectRepository.insert(project);

        Trip trip = tripSeeder(project.getId());

        return project;
    }

    private Trip tripSeeder(ObjectId projectId) {

        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 10);
        startDate = c.getTime();

        Date endDate = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        c2.add(Calendar.DATE, 20);
        endDate = c2.getTime();

        Area area1 = areaSeeder(jahadgars.get(0).getId());
        Area area2 = areaSeeder(jahadgars.get(1).getId());

        List<ModuleInArea> moduleInAreaList = new ArrayList<>();

        moduleRepository.findAll().forEach(module -> moduleInAreaList.add(
                ModuleInArea
                        .builder()
                        .members(
                                List.of(jahadgars.get(2).getId(), jahadgars.get(3).getId())
                        )
                        .id(new ObjectId())
                        .moduleId(module.getId())
                        .moduleName(module.getName())
                        .secretaries(List.of(jahadgars.get(4).getId()))
                        .build()
        ));

        area1.setModules(moduleInAreaList);
        area1.setMembers(List.of(jahadgars.get(2).getId(), jahadgars.get(3).getId(), jahadgars.get(4).getId()));

        area2.setModules(moduleInAreaList);
        area2.setMembers(List.of(jahadgars.get(2).getId(), jahadgars.get(3).getId(), jahadgars.get(4).getId()));

        Trip trip = Trip.builder()
                .groupsWithAccess(List.of(
                                GroupAccess
                                        .builder()
                                        .writeAccess(true)
                                        .groupId(groupUsers.get(0).getId())
                                        .build(),
                                GroupAccess
                                        .builder()
                                        .writeAccess(false)
                                        .groupId(groupUsers.get(1).getId())
                                        .build()
                        )
                )
                .name("sample trip")
                .no(1)
                .dailyStartAt("10:00")
                .dailyEndAt("23:00")
                .projectId(projectId)
                .startAt(startDate)
                .endAt(endDate)
                .areas(List.of(area1, area2))
                .build();

        tripRepository.insert(trip);
        return trip;
    }

    private Area areaSeeder(ObjectId userId) {

        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 13);
        startDate = c.getTime();

        Date endDate = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        c2.add(Calendar.DATE, 17);
        endDate = c2.getTime();

        City city = cityRepository.findAll().get(0);

        return Area
                .builder()
                .id(new ObjectId())
                .ownerId(userId)
                .dailyStartAt("10:00")
                .dailyEndAt("23:00")
                .startAt(startDate)
                .endAt(endDate)
                .color(Color.BLACK)
                .name("area 1")
                .city(city.getName())
                .cityId(city.getId())
                .stateId(city.getStateId())
                .state("استان")
                .lat(34.3442)
                .lng(44.343)
                .country("ایران")
                .build();
    }

    public void moduleSeeder() {
        ModuleSeeder.seed(moduleRepository);
    }

    public void seed() {

        createUserWithGroup(1, 121234);
        createUserWithGroup(2, 181234);
        createUserWithGroup(3, 281234);

        createUser(1, 121234);
        createUser(2, 121234);

        createUser(3, 181234);
        createUser(4, 181234);
        createUser(5, 181234);

        createUser(6, null);
        createUser(7, 281234);

        projectSeeder();
    }

}
