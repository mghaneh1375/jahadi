package four.group.jahadi.Service;

import four.group.jahadi.Enums.Status;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProjectService extends AbstractService<Project> {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TripService tripService;

    // filters:
    // 1- name
    // 2- status
    @Override
    public ResponseEntity<List<Project>> list(Object... filters) {

        List<List<Object>> filtersList = new ArrayList<>();

        if (filters[0] != null)
            filtersList.add(new ArrayList<>() {
                {
                    add("name");
                    add("regex");
                    add(filters[0]);
                }
            });

        LocalDateTime today = Utility.getCurrLocalDateTime();

        if (filters[1] != null) {

            if (Objects.equals(filters[1], Status.IN_PROGRESS)) {
                filtersList.add(new ArrayList<>() {
                    {
                        add("startAt");
                        add("lt");
                        add(today);
                    }
                });
                filtersList.add(new ArrayList<>() {
                    {
                        add("endAt");
                        add("gt");
                        add(today);
                    }
                });
            } else if (Objects.equals(filters[1], Status.FINISHED)) {
                filtersList.add(new ArrayList<>() {
                    {
                        add("endAt");
                        add("lt");
                        add(today);
                    }
                });
            }
        }

        if (filters.length > 3 && filters[3] != null) {
            List<String> groupIds = groupRepository.findByUserId((ObjectId) filters[3]).stream().map(Group::getId)
                    .map(ObjectId::toString).collect(Collectors.toList());
            filtersList.add(new ArrayList<>() {
                {
                    add("groupIds");
                    add("in");
                    add(String.join(";", groupIds));
                }
            });
        }

        List<Project> projects = projectRepository.findAllWithFilter(Project.class,
                FilteringFactory.abstractParseFromParams(filtersList, Project.class)
        );

        projects.forEach(x -> x.setGroups(new ArrayList<>()));

        List<Group> groups = groupRepository.findByIdsIn(projects.stream()
                .map(Project::getGroupIds).collect(Collectors.toList())
                .stream().flatMap(List::stream).distinct().collect(Collectors.toList())
        );

        List<User> users = userRepository.findByIdsIn(groups.stream().map(Group::getOwner).distinct().collect(Collectors.toList()));

        groups.forEach(group ->
                users.stream().filter(user -> user.getId().equals(group.getOwner())).findFirst()
                        .ifPresent(group::setUser)
        );

        projects.forEach(project -> {

            for (Group group : groups) {

                if (project.getGroupIds().stream().noneMatch(x -> x.equals(group.getId())))
                    continue;

                project.getGroups().add(group);
            }
        });

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    public ResponseEntity<Project> findById(ObjectId id, Object... params) {
        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        project.setGroupNames(
                groupRepository.findByIdsIn(project.getGroupIds())
                        .stream().map(Group::getName).collect(Collectors.toList())
        );

        return new ResponseEntity<>(
                project,
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<Project>> myProjects(ObjectId groupId, ObjectId userId) {

        List<Project> projects = new ArrayList<>();
        if (groupId != null)
            projects = projectRepository.findByOwner(Collections.singletonList(groupId));
        else {
            List<Trip> trips = tripRepository.findActivesProjectIdsByAreaOwnerId(Utility.getCurrLocalDateTime(), userId);
            if (trips.size() > 0)
                projects = projectRepository.findByIds(trips.stream().map(Trip::getProjectId).collect(Collectors.toList()));
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    public List<Project> myProjectsNeedAction(ObjectId groupId) {

        List<Project> projects =
                projectRepository.findByOwner(Collections.singletonList(groupId));
        List<Project> result = new ArrayList<>();

        projects.forEach(project -> {
            List<Trip> trips =
                    tripRepository.findNeedActionByGroupId(Utility.getCurrLocalDateTime(), groupId, project.getId());

            if(trips.size() == 0)
                return;

            List<ObjectId> ids = new ArrayList<>();

            for (Trip trip : trips) {
                if (trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                        groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId)
                ))
                    continue;

                ids.add(trip.getId());
            }

            if(ids.size() == 0)
                return;

            project.setTripIds(ids);
            result.add(project);
        });

        return result;
    }

}
