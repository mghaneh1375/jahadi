package four.group.jahadi.Service;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.Trip.TripStep2Data;
import four.group.jahadi.DTO.Trip.TripStepData;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Service.Area.AreaService;
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

import static four.group.jahadi.Utility.Utility.*;

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
    @Autowired
    private AreaService areaService;


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

    public ResponseEntity<List<Trip>> myActiveTrips(ObjectId groupId) {
        return new ResponseEntity<>(tripRepository.findActivesByGroupId(
                groupId, getCurrDate()
        ), HttpStatus.OK);
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
        Date startAt = getDate(new Date(dto.getStartAt()));
        Date endAt = getLastDate(new Date(dto.getEndAt()));

        Project project = projectRepository.findById(trip.getProjectId()).get();
        // Validate dates
        if(project.getStartAt().after(startAt))
            throw new InvalidFieldsException("تاریخ شروع باید از " + Utility.convertDateToJalali(project.getStartAt()) + " باشد");
        if(project.getEndAt().before(endAt))
            throw new InvalidFieldsException("تاریخ اتمام باید از " + Utility.convertDateToJalali(project.getEndAt()) + " باشد");

        trip.setName(dto.getName());
        trip.setStartAt(startAt);
        trip.setEndAt(endAt);

        tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<Trip> store(TripStepData dto, Object... params) {
        return null;
    }

    public void removeTrip(Trip trip, ObjectId userId, String username) {
        if (trip.getStartAt() != null &&
                Utility.getCurrDate().after(trip.getStartAt())
        )
            throw new InvalidFieldsException("اردو آغاز شده و امکان حدف آن وجود ندارد");

        if (trip.getAreas() != null)
            trip.getAreas().forEach(area -> areaService.remove(trip, area.getId(), userId, username, false));

        tripRepository.delete(trip);
    }

    public void removeTripFromProject(
            ObjectId projectId, ObjectId tripId,
            ObjectId userId, String username
    ) {
        Trip trip = tripRepository.findTripByProjectIdAndId(projectId, tripId).orElseThrow(InvalidIdException::new);
        removeTrip(trip, userId, username);
    }

    public void resetGroupAccessesForTrip(ObjectId projectId, ObjectId tripId, List<TripStep1Data> data) {
        Trip trip = tripRepository.findTripByProjectIdAndId(projectId, tripId).orElseThrow(InvalidIdException::new);
        List<GroupAccess> groupsWithAccess = new ArrayList<>();
        data.forEach(tripStepData -> groupsWithAccess.add(
                GroupAccess.builder()
                        .groupId(tripStepData.getOwner())
                        .writeAccess(tripStepData.getWriteAccess())
                        .build()
        ));

        trip.setGroupsWithAccess(groupsWithAccess);
        tripRepository.save(trip);
    }

    public void store(ObjectId projectId, List<TripStep1Data> data) {
        Project project = projectRepository.findById(projectId).orElseThrow(InvalidIdException::new);
        Trip trip = Trip
                .builder()
                .projectId(project.getId())
                .name(null)
                .build();

        List<GroupAccess> groupsWithAccess = new ArrayList<>();
        data.forEach(tripStepData -> groupsWithAccess.add(
                GroupAccess.builder()
                        .groupId(tripStepData.getOwner())
                        .writeAccess(tripStepData.getWriteAccess())
                        .build()
        ));

        trip.setGroupsWithAccess(groupsWithAccess);
        tripRepository.save(trip);
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
