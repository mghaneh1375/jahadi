package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Service.DrugService;
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
@RequestMapping(path = "/api/group/drug")
@Validated
public class DrugAPIRoutes extends Router {
    @Autowired
    private DrugService drugService;

    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @DeleteMapping(value = "remove/{id}")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        drugService.remove(id, getId(request));
    }

    @GetMapping(value = "getDrugTypes")
    @ResponseBody
    @Operation(summary = "گرفتن لیست enum نوع دارو")
    public ResponseEntity<DrugType[]> getDrugTypes() {
        return drugService.getDrugTypes();
    }

    //    @PutMapping(value = "updateDrugReminder/{areaId}/{id}/{newReminder}")
//    @ResponseBody
//    @Operation(summary = "آپدیت کردن تعداد باقی مانده از یک داروی خاص", description = "باید آی دی دارو در منطقه داده شود نه آی دی خود دارو")
//    public void updateDrugReminder(
//            HttpServletRequest request,
//            @PathVariable @ObjectIdConstraint ObjectId areaId,
//            @PathVariable @ObjectIdConstraint ObjectId id,
//            @PathVariable @Min(0) @Max(10000) Integer newReminder
//    ) {
//        drugServiceInArea.updateDrugReminder(getId(request), areaId, id, newReminder);
//    }

    @PostMapping(value = "store")
    @ResponseBody
    @Operation(summary = "افزودن تکی دارو توسط مسئول گروه")
    public ResponseEntity<Drug> store(
            HttpServletRequest request,
            @RequestBody @Valid DrugData dto
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return drugService.store(dto, user.getId(), user.getGroupId());
    }

    @PutMapping(value = "update/{drugId}")
    @Operation(summary = "ویرایش دارو توسط مسئول گروه")
    public void update(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody @Valid DrugData dto
    ) {
        drugService.update(drugId, dto, getId(request));
    }

    @PostMapping(value = "batchStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "افزودن دسته ای به وسیله فایل اکسل")
    public ResponseEntity<List<ErrorRow>> batchStore(
            HttpServletRequest request,
            @RequestBody MultipartFile file
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return drugService.batchStore(file, user.getId(), user.getGroupId());
    }
}
