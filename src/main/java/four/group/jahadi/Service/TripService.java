package four.group.jahadi.Service;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.Trip.TripStep2Data;
import four.group.jahadi.DTO.Trip.TripStepData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.getDate;

@Service
public class TripService extends AbstractService<Trip, TripStepData> {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;


    // 1- groupId
    // 2- status
    @Override
    public ResponseEntity<List<Trip>> list(Object... filters) {

        List<List<Object>> filtersList = new ArrayList<>();

        if (filters[0] != null) {
            filtersList.add(new ArrayList<>() {
                {
                    add("groups_with_access.group_id");
                    add("eq");
                    add(filters[0]);
                }
            });
        }

        List<Trip> trips = tripRepository.findAllWithFilter(
                Trip.class,
                FilteringFactory.abstractParseFromParams(filtersList, Trip.class)
        );

        List<Group> groups = groupRepository.findByIdsIn(findFromTripGroupIds(trips));

        trips.forEach(trip -> {
            for (Group group : groups) {
                trip.getGroupsWithAccess().stream()
                        .filter(x -> x.getGroupId().equals(group.getId()))
                        .findFirst().ifPresent(groupAccess -> {
                            groupAccess.setGroup(group);
                        });
            }
        });

        List<User> users = userRepository.findByIdsIn(
                trips.stream().map(Trip::getAreas)
                        .map(areas -> areas.stream().map(Area::getOwnerId).collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList())
        );

        trips.forEach(trip -> trip.getAreas().forEach(area -> users.stream()
                .filter(user -> user.getId().equals(area.getOwnerId()))
                .findFirst().ifPresent(area::setOwner)));

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    public ResponseEntity<List<Trip>> inProgressTripsForGroupAccess(ObjectId groupId) {

        List<Trip> trips;

        try {
            trips = tripRepository.findActivesOrNotStartedProjectsByGroupId(
                    Utility.getCurrDate(), groupId
            );
        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }

        List<User> users = userRepository.findByIdsIn(
                trips.stream().map(Trip::getAreas)
                        .map(areas -> areas.stream().map(Area::getOwnerId).collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList())
        );

        trips.forEach(trip -> trip.getAreas().forEach(area -> users.stream()
                .filter(user -> user.getId().equals(area.getOwnerId()))
                .findFirst().ifPresent(area::setOwner)));

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, TripStepData data, Object... params) {

        Trip trip = tripRepository.findById(id).orElseThrow(InvalidIdException::new);

        boolean hasAdminAccess = (boolean) params[0];

        if (!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(params[1]))
        )
            throw new NotAccessException();

        TripStep2Data dto = (TripStep2Data) data;

        trip.setName(dto.getName());
        trip.setStartAt(getDate(new Date(dto.getStartAt())));
        trip.setEndAt(getDate(new Date(dto.getEndAt())));

        tripRepository.save(trip);
    }

    // in update project scenario where admin want to add additional trip
    @Override
    public ResponseEntity<Trip> store(TripStepData data, Object... params) {

        ObjectId projectId = (ObjectId) params[0];

        Project project = projectRepository.findById(projectId).orElseThrow(InvalidIdException::new);

        TripStep1Data dto = (TripStep1Data) data;

//        List<GroupAccess> groupsWithAccess = tr;
//        groupsWithAccess.add(GroupAccess.builder()
//                .groupId(dto.getOwner())
//                .writeAccess(dto.getWriteAccess())
//                .build()
//        );

//        Trip trip = Trip
//                .builder()
//                .groupsWithAccess()
//                .projectId(project.getId())
//                .name(project.getName() + " - ").build();

//        tripRepository.save(trip);

//        return new ResponseEntity<>(trip, HttpStatus.OK);
        return null;
    }

    @Override
    public ResponseEntity<Trip> findById(ObjectId id, Object... params) {
        return null;
    }

    public ResponseEntity<List<Group>> getGroupsWhichHasActiveTrip() {

        List<Trip> activeTrips = tripRepository.findActives(Utility.getCurrDate());

        List<Group> groups = groupRepository.findByIdsIn(findFromTripGroupIds(activeTrips));
        groups.forEach(x -> x.setAreas(new ArrayList<>()));

        GroupService.fillGroupByUsers(groups, userRepository);

        activeTrips.forEach(trip -> {

            for (Group group : groups) {
                if (trip.getGroupsWithAccess().stream().anyMatch(x -> x.getGroupId().equals(group.getId())))
                    group.getAreas().addAll(trip.getAreas());
            }

        });

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    private List<ObjectId> findFromTripGroupIds(List<Trip> trips) {
        return trips.stream()
                .map(Trip::getGroupsWithAccess).collect(Collectors.toList())
                .stream().map(x -> x.stream().map(GroupAccess::getGroupId).collect(Collectors.toList()))
                .flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    public ResponseEntity<List<Trip>> getGroupsTrips(ObjectId groupId) {

        List<Trip> trips = tripRepository.findByGroupId(groupId);
        List<ObjectId> projectIds = trips.stream().map(Trip::getProjectId)
                .distinct().collect(Collectors.toList());

        List<Group> groups = groupRepository.findByIdsIn(findFromTripGroupIds(trips));
        List<User> users = userRepository.findByIdsIn(groups.stream().map(Group::getOwner).distinct().collect(Collectors.toList()));

        groups.forEach(group ->
                users.stream().filter(user -> user.getId().equals(group.getOwner())).findFirst()
                        .ifPresent(group::setUser)
        );

        List<Project> projects = projectRepository.findDigestByIds(projectIds);
        trips.forEach(trip -> {

            trip.getGroupsWithAccess().forEach(groupAccess ->
                    groups.stream().filter(group -> group.getId().equals(groupAccess.getGroupId()))
                            .findFirst().ifPresent(groupAccess::setGroup)
            );

            projects.stream()
                    .filter(project -> project.getId().equals(trip.getProjectId()))
                    .findFirst().ifPresent(project1 ->
                            trip.setProject(project1.getName())
                    );
        });

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }
}
