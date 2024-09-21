package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AreaEquipmentsData;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;

public class JahadgarEquipmentAPIRoutes extends Router {

    @Autowired
    private EquipmentService equipmentService;

    @PutMapping(value = "addAllEquipmentsToArea/{areaId}")
    @Operation(summary = "افزودن یک یا چند تجهیز به منطقه توسط مسئول گروه")
    public void addAllEquipmentsToArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid ValidList<AreaEquipmentsData> dataValidList
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        equipmentService.addAllEquipmentsToArea(
                user.getId(), user.getGroupId(),
                user.getPhone(), areaId, dataValidList,
                false
        );
    }

    @DeleteMapping(value = "removeAllFromEquipmentsList/{areaId}")
    @Operation(summary = "حذف یک یا چند تجهیز از منطقه توسط مسئول گروه")
    public void removeAllFromEquipmentsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        equipmentService.removeAllFromEquipmentsList(
                user.getId(), user.getGroupId(), user.getPhone(),
                areaId, drugs, false
        );
    }
}
