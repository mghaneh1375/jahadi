package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Area.ExperimentInArea;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.ExperimentServiceInArea;
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
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/experiment")
@Validated
public class RegionExperimentAPIRoutes extends Router {

    @Autowired
    private ExperimentServiceInArea experimentServiceInArea;

    @GetMapping(value = "list/{areaId}")
    @ResponseBody
    @Operation(summary = "آزمایش های موجود در منطقه")
    public ResponseEntity<List<ExperimentInArea>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId) {
        return experimentServiceInArea.list(
                getId(request), areaId
        );
    }

    @PutMapping(value = "addExperimentToRegion/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن یک یا چند آزمایش به منطقه")
    public void store(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> experiments
            ) {
        experimentServiceInArea.addAllToExperimentsList(
                getId(request), areaId, experiments
        );
    }

    @DeleteMapping(value = "removeExperimentFromRegion/{areaId}")
    @ResponseBody
    @Operation(summary = "حذف یک یا چند آزمایش از منطقه")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> experiments
    ) {
        experimentServiceInArea.removeAllFromExperimentsList(
                getId(request), areaId, experiments
        );
    }
}
