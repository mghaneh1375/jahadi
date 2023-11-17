package four.group.jahadi.Service;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GroupService extends AbstractService<Group, GroupData> {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TripService tripService;

    public static void fillGroupByUsers(List<Group> groups, UserRepository userRepository) {
        List<ObjectId> userIds = groups.stream().map(Group::getOwner).collect(Collectors.toList());
        List<User> users = userRepository.findBy_idIn(userIds);
        groups.forEach(x -> x.setUser(users.stream().filter(itr -> x.getOwner().equals(itr.getId())).findFirst().orElse(null)));
    }

    @Override
    public ResponseEntity<List<Group>> list(Object ... filters) {

        List<Group> groups = filters[0] == null ? groupRepository.findAll() :
                groupRepository.findLikeName(filters[0].toString());

        fillGroupByUsers(groups, userRepository);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, GroupData dto, Object ... params) {

        Group group = groupRepository.findById(id).orElseThrow(InvalidIdException::new);

        group.setOwner(dto.getOwner());
        group.setName(dto.getName());
        group.setColor(dto.getColor());

        groupRepository.save(group);
    }

    public ResponseEntity<Group> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                groupRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Group> store(GroupData data, Object ... params) {

        if(userRepository.countActiveBy_id(data.getOwner()) == 0)
            throw new InvalidFieldsException("مسئول موردنظر وجود ندارد");

        Group group = populateEntity(new Group(), data);

        try {
            groupRepository.insert(group);
        }
        catch (Exception x) {
            throw new InvalidFieldsException("نام وارد شده تکراری است");
        }

        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    public void changeCode(ObjectId groupId, int code) {

        if(groupId == null)
            throw new NotAccessException();

        Group group = groupRepository.findById(groupId).orElseThrow(InvalidIdException::new);
        group.setCode(code);
        groupRepository.save(group);
    }

    public void toggleActivityStatus(ObjectId groupId, String password) {
        Group group = groupRepository.findById(groupId).orElseThrow(InvalidIdException::new);
        group.setActive(!group.isActive());
        groupRepository.save(group);
    }

    public void setNewOwner(ObjectId groupId, ObjectId userId) {

        User newOwner = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        Group group = groupRepository.findById(groupId).orElseThrow(InvalidIdException::new);

        Optional<User> oldOwner = userRepository.findById(group.getOwner());
        oldOwner.ifPresent(user -> {
            user.getAccesses().remove(Access.GROUP);
            userRepository.save(user);
        });

        group.setOwner(userId);
        groupRepository.save(group);

        if(!newOwner.getAccesses().contains(Access.GROUP)) {
            newOwner.getAccesses().add(Access.GROUP);
            userRepository.save(newOwner);
        }
    }

    public ResponseEntity<HashMap<String, Object>> statisticData(ObjectId groupId) {

        HashMap<String, Object> statisticData = new HashMap<>();

        statisticData.put("members", userRepository.countByGroupId(groupId));
        statisticData.put("surgeries", 0);
        statisticData.put("postRef", 0);
        statisticData.put("totalCosts", 0);
        statisticData.put("totalRefs", 0);
        statisticData.put("activeProjects", projectService.myProjects(groupId, null).getBody());
        statisticData.put("activeTrips", tripService.inProgressTripsForGroupAccess(groupId).getBody());

        return new ResponseEntity<>(statisticData, HttpStatus.OK);
    }
}
