package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
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
import java.util.Date;
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
