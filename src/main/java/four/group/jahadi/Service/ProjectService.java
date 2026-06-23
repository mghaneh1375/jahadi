package four.group.jahadi.Service;

import four.group.jahadi.DTO.Area.AreaDigest;
import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.DTO.Trip.TripDigest;
import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.UpdateProjectData;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Enums.Status;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Utility.Utility;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.getLastLocalDateTime;
import static four.group.jahadi.Utility.Utility.getLocalDateTime;


@Service
@RequiredArgsConstructor
public class ProjectService extends AbstractService<Project, ProjectData> {

    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripService tripService;

    // filters:
    // 1- name
    // 2- status
    @Override
    public ResponseEntity<List<Project>> list(Object... filters) {
        return null;
    }

    @Override
    public ResponseEntity<Page<Project>> paginateList(int pageIndex, int pageSize, Object... filters) {
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

        Page<Project> projects = projectRepository.findAllWithFilterWithPagination(
                Project.class,
                FilteringFactory.abstractParseFromParams(filtersList, Project.class),
                Pageable.ofSize(pageSize).withPage(pageIndex),
                Sort.by("start_at").descending()
        );

        Map<ObjectId, List<ObjectId>> tripsPerProject = tripRepository.findTripByProjectIds(
                        projects.stream().map(Project::getId).collect(Collectors.toList())
                )
                .stream()
                .collect(Collectors.groupingBy(
                        Trip::getProjectId,
                        HashMap::new,
                        Collectors.mapping(Model::getId, Collectors.toList())
                ));

        projects.forEach(x -> {
            x.setGroups(new ArrayList<>());
            x.setTripIds(tripsPerProject.get(x.getId()));
        });
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

    public void setProgress(ObjectId id, int progress) {
        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        project.setProgress(progress);
        projectRepository.save(project);
    }

    //    @Transactional
    @CacheEvict(value = "groupStatisticData", allEntries = true)
    public void remove(ObjectId id, ObjectId userId, String username, ObjectId groupId) {
        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        if (project.getStartAt().isBefore(Utility.getCurrLocalDateTime()))
            throw new InvalidFieldsException("پروژه آغاز شده و امکان حدف آن وجود ندارد");

        tripRepository
                .findTripByProjectId(project.getId())
                .forEach(trip -> tripService.removeTrip(trip, userId, username, groupId));
        projectRepository.delete(project);
    }

    public void update(ObjectId id, UpdateProjectData dto) {
        if(dto.getEndAt() != null && dto.getStartAt() > dto.getEndAt()) {
            throw new InvalidFieldsException("تاریخ اتمام باید از شروع بزرگ تر باشد");
        }

        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        project = populateEntity(project, dto);

        try {
            projectRepository.save(project);
        } catch (Exception x) {
            throw new InvalidFieldsException("نام وارد شده تکراری است");
        }
    }

    @Override
    public void update(ObjectId id, ProjectData dto, Object... params) {
    }

    public ResponseEntity<Project> findById(ObjectId id, Object... params) {
        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        project.setTripsGroupAccess(
                tripRepository.findTripExcludeAreaByProjectId(project.getId())
                        .stream()
                        .map(trip -> trip.getGroupsWithAccess().stream().map(groupAccess -> JSONGroupAccess
                                .builder()
                                .groupId(groupAccess.getGroupId())
                                .writeAccess(groupAccess.getWriteAccess())
                                .build()).collect(Collectors.toList()))
                        .collect(Collectors.toList())
        );

        return new ResponseEntity<>(
                project,
                HttpStatus.OK
        );
    }

    @CacheEvict(value = "groupStatisticData", allEntries = true)
    public ResponseEntity<Project> store(ProjectData data, Object... params) {

        if (projectRepository.countByName(data.getName()) > 0)
            throw new InvalidFieldsException("نام وارد شده تکراری است");

        List<ObjectId> groupIds = data.getTrips().stream()
                .map(x -> x.stream().map(TripStep1Data::getOwner).collect(Collectors.toList()))
                .flatMap(List::stream).distinct().collect(Collectors.toList());

        if (groupRepository.countBy_idIn(groupIds) != groupIds.size())
            throw new InvalidFieldsException("آی دی گروه ها نامعتبر است");

        Project project = populateEntity(null, data);
        projectRepository.insert(project);

        data.getTrips().forEach(x -> {
            Trip trip = Trip
                    .builder()
                    .projectId(project.getId())
                    .name(null)
                    .build();

            List<GroupAccess> groupsWithAccess = new ArrayList<>();
            x.forEach(tripStep1Data -> groupsWithAccess.add(GroupAccess.builder()
                            .groupId(tripStep1Data.getOwner())
                            .writeAccess(tripStep1Data.getWriteAccess())
                            .build()
                    )
            );

            trip.setGroupsWithAccess(groupsWithAccess);
            tripRepository.save(trip);
        });

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    Project populateEntity(Project project, ProjectData projectData) {
        return Project.builder()
                .groupIds(projectData.getTrips().stream()
                        .map(x -> x.stream().map(TripStep1Data::getOwner)
                                .collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList()))
                .name(projectData.getName())
                .color(projectData.getColor())
                .startAt(getLocalDateTime(new Date(projectData.getStartAt())))
                .endAt(getLastLocalDateTime(new Date(projectData.getEndAt())))
                .build();
    }


    Project populateEntity(Project project, UpdateProjectData projectData) {
        project.setName(projectData.getName());
        project.setColor(Color.WHITE);
        project.setStartAt(getLocalDateTime(new Date(projectData.getStartAt())));
        project.setEndAt(getLastLocalDateTime(new Date(projectData.getEndAt())));
        return project;
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
                projectRepository.findActivesByOwner(Collections.singletonList(groupId), Utility.getCurrLocalDateTime());
        List<Project> result = new ArrayList<>();

        projects.forEach(project -> {
            List<Trip> trips =
                    tripRepository.findNeedActionByGroupId(Utility.getCurrLocalDateTime(), groupId, project.getId());

            if (trips.size() == 0)
                return;

            List<TripDigest> tripDigests = new ArrayList<>();
            for (Trip trip : trips) {
                if (trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                        groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId)
                ))
                    continue;

                tripDigests.add(
                        TripDigest
                                .builder()
                                .id(trip.getId())
                                .name(trip.getName())
                                .startAt(trip.getStartAt())
                                .endAt(trip.getEndAt())
                                .dailyStartAt(trip.getDailyStartAt())
                                .dailyEndAt(trip.getDailyEndAt())
                                .areas(
                                        trip.getAreas().stream().map(area -> {
                                            return AreaDigest
                                                    .builder()
                                                    .id(area.getId())
                                                    .ownerId(area.getOwnerId())
                                                    .name(area.getName())
                                                    .build();
                                        }).collect(Collectors.toList())
                                )
                                .build()
                );
            }

            if (tripDigests.size() == 0)
                return;

            project.setTripDigests(tripDigests);
            result.add(project);
        });

        return result;
    }

}
