package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.ProjectData;
import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.UpdateProjectData;
import four.group.jahadi.Enums.Status;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.ProjectService;
import four.group.jahadi.Service.TripService;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/project")
@Validated
public class ProjectAPIRoutes extends Router {

    @Autowired
    ProjectService projectService;
    @Autowired
    TripService tripService;

    @PostMapping(value = "/store")
    @ResponseBody
    public ResponseEntity<Project> store(@RequestBody @Valid ProjectData projectData) {
        return projectService.store(projectData);
    }

    @PostMapping(value = "/addNewTripToProject/{projectId}")
    @Operation(summary = "افزودن اردو جدید به پروژه")
    public void addNewTripToProject(
            @PathVariable @ObjectIdConstraint ObjectId projectId,
            @RequestBody @Valid @Size(min = 1) ValidList<TripStep1Data> data
    ) {
        tripService.store(projectId, data);
    }

    @PutMapping(value = "/resetGroupAccessesForTrip/{projectId}/{tripId}")
    @Operation(summary = "ریست کردن دسترسی های موجود در یک اردو در یک پروژه")
    public void resetGroupAccessesForTrip(
            @PathVariable @ObjectIdConstraint ObjectId projectId,
            @PathVariable @ObjectIdConstraint ObjectId tripId,
            @RequestBody @Valid @Size(min = 1) ValidList<TripStep1Data> data
    ) {
        tripService.resetGroupAccessesForTrip(projectId, tripId, data);
    }

    @DeleteMapping(value = "/removeTripFromProject/{projectId}/{tripId}")
    @Operation(summary = "حذف یک اردو از پروژه")
    public void removeTripFromProject(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId projectId,
            @PathVariable @ObjectIdConstraint ObjectId tripId
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        tripService.removeTripFromProject(projectId, tripId, user.getId(), user.getNid(), null);
    }

    @DeleteMapping(value = "/remove/{projectId}")
    @Operation(summary = "حذف یک پروژه")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId projectId
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        projectService.remove(projectId, user.getId(), user.getNid(), null);
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

}
