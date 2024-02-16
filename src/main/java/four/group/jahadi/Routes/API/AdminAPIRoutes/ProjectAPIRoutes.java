package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.DTO.UpdateProjectData;
import four.group.jahadi.Enums.Status;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Service.ProjectService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/project")
@Validated
public class ProjectAPIRoutes {

    @Autowired
    ProjectService projectService;

    @PostMapping(value = "/store")
    @ResponseBody
    public ResponseEntity<Project> store(@RequestBody @Valid ProjectData projectData) {
        return projectService.store(projectData);
    }

    @PutMapping(value = "/update/{projectId}")
    @ResponseBody
    public void update(
            @PathVariable @ObjectIdConstraint ObjectId projectId,
            @RequestBody @Valid UpdateProjectData projectData
    ) {
        projectService.update(projectId, projectData);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public ResponseEntity<List<Project>> list(@RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "status", required = false) Status status
    ) {
        return projectService.list(name, status);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Project> get(@PathVariable @ObjectIdConstraint ObjectId id) {
        return projectService.findById(id);
    }

    @PutMapping(value = "/setProgress/{id}")
    public void setProgress(@PathVariable @ObjectIdConstraint ObjectId id,
                            @RequestParam(value = "progress") @Min(0) @Max(100) int progress) {
        projectService.setProgress(id, progress);
    }

    @DeleteMapping(value = "/remove/{id}")
    public void remove(@PathVariable @ObjectIdConstraint ObjectId id) {
        projectService.remove(id);
    }

}
