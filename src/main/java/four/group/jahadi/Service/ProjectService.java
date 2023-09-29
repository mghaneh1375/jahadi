package four.group.jahadi.Service;

import four.group.jahadi.DTO.Digest.GroupDigest;
import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Repository.FilteringFactory;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.ProjectRepository;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static four.group.jahadi.Utility.StaticValues.JSON_OK;
import static four.group.jahadi.Utility.Utility.*;

@Service
public class ProjectService extends AbstractService<Project, ProjectData> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GroupRepository groupRepository;

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
                    .map(ObjectId::toString).toList();
            filtersList.add("groupIds|in|" + String.join(";", groupIds));
        }

        List<Project> projects = projectRepository.findAllWithFilter(Project.class,
                FilteringFactory.parseFromParams(filtersList, Project.class)
        );

        List<ObjectId> groupIds = projects.stream().map(Project::getGroupIds)
                .flatMap(Collection::stream).toList();

        List<GroupDigest> groups = groupRepository.findBy_idIn(groupIds);

        JSONArray jsonArray = new JSONArray();

        projects.forEach(x -> {

            try {

                JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(x));

                jsonObject.remove("_id");
                jsonObject.put("id", x.getId().toString());
                jsonObject.put("createdAt", convertDateToJalali(x.getCreatedAt()));
                jsonObject.put("startAt", convertIntToDate(x.getStartAt()));
                jsonObject.put("endAt", convertIntToDate(x.getEndAt()));

                List<JSONObject> groupsTmp = groups.stream().filter(itr -> x.getGroupIds().contains(itr.get_id()))
                        .map(AbstractService::getDTO).toList();

                jsonObject.put("groups", groupsTmp);

                jsonArray.put(jsonObject);

            } catch (IOException ignore) {}
        });

//        return generateSuccessMsg("data", jsonArray);
        return null;
    }

    public String setProgress(ObjectId id, int progress) {

        Project project = projectRepository.findById(id).orElseThrow(InvalidIdException::new);

        project.setProgress(progress);
        projectRepository.save(project);

        return JSON_OK;
    }

    @Override
    String update(ObjectId id, ProjectData dto, Object ... params) {
        return null;
    }

    public ResponseEntity<Project> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                projectRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public String store(ProjectData data, Object ... params) {

        if(groupRepository.countBy_idIn(data.getGroupIds()) != data.getGroupIds().size())
            return generateErr("آی دی گروه ها نامعتبر است");

//        data.getTripNos().forEach(x -> {
//
//
//
//        });

        Project project = populateEntity(new Project(), data);

        try {
            projectRepository.insert(project);
        }
        catch (Exception x) {
            return generateErr("نام وارد شده تکراری است");
        }
        return generateSuccessMsg("id", project.getId());
    }

    @Override
    Project populateEntity(Project project, ProjectData projectData) {

        project.setName(projectData.getName());
        project.setColor(projectData.getColor());
        project.setStartAt(Utility.convertStringToDate(projectData.getStartAt()));
        project.setEndAt(Utility.convertStringToDate(projectData.getEndAt()));
        project.setGroupIds(projectData.getGroupIds());

        return project;
    }

}
