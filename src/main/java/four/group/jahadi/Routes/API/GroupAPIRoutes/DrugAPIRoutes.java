package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.DTO.ObjectIdList;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Service.JahadgarDrugService;
import four.group.jahadi.Utility.Utility;
import four.group.jahadi.Validator.ObjectIdConstraint;
import four.group.jahadi.Validator.ValidatedUpdatePresenceList;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/api/drug")
@Validated
public class DrugAPIRoutes extends Router {
    @Autowired
    private DrugService drugService;

    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @Autowired
    private JahadgarDrugService jahadgarDrugService;

    @DeleteMapping(value = "remove/{id}")
    public void remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        drugService.remove(
                id, fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

//    @DeleteMapping(value = "removeAll")
//    public void removeAll(
//            HttpServletRequest request,
//            @RequestBody @Valid ObjectIdList list
//    ) {
//        TokenInfo fullTokenInfo = getFullTokenInfo(request);
//        drugService.remove(
//                id, fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
//                fullTokenInfo.getAccesses().contains(Access.GROUP)
//        );
//    }

    @DeleteMapping(value = "archive/{id}")
    public void archive(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        drugService.archive(
                id, fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @DeleteMapping(value = "archiveAll")
    public void archive(
            HttpServletRequest request,
            @RequestBody @Valid ObjectIdList list
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        drugService.archiveAll(
                list.getIds(), fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
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
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return drugService.store(
                dto, fullTokenInfo.getUserId(), fullTokenInfo.getGroupId(),
                fullTokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @PutMapping(value = "update/{drugId}")
    @Operation(summary = "ویرایش دارو توسط مسئول گروه")
    public void update(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody @Valid DrugData dto
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        drugService.update(drugId, dto,
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
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        return drugService.batchStore(file, tokenInfo.getUserId(), tokenInfo.getGroupId());
    }

    @GetMapping(value = "list/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن داروهای موجود در منطقه توسط مسئول انبار داروخانه و یا مسئول گروه")
    public ResponseEntity<List<JoinedAreaDrugs>> list(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "drugType") String drugType,
            @RequestParam(required = false, name = "fromExpireAt") String fromExpireAt,
            @RequestParam(required = false, name = "toExpireAt") String toExpireAt
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return drugServiceInArea.list(
                fullTokenInfo.getGroupId(), areaId,
                fullTokenInfo.getAccesses().contains(Access.GROUP) ? null : fullTokenInfo.getUserId(),
                name, drugType,
                fromExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(fromExpireAt)),
                toExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(toExpireAt))
        );
    }

    @GetMapping(value = "logReport")
    @Operation(summary = "گرفتن خروجی اکسل از لاگ های پر و خالی شدن انبار دارو")
    public void logReport(
            HttpServletRequest request,
            @RequestParam(name = "drugId", required = false) ObjectId drugId,
            @RequestParam(name = "groupId", required = false) ObjectId groupId,
            @RequestParam(name = "userId", required = false) ObjectId userId,
            @RequestParam(name = "areaId", required = false) ObjectId areaId,
            @RequestParam(required = false, name = "fromExpireAt") String fromExpireAt,
            @RequestParam(required = false, name = "toExpireAt") String toExpireAt,
            HttpServletResponse response
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        if(
                !fullTokenInfo.getAccesses().contains(Access.GROUP) &&
                        !fullTokenInfo.getAccesses().contains(Access.ADMIN)
        ) {
            ResponseEntity<Boolean> hasAccess = jahadgarDrugService.checkAccessToWareHouse(
                    fullTokenInfo.getGroupId(),
                    fullTokenInfo.getUserId()
            );
            if(hasAccess == null || hasAccess.getBody() == null ||
                    !hasAccess.getBody()
            )
                throw new NotAccessException();
        }

        drugService.logReport(
                drugId,
                fullTokenInfo.getAccesses().contains(Access.ADMIN)
                        ? groupId
                        : fullTokenInfo.getGroupId(),
                userId, areaId,
                fromExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(fromExpireAt)),
                toExpireAt == null ? null : Utility.getLastLocalDateTime(Utility.convertJalaliToGregorianDate(toExpireAt)),
                response
        );
    }

    @GetMapping(value = "report")
    public void report(
            HttpServletRequest request,
            @RequestParam(name = "groupId", required = false) ObjectId groupId,
            HttpServletResponse response
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        if(
                !fullTokenInfo.getAccesses().contains(Access.GROUP) &&
                        !fullTokenInfo.getAccesses().contains(Access.ADMIN)
        ) {
            ResponseEntity<Boolean> hasAccess = jahadgarDrugService.checkAccessToWareHouse(
                    fullTokenInfo.getGroupId(),
                    fullTokenInfo.getUserId()
            );
            if(hasAccess == null || hasAccess.getBody() == null ||
                    !hasAccess.getBody()
            )
                throw new NotAccessException();
        }

        drugService.report(
                fullTokenInfo.getAccesses().contains(Access.ADMIN)
                        ? groupId
                        : fullTokenInfo.getGroupId(),
                response
        );
    }

    @GetMapping("removeRedundants")
    @ResponseStatus(HttpStatus.OK)
    public void removeRedundants() {
        drugService.removeRedundants();
    }
}
