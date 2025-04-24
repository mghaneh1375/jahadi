package four.group.jahadi.Service;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GroupService extends AbstractService<Group> {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TripService tripService;

    public static void fillGroupByUsers(List<Group> groups, UserRepository userRepository) {
        List<ObjectId> userIds = groups.stream().map(Group::getOwner).collect(Collectors.toList());
        List<User> users = userRepository.findGroupUsersByIdsIn(userIds);
        groups.forEach(x -> x.setUser(users.stream().filter(itr -> x.getOwner().equals(itr.getId())).findFirst().orElse(null)));
    }

    @Override
    public ResponseEntity<List<Group>> list(Object ... filters) {

        List<Group> groups = filters[0] == null ? groupRepository.findAll() :
                groupRepository.findLikeName(filters[0].toString());

        fillGroupByUsers(groups, userRepository);
        groups.forEach(group -> group.setTripsCount(tripRepository.countByGroupId(group.getId())));
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    public ResponseEntity<Group> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                groupRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Group> store(GroupData data, Object ... params) {
        return null;
    }

    public ResponseEntity<HashMap<String, Object>> statisticData(ObjectId groupId) {

        HashMap<String, Object> statisticData = new HashMap<>();

        statisticData.put("members", userRepository.countByGroupId(groupId));
        statisticData.put("surgeries", 0);
        statisticData.put("postRef", 0);
        statisticData.put("totalCosts", 0);
        statisticData.put("totalRefs", 0);
        statisticData.put("activeProjects", projectService.myProjectsNeedAction(groupId));
        statisticData.put("activeTrips", tripService.inProgressTripsForGroupAccess(groupId).getBody());

        return new ResponseEntity<>(statisticData, HttpStatus.OK);
    }
}
