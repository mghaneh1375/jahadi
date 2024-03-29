package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.ModuleServiceInArea;
import four.group.jahadi.Service.ModuleService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "api/region/module")
@Validated
public class RegionModuleAPIRoutes extends Router {

    @Autowired
    private ModuleServiceInArea moduleServiceInArea;

    @Autowired
    private ModuleService moduleService;


    @GetMapping(path = "getAllModules")
    @ResponseBody
    @Operation(summary = "گرفتن کل ماژول های موجود در سیستم")
    public ResponseEntity<List<Module>> getAllModules() {
        return moduleService.findAllDigests();
    }

    @GetMapping(path = "getModulesInArea/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن ماژول های افزوده شده به منطقه توسط منطقه")
    public ResponseEntity<List<ModuleInArea>> getModulesInArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return moduleServiceInArea.modules(getId(request), areaId);
    }

    @PutMapping(path = "addModule/{areaId}")
    @ResponseBody
    @Operation(
            summary = "افزودن ماژول/ماژول ها به یک منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void addModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody List<ObjectId> moduleIds
    ) {
        moduleServiceInArea.addModule(getId(request), areaId, moduleIds);
    }

    @DeleteMapping(path = "removeModule/{areaId}")
    @ResponseBody
    @Operation(
            summary = "حذف ماژول/ماژول ها از یک منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void removeModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody List<ObjectId> moduleIds
    ) {
        moduleServiceInArea.removeModule(getId(request), areaId, moduleIds);
    }


    @PutMapping(path = "addMembersToModule/{areaId}/{moduleIdInArea}")
    @ResponseBody
    @Operation(
            summary = "افزودن کاربر/کاربران به یک ماژول در منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void addMembersToModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleIdInArea,
            @RequestBody List<ObjectId> members
    ) {
        moduleServiceInArea.addMembersToModule(getId(request), areaId, moduleIdInArea, members);
    }

    @DeleteMapping(path = "removeMemberFromModule/{areaId}/{moduleIdInArea}/{userId}")
    @ResponseBody
    @Operation(
            summary = "حذف کاربر از ماژول در یک منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void removeMemberFromModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleIdInArea,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        moduleServiceInArea.removeMemberFromModule(getId(request), areaId, moduleIdInArea, userId);
    }


    @PutMapping(path = "addSecretariesToModule/{areaId}/{moduleIdInArea}")
    @ResponseBody
    @Operation(
            summary = "افزودن کاربر/کاربران به منشی های یک ماژول در منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void addSecretariesToModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleIdInArea,
            @RequestBody List<ObjectId> members
    ) {
        moduleServiceInArea.addSecretariesToModule(getId(request), areaId, moduleIdInArea, members);
    }

    @DeleteMapping(path = "removeMemberFromSecretaries/{areaId}/{moduleIdInArea}/{userId}")
    @ResponseBody
    @Operation(
            summary = "حذف کاربر از منشی های یک ماژول در یک منطقه توسط مسئول منطقه",
            description = "زمان شروع اردو نباید رسیده باشد"
    )
    public void removeMemberFromSecretaries(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleIdInArea,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        moduleServiceInArea.removeMemberFromSecretaries(getId(request), areaId, moduleIdInArea, userId);
    }
}
