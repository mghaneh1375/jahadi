package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AdviceDrugData;
import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.DTO.Area.GiveDrugData;
import four.group.jahadi.DTO.DrugBookmarkData;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Models.PatientDrug;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
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
    private JahadgarDrugService drugService;
    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @PutMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "منتخب کردن یک دارو و طریقه مصرف آن توسط دکتر")
    public void bookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody @Valid DrugBookmarkData drugBookmarkData
    ) {
        drugService.store(drugBookmarkData, getId(request), drugId);
    }

    @DeleteMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "حذف کردن یک دارو از لیست منتخب های دکتر")
    public void removeFromBookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId
    ) {
        drugService.remove(getId(request), drugId);
    }

    @GetMapping(value = "bookmarks")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای منتخب دکتر")
    public ResponseEntity<List<DrugBookmark>> bookmarks(
            HttpServletRequest request
    ) {
        return drugService.list(getId(request));
    }

    @PutMapping(value = "addAllToDrugsList/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن یک یا چند دارو به منطقه توسط مسئول گروه")
    public void addAllToDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<AreaDrugsData> drugsData
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        drugServiceInArea.addAllToDrugsList(
                tokenInfo.getUserId(), tokenInfo.getGroupId(), tokenInfo.getUsername(),
                areaId, drugsData, false
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
    public void removeAdvice(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId adviceId
    ) {
        drugServiceInArea.removeAdvice(getId(request), adviceId);
    }

    @GetMapping(value = "listOfAdvices/{areaId}/{patientId}")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای تجویز شده توسط دکتر")
    public ResponseEntity<List<PatientDrug>> listOfAdvices(
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
    public ResponseEntity<List<PatientDrug>> listOfAdvices(
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
    public void giveDrugToPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId adviceId,
            @RequestBody @Valid GiveDrugData data
    ) {
        drugServiceInArea.giveDrug(getId(request), areaId, adviceId, data);
    }


    @DeleteMapping(value = "removeAllFromDrugsList/{areaId}")
    @Operation(summary = "حذف یک یا چند دارو از منطقه توسط مسئول گروه")
    public void removeAllFromDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) {
        TokenInfo tokenInfo = getTokenInfo(request);
        drugServiceInArea.removeAllFromDrugsList(
                tokenInfo.getUserId(), tokenInfo.getGroupId(),
                tokenInfo.getUsername(), areaId, drugs, false
        );
    }

    @GetMapping(value = "checkAccessToWareHouse")
    @ResponseBody
    public ResponseEntity<Boolean> checkAccessToWareHouse(
            HttpServletRequest request
    ) {
        TokenInfo groupAndId = getTokenInfo(request);
        return drugService.checkAccessToWareHouse(
                groupAndId.getGroupId(),
                groupAndId.getUserId()
        );
    }

    @GetMapping(value = "getAdviceDetail/{adviceId}")
    @ResponseBody
    public ResponseEntity<PatientDrug> getAdviceDetail(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId adviceId
    ) {
        return drugServiceInArea.getAdviceDetail(getId(request), adviceId);
    }
}
