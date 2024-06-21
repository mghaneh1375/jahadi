package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.SubModule;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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

    @GetMapping(path = "getTabsInArea/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن تب های موجود در منطقه")
    public ResponseEntity<List<String>> getTabsInArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return moduleServiceInArea.tabs(getId(request), areaId);
    }

    @GetMapping(path = "getModulesInTab/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن ماژول های داخل یک تب در منطقه")
    public ResponseEntity<List<ModuleInArea>> getModulesInTab(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(value = "tabName") @NotEmpty @Size(min = 3, max = 30) String tabName
    ) {
        return moduleServiceInArea.getModulesInTab(getId(request), areaId, tabName);
    }

    @GetMapping(path = "getModuleInArea/{areaId}/{moduleId}")
    @ResponseBody
    @Operation(summary = "گرفتن ماژول در منطقه توسط منطقه یا دکتر آن ماژول")
    public ResponseEntity<Module> getModuleInArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId
    ) {
        return moduleServiceInArea.getModule(getId(request), areaId, moduleId);
    }

    @GetMapping(path = "getSubModule/{areaId}/{moduleId}/{subModuleId}")
    @ResponseBody
    @Operation(summary = "گرفتن ساب ماژول در منطقه توسط منطقه یا دکتر آن ماژول")
    public ResponseEntity<SubModule> getSubModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId subModuleId
    ) {
        return moduleServiceInArea.getSubModule(getId(request), areaId, moduleId, subModuleId);
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
