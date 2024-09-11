package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/group/equipment")
@Validated
public class EquipmentAPIRoutes extends Router {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Equipment>> list(
            HttpServletRequest request
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return equipmentService.list(user.getId(), null, null, null, null);
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

    @PostMapping(value = "batchStore")
    @ResponseBody
    public ResponseEntity<List<EquipmentService.ErrorRow>> batchStore(
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