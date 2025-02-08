package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.EquipmentService;
import four.group.jahadi.Service.EquipmentServiceInArea;
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
import java.util.List;

@RestController
@RequestMapping(value = "api/equipment")
@Validated
public class EquipmentAPIRoutes extends Router {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Equipment> store(
            HttpServletRequest request,
            @RequestBody @Valid EquipmentData dto
    ) throws UnAuthException, NotActivateAccountException {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return equipmentService.store(dto,
                fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @PostMapping(value = "batchStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "افزودن دسته ای به وسیله فایل اکسل")
    public ResponseEntity<List<ErrorRow>> batchStore(
            HttpServletRequest request,
            @RequestBody MultipartFile file
    ) throws UnAuthException, NotActivateAccountException {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return equipmentService.batchStore(file,
                fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @PutMapping(value = "update/{id}")
    public void update(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id,
            @RequestBody @Valid EquipmentData dto
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        equipmentService.update(
                id, dto,
                fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @DeleteMapping(value = "remove/{id}")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        equipmentService.remove(
                id, fullTokenInfo.getUserId(),
                fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

}
