package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.ModuleServiceInArea;
import four.group.jahadi.Service.Area.ReportServiceInArea;
import four.group.jahadi.Service.ModuleService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/region/module")
@Validated
public class RegionModuleAPIRoutes extends Router {
    @Autowired
    private ModuleServiceInArea moduleServiceInArea;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ReportServiceInArea reportServiceInArea;


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
    public ResponseEntity<Map<String, String>> getTabsInArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return moduleServiceInArea.tabs(
                fullTokenInfo.getUserId(), areaId,
                fullTokenInfo.getAccesses().contains(Access.GROUP) ? fullTokenInfo.getGroupId() : null
        );
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
    @Operation(summary = "گرفتن ماژول در منطقه توسط منطقه")
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
            @PathVariable @ObjectIdConstraint ObjectId subModuleId,
            @RequestParam(required = false, name = "patientId") ObjectId patientId
    ) {
        return moduleServiceInArea.getSubModule(
                getId(request), areaId, moduleId,
                subModuleId, patientId
        );
    }

    @GetMapping(path = "getModuleReport/{areaId}/{moduleId}")
    public void getModuleReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId
    ) {
        reportServiceInArea.moduleReport(getId(request), areaId, moduleId, response);
    }

    @GetMapping(path = "getAreaReport/{areaId}")
    public void getModuleReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        reportServiceInArea.getAreaReport(getId(request), areaId, response);
    }
}
