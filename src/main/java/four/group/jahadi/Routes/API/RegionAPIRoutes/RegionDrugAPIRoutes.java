package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
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
    public ResponseEntity<Page<JoinedAreaDrugs>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(value = "pageIndex") @Min(0) Integer pageIndex,
            @RequestParam(value = "pageSize") @Min(10) Integer pageSize
    ) {
        return drugServiceInArea.list(getId(request), areaId, pageIndex, pageSize);
    }

    @GetMapping(value = "search/{areaId}")
    @ResponseBody
    @Operation(summary = "جست و جو در داروهای موجود در منطقه")
    public ResponseEntity<List<JoinedAreaDrugs>> search(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(value = "key") @NotBlank @Size(min = 2) String key
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return drugServiceInArea.search(fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(), areaId, key);
    }

    @PutMapping(value = "returnAllDrugs/{areaId}")
    @ResponseBody
    @Operation(summary = "عودت تمام داروهای باقی مانده به انبار کل توسط مسئول منطقه یا مسئول داروخانه")
    public void returnAllDrugs(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        drugServiceInArea.returnAllDrugs(
                tokenInfo.getUserId(), tokenInfo.getUsername(),
                areaId, tokenInfo.getGroupId()
        );
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
        TokenInfo tokenInfo = getFullTokenInfo(request);
        drugServiceInArea.returnDrug(
                drugId, amount,
                tokenInfo.getGroupId(), areaId,
                tokenInfo.getUsername(),
                tokenInfo.getAccesses().contains(Access.GROUP) ? null : tokenInfo.getUserId()
        );
    }
}
