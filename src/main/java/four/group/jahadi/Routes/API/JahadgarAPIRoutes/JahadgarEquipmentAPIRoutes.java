package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AreaEquipmentsData;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Service.EquipmentServiceInArea;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/jahadgar/equipment")
@Validated
public class JahadgarEquipmentAPIRoutes extends Router {
    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;
    @Autowired
    private EquipmentService equipmentService;

    @PutMapping(value = "addAllEquipmentsToArea/{areaId}")
    @Operation(summary = "افزودن یک یا چند تجهیز به منطقه توسط مسئول گروه")
    public void addAllEquipmentsToArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid ValidList<AreaEquipmentsData> dataValidList
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        equipmentServiceInArea.addAllEquipmentsToArea(
                tokenInfo.getUserId(), tokenInfo.getGroupId(),
                tokenInfo.getUsername(), areaId, dataValidList,
                false
        );
    }

    @DeleteMapping(value = "removeAllFromEquipmentsList/{areaId}")
    @Operation(summary = "حذف یک یا چند تجهیز از منطقه توسط مسئول گروه")
    public void removeAllFromEquipmentsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        equipmentServiceInArea.removeAllFromEquipmentsList(
                tokenInfo.getUserId(), tokenInfo.getGroupId(), tokenInfo.getUsername(),
                areaId, drugs, false
        );
    }
    @GetMapping(value = "checkAccessToWareHouse")
    @ResponseBody
    public ResponseEntity<Boolean> checkAccessToWareHouse(
            HttpServletRequest request
    ) {
        TokenInfo groupAndId = getTokenInfo(request);
        return equipmentServiceInArea.checkAccessToWareHouse(
                groupAndId.getGroupId(),
                groupAndId.getUserId()
        );
    }

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Equipment>> list(
            HttpServletRequest request,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "minAvailable") Integer minAvailable,
            @RequestParam(required = false, value = "maxAvailable") Integer maxAvailable,
            @RequestParam(required = false, value = "healthyStatus") String healthyStatus,
            @RequestParam(required = false, value = "propertyId") String propertyId,
            @RequestParam(required = false, value = "location") String location,
            @RequestParam(required = false, value = "equipmentType") String equipmentType,
            @RequestParam(required = false, value = "rowNo") String rowNo,
            @RequestParam(required = false, value = "shelfNo") String shelfNo,
            @RequestParam(required = false, value = "fromBuyAt") Date fromBuyAt,
            @RequestParam(required = false, value = "toBuyAt") Date toBuyAt,
            @RequestParam(required = false, value = "fromGuaranteeExpireAt") Date fromGuaranteeExpireAt,
            @RequestParam(required = false, value = "toGuaranteeExpireAt") Date toGuaranteeExpireAt
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return equipmentService.list(
                user.getId(), name, minAvailable, maxAvailable,
                healthyStatus, propertyId, location, equipmentType,
                rowNo, shelfNo, fromBuyAt, toBuyAt,
                fromGuaranteeExpireAt, toGuaranteeExpireAt
        );
    }
}
