package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Models.Area.PatientJoinArea;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.PatientServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/patient")
@Validated
public class RegionPatientAPIRoutes extends Router {

    @Autowired
    private PatientServiceInArea patientServiceInArea;

    @PutMapping(value = "createPatientAndAddToRegion/{areaId}")
    @ResponseBody
    @Operation(
            summary = "افزودن بیمار جدید و افزودن آن به لیست پذیرش در یک منطقه توسط فرد مسئول پذیرش"
    )
    public void createPatientAndAddToRegion(HttpServletRequest request,
                                            @PathVariable @ObjectIdConstraint ObjectId areaId,
                                            @RequestBody @Valid PatientData patientData
    ) {
        patientServiceInArea.createPatientAndAddToRegion(getId(request), areaId, patientData);
    }

    @PostMapping(value = "inquiryPatient/{areaId}")
    @ResponseBody
    @Operation(
            summary = "استعلام بیمار توسط مسئول پذیرش در یک منطقه"
    )
    public ResponseEntity<Patient> inquiryPatient(HttpServletRequest request,
                                                  @PathVariable @ObjectIdConstraint ObjectId areaId,
                                                  @RequestBody @Valid InquiryPatientData inquiryPatientData
    ) {
        return patientServiceInArea.inquiryPatient(getId(request), areaId, inquiryPatientData);
    }

    @PutMapping(value = "addPatientToRegion/{areaId}/{patientId}")
    @ResponseBody
    @Operation(
            summary = "افزودن بیمار از قبل موجود به لیست پذیرش در یک منطقه توسط فرد مسئول پذیرش"
    )
    public void addPatientToRegion(HttpServletRequest request,
                                   @PathVariable @ObjectIdConstraint ObjectId areaId,
                                   @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        patientServiceInArea.addPatientToRegion(getId(request), areaId, patientId);
    }

    @GetMapping(value = "getModulePatients/{areaId}/{moduleId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن لبستی از بیماران پذیرش شده/نشده در یک ماژول خاص در منطقه مدنظر توسط مسئول و یا منشی آن ماژول"
    )
    public ResponseEntity<List<PatientJoinArea>> getModulePatients(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @RequestParam(required = false, value = "justRecepted") Boolean justRecepted,
            @RequestParam(required = false, value = "justUnRecepted") Boolean justUnRecepted
    ) {
        return patientServiceInArea.getModulePatients(
                getId(request), areaId, moduleId, justRecepted, justUnRecepted
        );
    }

    @PutMapping(value = "setReceptionStatusOfPatient/{areaId}/{moduleId}/{patientId}")
    @ResponseBody
    @Operation(
            summary = "ست کردن وضعیت پذیرش یک بیمار در ماژول مدنظر در منطقه"
    )
    public void setReceptionStatusOfPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestParam(value = "isRecepted") Boolean isRecepted
    ) {
        patientServiceInArea.setReceptionStatusOfPatient(
                getId(request), areaId, moduleId, patientId, isRecepted
        );
    }


    @GetMapping(value = "getPatients/{areaId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن لبستی از بیماران موجود در منطقه مدنظر توسط مسئول پذیرش"
    )
    public ResponseEntity<List<PatientJoinArea>> getPatients(HttpServletRequest request,
                                                             @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return patientServiceInArea.getPatients(
                getId(request), areaId
        );
    }

    @GetMapping(value = "getInsuranceList/{areaId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن لبستی از بیماران موجود در منطقه مدنظر توسط مسئول بیمه"
    )
    public ResponseEntity<List<PatientJoinArea>> getInsuranceList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(required = false, value = "justHasInsurance") Boolean justHasInsurance,
            @RequestParam(required = false, value = "justHasNotInsurance") Boolean justHasNotInsurance
    ) {
        return patientServiceInArea.getInsuranceList(
                getId(request), areaId, justHasInsurance, justHasNotInsurance
        );
    }

    @GetMapping(value = "getTrainList/{areaId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن لیست آموزش از بیماران موجود در منطقه مدنظر توسط مسئول آموزش"
    )
    public ResponseEntity<List<PatientJoinArea>> getTrainList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestParam(required = false, value = "justAdult") Boolean justAdult,
            @RequestParam(required = false, value = "justChildren") Boolean justChildren,
            @RequestParam(required = false, value = "justTrained") Boolean justTrained,
            @RequestParam(required = false, value = "justNotTrained") Boolean justNotTrained
    ) {
        return patientServiceInArea.getTrainList(
                getId(request), areaId, justAdult, justChildren,
                justTrained, justNotTrained
        );
    }

    @PutMapping(value = "setPatientTrainStatus/{areaId}/{patientId}")
    @Operation(
            summary = "ست کردن وضعیت آموزش یک بیمار در منطقه خاص توسط مسئول آموزش"
    )
    public void setPatientTrainStatus(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestParam(value = "hasTrained") Boolean hasTrained
    ) {
        patientServiceInArea.setPatientTrainStatus(
                getId(request), areaId, patientId, hasTrained
        );
    }

    @PutMapping(value = "setPatientInsuranceStatus/{areaId}/{patientId}")
    @Operation(
            summary = "ست کردن وضعیت بیمه یک بیمار در منطقه خاص توسط مسئول بیمه گر"
    )
    public void setPatientInsuranceStatus(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestParam(value = "insurance") Insurance insurance
    ) {
        patientServiceInArea.setPatientInsuranceStatus(
                getId(request), areaId, patientId, insurance
        );
    }

    @PutMapping(value = "addReferralForPatientByOwner/{areaId}/{patientId}/{moduleId}")
    @Operation(
            summary = "ارجاع یک بیمار توسط مسئول منطقه به یک ماژول خاص"
    )
    public void addReferralForPatientByOwner(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId
    ) {
        patientServiceInArea.addReferralForPatientByOwner(
                getId(request), areaId, patientId, moduleId
        );
    }

    @PutMapping(value = "addReferralForPatient/{areaId}/{moduleId}/{patientId}")
    @Operation(
            summary = "ارجاع یک بیمار توسط دکتر به یک ماژول دیگر"
    )
    public void addReferralForPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        patientServiceInArea.addReferralForPatient(
                getId(request), areaId, patientId, moduleId
        );
    }
}
