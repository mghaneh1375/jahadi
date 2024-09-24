package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.User;
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
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        equipmentServiceInArea.returnAllEquipments(user.getId(), user.getPhone(), areaId);
    }

}
