package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/equipment")
@Validated
public class RegionEquipmentAPIRoutes extends Router {

    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;

    @GetMapping(value = "list/{areaId}")
    @ResponseBody
    @Operation(summary = "تجهیزات موجود در منطقه")
    public ResponseEntity<List<AreaEquipments>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return equipmentServiceInArea.list(getId(request), areaId);
    }

    @PutMapping(value = "returnAllEquipments/{areaId}")
    @ResponseBody
    @Operation(summary = "عودت تمام تجهیزات باقی مانده به انبار کل توسط مسئول منطقه یا مسئول تجهیزات")
    public void returnAllEquipments(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        equipmentServiceInArea.returnAllEquipments(tokenInfo.getUserId(), tokenInfo.getUsername(), areaId);
    }
    @PostMapping(value = "countDownEquipment/{areaId}/{equipmentId}")
    @ResponseBody
    @Operation(summary = "کم کردن تعداد یک تجهیز در منطقه", description = "این عملیات غیرقابل بازگشت است و از فراخوانی آن اطمینان داشته باشید")
    public void countDownEquipment(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId equipmentId,
            @RequestParam(value = "count") @Min(0) Integer count
    ) {
        equipmentServiceInArea.countDownEquipment(
                getId(request), areaId, equipmentId, count
        );
    }

}
