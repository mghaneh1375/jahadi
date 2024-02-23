package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
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
@RequestMapping(value = "/api/region/drug")
@Validated
public class RegionDrugAPIRoutes extends Router {

    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @GetMapping(value = "list/{areaId}")
    @ResponseBody
    @Operation(summary = "داروهای موجود در منطقه")
    public ResponseEntity<List<AreaDrugs>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId) {
        return drugServiceInArea.list(getId(request), areaId);
    }

    @PutMapping(value = "addAllToDrugsList/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن یک یا چند دارو به منطقه")
    public void addAllToDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<AreaDrugsData> drugsData
    ) {
        drugServiceInArea.addAllToDrugsList(
                getId(request), areaId, drugsData
        );
    }

    @DeleteMapping(value = "removeAllFromDrugsList/{areaId}")
    @ResponseBody
    @Operation(summary = "حذف یک یا چند دارو از منطقه")
    public void removeAllFromDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) {
        drugServiceInArea.removeAllFromDrugsList(
                getId(request), areaId, drugs
        );
    }

    @PutMapping(value = "updateDrugReminder/{areaId}/{id}/{newReminder}")
    @ResponseBody
    @Operation(summary = "آپدیت کردن تعداد باقی مانده از یک داروی خاص", description = "باید آی دی دارو در منطقه داده شود نه آی دی خود دارو")
    public void updateDrugReminder(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId id,
            @PathVariable @Min(0) @Max(10000) Integer newReminder
    ) {
        drugServiceInArea.updateDrugReminder(getId(request), areaId, id, newReminder);
    }
}
