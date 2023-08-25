package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.Service.ProjectService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/admin/project")
@Validated
public class ProjectAPIRoutes {

    @Autowired
    ProjectService projectService;

    @PostMapping(value = "/store")
    @ResponseBody
    public String store(@RequestBody @Valid ProjectData projectData) {
        return projectService.store(projectData);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "justActive", required = false) Boolean justActive,
                       @RequestParam(value = "justArchive", required = false) Boolean justArchive
    ) {
        return projectService.list(name, justActive, justArchive);
    }


    @PutMapping(value = "/setProgress/{id}")
    @ResponseBody
    public String setProgress(@PathVariable @ObjectIdConstraint ObjectId id,
                              @RequestParam(value = "progress") @Min(0) @Max(100) int progress) {
        return projectService.setProgress(id, progress);
    }

}
