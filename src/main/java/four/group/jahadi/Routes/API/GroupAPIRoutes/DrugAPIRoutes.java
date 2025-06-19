package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Utility.Utility;
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
@RequestMapping(path = "/api/drug")
@Validated
public class DrugAPIRoutes extends Router {
    @Autowired
    private DrugService drugService;

    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @GetMapping(value = "getDrugTypes")
    @ResponseBody
    @Operation(summary = "گرفتن لیست enum نوع دارو")
    public ResponseEntity<DrugType[]> getDrugTypes() {
        return drugService.getDrugTypes();
    }

    @GetMapping(value = "list/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن داروهای موجود در منطقه توسط مسئول انبار داروخانه و یا مسئول گروه")
    public ResponseEntity<List<JoinedAreaDrugs>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "drugType") String drugType,
            @RequestParam(required = false, name = "fromExpireAt") String fromExpireAt,
            @RequestParam(required = false, name = "toExpireAt") String toExpireAt
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return drugServiceInArea.list(
                fullTokenInfo.getGroupId(), areaId,
                fullTokenInfo.getAccesses().contains(Access.GROUP) ? null : fullTokenInfo.getUserId(),
                name, drugType,
                fromExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(fromExpireAt)),
                toExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(toExpireAt))
        );
    }
}
