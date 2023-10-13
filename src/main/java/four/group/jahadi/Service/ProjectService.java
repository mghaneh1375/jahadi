package four.group.jahadi.Service;

import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.GroupAccess;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.FilteringFactory;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.ProjectRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.*;

@Service
public class ProjectService extends AbstractService<Project, ProjectData> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TripRepository tripRepository;

    // filters:
    // 1- name
    // 2- justActives
    // 3- justArchives
    @Override
    public ResponseEntity<List<Project>> list(Object ... filters) {

        List<String> filtersList = new ArrayList<>();

        if(filters[0] != null)
            filtersList.add("name|regex|" + filters[0]);

        int today = convertStringToDate(getToday("/"));

        if(filters[1] != null && (boolean)filters[1])
            filtersList.add("startAt|gt|" + today);

        if(filters[2] != null && (boolean)filters[2])
            filtersList.add("endAt|lt|" + today);

        if(filters.length > 3 && filters[3] != null) {
            List<String> groupIds = groupRepository.findByUserId((ObjectId) filters[3]).stream().map(Group::getId)
                    .map(ObjectId::toString).collect(Collectors.toList());
            filtersList.add("groupIds|in|" + String.join(";", groupIds));
        }

        List<Project> projects = projectRepository.findAllWithFilter(Project.class,
                FilteringFactory.parseFromParams(filtersList, Project.class)
        );

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    public void setProgress(ObjectId id, int progress) {

        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);

        project.setProgress(progress);
        projectRepository.save(project);
    }

    @Override
    public void update(ObjectId id, ProjectData dto, Object ... params) {

        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);
        // todo: check for update policies

        project = populateEntity(project, dto);

        try {
            projectRepository.save(project);
        }
        catch (Exception x) {
            throw new InvalidFieldsException("نام وارد شده تکراری است");
        }
    }

    public ResponseEntity<Project> findById(ObjectId id, Object ... params) {

        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);

        List<GroupAccess> groupAccesses = project.getGroupAccesses();
        List<ObjectId> groupIds = groupAccesses.stream().map(GroupAccess::getGroupId).collect(Collectors.toList());

        List<Group> groups = groupRepository.findBy_idIn(groupIds);
        List<GroupAccess> groupAccessesWithGroup = new ArrayList<>();

        for(GroupAccess groupAccess : groupAccesses) {
            groups.stream().filter(e -> e.getId().equals(groupAccess.getGroupId())).findFirst().ifPresent(e -> {
                groupAccess.setGroup(e);
                groupAccessesWithGroup.add(groupAccess);
            });
        }

        project.setGroupAccesses(groupAccesses);

        return new ResponseEntity<>(
                project,
                HttpStatus.OK
        );
    }

    public ResponseEntity<Project> store(ProjectData data, Object ... params) {

        if(data.getTrips() != null) {
            List<ObjectId> groupIds = data.getTrips().stream()
                    .map(TripStep1Data::getOwner)
                    .collect(Collectors.toList());

            if (groupRepository.countBy_idIn(groupIds) != groupIds.size())
                throw new InvalidFieldsException("آی دی گروه ها نامعتبر است");
        }

        Project project = populateEntity(null, data);

        try {
            projectRepository.insert(project);
        }
        catch (Exception x) {
            throw new InvalidFieldsException("نام وارد شده تکراری است");
        }

        if(data.getTrips() != null) {
            final int[] idx = {1};
            data.getTrips().forEach(x -> {

                Trip trip = Trip
                        .builder()
                        .projectId(project.getId())
                        .name(project.getName() + " - " + idx[0]).build();

                idx[0]++;
                trip.setOwner(x.getOwner());
                tripRepository.save(trip);

            });
        }

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    Project populateEntity(Project project, ProjectData projectData) {

        if(project == null)
            return Project.builder()
                    .groupAccesses(projectData.getTrips()
                            .stream().map(e -> GroupAccess.builder()
                                    .groupId(e.getOwner())
                                    .writeAccess(e.getWriteAccess())
                                    .build()
                            ).collect(Collectors.toList()))
                    .name(projectData.getName())
                    .color(projectData.getColor())
                    .startAt(new Date(projectData.getStartAt()))
                    .endAt(new Date(projectData.getEndAt())).build();

        project.setName(projectData.getName());
        project.setColor(projectData.getColor());
        project.setStartAt(new Date(projectData.getStartAt()));
        project.setEndAt(new Date(projectData.getEndAt()));

        return project;
    }

    public ResponseEntity<List<Project>> myProjects(ObjectId groupId) {
        List<Project> projects = projectRepository.findByOwner(groupId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

}
