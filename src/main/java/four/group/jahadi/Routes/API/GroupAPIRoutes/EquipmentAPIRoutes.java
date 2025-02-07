package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.Area.AreaEquipmentsData;
import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Service.EquipmentServiceInArea;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(value = "api/group/equipment")
@Validated
public class EquipmentAPIRoutes extends Router {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;

    @PutMapping(value = "addAllEquipmentsToArea/{areaId}")
    @Operation(summary = "افزودن یک یا چند تجهیز به منطقه توسط مسئول گروه")
    public void addAllEquipmentsToArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid ValidList<AreaEquipmentsData> dataValidList
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        equipmentServiceInArea.addAllEquipmentsToArea(
                user.getId(), user.getGroupId(),
                user.getPhone(), areaId, dataValidList,
                true
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
        equipmentServiceInArea.removeAllFromEquipmentsList(
                user.getId(), user.getGroupId(), user.getPhone(), areaId, drugs, true
        );
    }

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Equipment> store(
            HttpServletRequest request,
            @RequestBody @Valid EquipmentData dto
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return equipmentService.store(dto, user.getId(), user.getGroupId());
    }

    @PostMapping(value = "batchStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "افزودن دسته ای به وسیله فایل اکسل")
    public ResponseEntity<List<ErrorRow>> batchStore(
            HttpServletRequest request,
            @RequestBody MultipartFile file
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return equipmentService.batchStore(file, user.getId(), user.getGroupId());
    }

    @PutMapping(value = "update/{id}")
    public void update(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id,
            @RequestBody @Valid EquipmentData dto
    ) {
        equipmentService.update(id, dto, getId(request));
    }

    @DeleteMapping(value = "remove/{id}")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        equipmentService.remove(id, getId(request));
    }

}
