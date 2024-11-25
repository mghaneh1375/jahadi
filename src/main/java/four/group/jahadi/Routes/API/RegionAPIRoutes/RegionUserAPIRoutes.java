package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Service.Area.ModuleServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/user")
@Validated
public class RegionUserAPIRoutes extends Router {

    @Autowired
    private ModuleServiceInArea moduleServiceInArea;

    @Autowired
    private AreaService areaService;

    @GetMapping(value = "modules/{areaId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن ماژول ها در یک منطقه خاص برای یک جهادگر",
            description = "در صورتی که کاربر به یک ماژول خاص دسترسی داشته باشد فیلد دسترسی های متناظر با آن پر میشود"
    )
    public ResponseEntity<List<ModuleInArea>> modules(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(value = "tabName", required = false) @Size(min = 3, max = 30) String tabName
    ) {
        return moduleServiceInArea.modulesForJahadgar(getId(request), areaId, tabName);
    }

    @GetMapping(value = "myCartableAreas")
    @ResponseBody
    @Operation(summary = "گرفتن لیستی از مناطقی که من جهادگر آنها هستم که هنوز تموم نشده اند")
    public ResponseEntity<List<Trip>> myCartableAreas(HttpServletRequest request) {
        return areaService.myCartableList(getId(request), false);
    }

    @GetMapping(value = "staticAccesses/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن دسترسی های من جهادگر به ماژول های استاتیک در یک منطقه خاص")
    public ResponseEntity<HashMap<String, Boolean>> staticAccesses(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return areaService.staticAccesses(getId(request), areaId);
    }

}
