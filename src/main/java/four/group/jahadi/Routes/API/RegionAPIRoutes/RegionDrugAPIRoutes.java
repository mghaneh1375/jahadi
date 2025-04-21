package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    public ResponseEntity<List<JoinedAreaDrugs>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return drugServiceInArea.list(getId(request), areaId);
    }

    @PutMapping(value = "returnAllDrugs/{areaId}")
    @ResponseBody
    @Operation(summary = "عودت تمام داروهای باقی مانده به انبار کل توسط مسئول منطقه یا مسئول داروخانه")
    public void returnAllDrugs(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        drugServiceInArea.returnAllDrugs(tokenInfo.getUserId(), tokenInfo.getUsername(), areaId);
    }

    @PutMapping(value = "returnDrug/{areaId}/{drugId}/{amount}")
    @ResponseBody
    @Operation(summary = "عودت تعدادی مشخص از دارو مدنظر به انبار کل توسط مسئول انبار دارو یا مسئول گروه")
    public void returnDrug(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @PathVariable @NotNull @Min(0) @Max(1000000) int amount
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        drugServiceInArea.returnDrug(
                drugId, amount,
                tokenInfo.getGroupId(), areaId,
                tokenInfo.getUsername(),
                tokenInfo.getAccesses().contains(Access.GROUP) ? null : tokenInfo.getUserId()
        );
    }
}
