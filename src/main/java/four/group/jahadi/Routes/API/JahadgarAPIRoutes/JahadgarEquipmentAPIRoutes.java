package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AreaEquipmentsData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Service.EquipmentServiceInArea;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/api/jahadgar/equipment")
@Validated
@RequiredArgsConstructor
public class JahadgarEquipmentAPIRoutes extends Router {
    private final EquipmentServiceInArea equipmentServiceInArea;
    private final EquipmentService equipmentService;

    @PutMapping(value = "addAllEquipmentsToArea/{areaId}")
    @Operation(summary = "افزودن یک یا چند تجهیز به منطقه توسط مسئول گروه")
    public void addAllEquipmentsToArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid ValidList<AreaEquipmentsData> dataValidList
    ) {
        TokenInfo tokenInfo = getFullTokenInfo(request);
        equipmentServiceInArea.addAllEquipmentsToArea(
                tokenInfo.getUserId(), tokenInfo.getGroupId(),
                tokenInfo.getUsername(), areaId, dataValidList,
                tokenInfo.getAccesses().contains(Access.GROUP)
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
    public ResponseEntity<Page<Equipment>> list(
            HttpServletRequest request,
            @RequestParam(name = "pageIndex") @NotNull @Min(0) @Max(10000) Integer pageIndex,
            @RequestParam(name = "pageSize") @NotNull @Min(5) @Max(100) Integer pageSize,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "minAvailable") Integer minAvailable,
            @RequestParam(required = false, value = "maxAvailable") Integer maxAvailable,
            @RequestParam(required = false, value = "healthyStatus") String healthyStatus,
            @RequestParam(required = false, value = "propertyId") String propertyId,
            @RequestParam(required = false, value = "location") String location,
            @RequestParam(required = false, value = "equipmentType") String equipmentType,
            @RequestParam(required = false, value = "rowNo") String rowNo,
            @RequestParam(required = false, value = "shelfNo") String shelfNo,
            @RequestParam(required = false, value = "fromBuyAt") LocalDateTime fromBuyAt,
            @RequestParam(required = false, value = "toBuyAt") LocalDateTime toBuyAt,
            @RequestParam(required = false, value = "fromGuaranteeExpireAt") LocalDateTime fromGuaranteeExpireAt,
            @RequestParam(required = false, value = "toGuaranteeExpireAt") LocalDateTime toGuaranteeExpireAt
    ) throws UnAuthException, NotActivateAccountException {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        if(!fullTokenInfo.getAccesses().contains(Access.GROUP)) {
            ResponseEntity<Boolean> hasAccess = equipmentServiceInArea.checkAccessToWareHouse(
                    fullTokenInfo.getGroupId(),
                    fullTokenInfo.getUserId()
            );
            if(hasAccess == null || hasAccess.getBody() == null ||
                    !hasAccess.getBody()
            )
                throw new NotAccessException();
        }
        return equipmentService.paginateList(
                pageIndex, pageSize,
                fullTokenInfo.getGroupId(),
                name, minAvailable, maxAvailable,
                healthyStatus, propertyId, location, equipmentType,
                rowNo, shelfNo, fromBuyAt, toBuyAt,
                fromGuaranteeExpireAt, toGuaranteeExpireAt
        );
    }
}
