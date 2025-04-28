package four.group.jahadi.Tests;

import four.group.jahadi.Enums.*;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Tests.Modules.ModuleSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private static final List<User> jahadgars = new ArrayList<>();
    private static final List<User> groupUsers = new ArrayList<>();
    private static final List<Group> groups = new ArrayList<>();

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
    }

}
