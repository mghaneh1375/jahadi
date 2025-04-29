package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Service.EquipmentServiceInArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        return equipmentService.list(
                fullTokenInfo.getGroupId(),
                name, minAvailable, maxAvailable,
                healthyStatus, propertyId, location, equipmentType,
                rowNo, shelfNo, fromBuyAt, toBuyAt,
                fromGuaranteeExpireAt, toGuaranteeExpireAt
        );
    }
}
