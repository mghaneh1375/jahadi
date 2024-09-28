package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Area.AreaDrugs;
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

}
