package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AdviceDrugData;
import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.DTO.Area.GiveDrugData;
import four.group.jahadi.DTO.DrugBookmarkData;
import four.group.jahadi.DTO.Patient.PatientAdvices;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Models.PatientDrug;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Service.JahadgarDrugService;
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
@RequestMapping(path = "/api/jahadgar/drug")
@Validated
public class JahadgarDrugAPIRoutes extends Router {

    @Autowired
    private JahadgarDrugService jahadgarDrugService;
    @Autowired
    private DrugServiceInArea drugServiceInArea;
    @Autowired
    private DrugService drugService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "منتخب کردن یک دارو و طریقه مصرف آن توسط دکتر")
    public void bookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody @Valid DrugBookmarkData drugBookmarkData
    ) {
        jahadgarDrugService.store(drugBookmarkData, getId(request), drugId);
    }

    @DeleteMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "حذف کردن یک دارو از لیست منتخب های دکتر")
    public void removeFromBookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId
    ) {
        jahadgarDrugService.remove(getId(request), drugId);
    }

    @GetMapping(value = "bookmarks")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای منتخب دکتر")
    public ResponseEntity<List<DrugBookmark>> bookmarks(
            HttpServletRequest request
    ) {
        return jahadgarDrugService.list(getId(request));
    }

    @PutMapping(value = "addAllToDrugsList/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن یک یا چند دارو به منطقه توسط مسئول انبار دارو گروه یا مسئول گروه")
    public void addAllToDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<AreaDrugsData> drugsData
    ) {
        TokenInfo tokenInfo = getFullTokenInfo(request);
        drugServiceInArea.addAllToDrugsList(
                tokenInfo.getUserId(), tokenInfo.getGroupId(), tokenInfo.getUsername(),
                areaId, drugsData, tokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @PostMapping(value = "advice/{patientId}/{moduleId}/{areaDrugId}")
    @ResponseBody
    @Operation(summary = "تجویز دارو توسط دکتر در منطقه")
    public void advice(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId areaDrugId,
            @RequestBody @Valid AdviceDrugData data
    ) {
        drugServiceInArea.advice(
                getId(request), patientId,
                moduleId, areaDrugId, data
        );
    }

    @DeleteMapping(value = "removeAdvice/{adviceId}")
    @Operation(summary = "حذف تجویز انجام شده فقط توسط خود همان دکتری که تجویز را انجام داده است")
    public void removeAdvice(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId adviceId
    ) {
        drugServiceInArea.removeAdvice(getId(request), adviceId);
    }

    @GetMapping(value = "listOfAdvices/{areaId}/{patientId}")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای تجویز شده توسط دکتر")
    public ResponseEntity<List<PatientAdvices>> listOfAdvices(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestParam(value = "moduleId", required = false) ObjectId moduleId,
            @RequestParam(value = "deliveryStatus", required = false) DeliveryStatus deliveryStatus
    ) {
        return drugServiceInArea.listOfAdvices(
                getId(request), areaId, patientId,
                moduleId, null, deliveryStatus,
                null, null, null, null,
                null, null, null, null,
                null
        );
    }

    @GetMapping(value = "listOfAdvices/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای تجویز شده توسط مسئول داروخانه")
    public ResponseEntity<List<PatientAdvices>> listOfAdvices(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(value = "patientId", required = false) ObjectId patientId,
            @RequestParam(value = "moduleId", required = false) ObjectId moduleId,
            @RequestParam(value = "deliveryStatus", required = false) DeliveryStatus deliveryStatus,
            @RequestParam(value = "doctorId", required = false) ObjectId doctorId,
            @RequestParam(value = "drugId", required = false) ObjectId drugId,
            @RequestParam(value = "startAdviceAt", required = false) Date startAdviceAt,
            @RequestParam(value = "endAdviceAt", required = false) Date endAdviceAt,
            @RequestParam(value = "startGiveAt", required = false) Date startGiveAt,
            @RequestParam(value = "endGiveAt", required = false) Date endGiveAt,
            @RequestParam(value = "startSuggestCount", required = false) Integer startSuggestCount,
            @RequestParam(value = "endSuggestCount", required = false) Integer endSuggestCount,
            @RequestParam(value = "giverId", required = false) ObjectId giverId,
            @RequestParam(value = "pageNo") Integer pageNo
    ) {
        return drugServiceInArea.listOfAdvices(
                getId(request), areaId, patientId,
                moduleId, doctorId, deliveryStatus,
                drugId, startAdviceAt, endAdviceAt, startGiveAt,
                endGiveAt, startSuggestCount, endSuggestCount, giverId,
                pageNo
        );
    }
    @PutMapping(value = "giveDrugToPatient/{areaId}/{adviceId}")
    @Operation(summary = "تحویل دارو تجویز شده به کاربر", description = "فیلد drugId اختیاری است و درصورتی که مسئول تحویل بخواهد دارویی به غیر از دارو تجویز شده به بیمار تحویل دهد آن را پر می کند. توضیح هم اختیاری است")
    public void giveDrugToPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId adviceId,
            @RequestBody @Valid GiveDrugData data
    ) {
        drugServiceInArea.giveDrug(getId(request), areaId, adviceId, data);
    }


    @DeleteMapping(value = "removeAllFromDrugsList/{areaId}")
    @Operation(summary = "حذف یک یا چند دارو از منطقه توسط مسئول گروه یا مسئول انبار دارو")
    public void removeAllFromDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) {
        TokenInfo tokenInfo = getFullTokenInfo(request);
        drugServiceInArea.removeAllFromDrugsList(
                tokenInfo.getUserId(), tokenInfo.getGroupId(),
                tokenInfo.getUsername(), areaId, drugs, tokenInfo.getAccesses().contains(Access.GROUP)
        );
    }

    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروها توسط مسئول گروه یا مسئول انبار")
    public ResponseEntity<List<Drug>> list(
            HttpServletRequest request,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "minAvailableCount") Integer minAvailableCount,
            @RequestParam(required = false, name = "maxAvailableCount") Integer maxAvailableCount,
            @RequestParam(required = false, name = "drugLocation") String drugLocation,
            @RequestParam(required = false, name = "drugType") String drugType,
            @RequestParam(required = false, name = "fromExpireAt") Date fromExpireAt,
            @RequestParam(required = false, name = "toExpireAt") Date toExpireAt,
            @RequestParam(required = false, name = "boxNo") String boxNo,
            @RequestParam(required = false, name = "shelfNo") String shelfNo
    ) throws UnAuthException, NotActivateAccountException {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        if(!fullTokenInfo.getAccesses().contains(Access.GROUP)) {
            ResponseEntity<Boolean> hasAccess = jahadgarDrugService.checkAccessToWareHouse(
                    fullTokenInfo.getGroupId(),
                    fullTokenInfo.getUserId()
            );
            if(hasAccess == null || hasAccess.getBody() == null ||
                    !hasAccess.getBody()
            )
                throw new NotAccessException();
        }
        return drugService.list(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? fullTokenInfo.getUserId()
                        : userRepository.findIdByGroupOwnerId(fullTokenInfo.getGroupId()).getId(),
                name, minAvailableCount, maxAvailableCount,
                drugLocation, drugType, fromExpireAt, toExpireAt,
                boxNo, shelfNo
        );
    }

    @GetMapping(value = "checkAccessToWareHouse")
    @ResponseBody
    @Operation(summary = "چک کردن داشتن دسترسی به انبار گروه برای یک کاربر خاص")
    public ResponseEntity<Boolean> checkAccessToWareHouse(
            HttpServletRequest request
    ) {
        TokenInfo groupAndId = getTokenInfo(request);
        return jahadgarDrugService.checkAccessToWareHouse(
                groupAndId.getGroupId(),
                groupAndId.getUserId()
        );
    }

    @GetMapping(value = "getAdviceDetail/{adviceId}")
    @ResponseBody
    @Operation(summary = "گرفتن جزئیات یک تجویز", description = "فیلدهایی نظیر توضیح تجویز و یا توضیح تحویل دارو و همچنین نام دکتر تجویز کننده و شخص تحویل دهنده در این سرویس برمیگردد")
    public ResponseEntity<PatientDrug> getAdviceDetail(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId adviceId
    ) {
        return drugServiceInArea.getAdviceDetail(getId(request), adviceId);
    }
}
