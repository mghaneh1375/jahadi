package four.group.jahadi.Routes.API.RegionAPIRoutes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import four.group.jahadi.DTO.ModuleForms.PatientFormData;
import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
import four.group.jahadi.DTO.Patient.PatientReferralData;
import four.group.jahadi.DTO.Patient.TrainFormData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Models.Area.*;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.PatientServiceInArea;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
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

    @PutMapping(value = "updatePatient/{patientId}/{areaId}")
    @ResponseBody
    @Operation(
            summary = "ویرایش اطلاعات بیمار در یک منطقه توسط فرد مسئول پذیرش"
    )
    public void updatePatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid PatientData patientData
    ) {
        patientServiceInArea.updatePatient(patientId, getId(request), areaId, patientData);
    }

    @PostMapping(value = "inquiryPatient/{areaId}")
    @ResponseBody
    @Operation(
            summary = "استعلام بیمار توسط مسئول پذیرش در یک منطقه"
    )
    public ResponseEntity<Patient> inquiryPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid InquiryPatientData inquiryPatientData
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return patientServiceInArea.inquiryPatient(
                fullTokenInfo.getUserId(), areaId, inquiryPatientData,
                fullTokenInfo.getAccesses().contains(Access.GROUP) ? fullTokenInfo.getGroupId() : null
        );
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

    @DeleteMapping(value = "removePatientFromArea/{areaId}/{patientId}")
    @Operation(
            summary = "حذف بیمار موجود از لیست پذیرش در یک منطقه توسط فرد مسئول پذیرش"
    )
    public void removePatientFromArea(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        patientServiceInArea.removePatientFromArea(getId(request), areaId, patientId);
    }

    @GetMapping(value = "getModulePatients/{areaId}/{moduleId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن لبستی از بیماران پذیرش شده/نشده در یک ماژول خاص در منطقه مدنظر توسط مسئول و یا منشی آن ماژول"
    )
    public ResponseEntity<HashMap<String, Object>> getModulePatients(
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
    public ResponseEntity<List<PatientJoinArea>> getPatients(
            HttpServletRequest request,
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

    @PutMapping(value = "setPatientTrainFrom/{areaId}/{patientId}")
    @Operation(
            summary = "ست کردن فرم آموزش یک بیمار در منطقه خاص توسط مسئول آموزش"
    )
    public void setPatientTrainFrom(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestBody @Valid TrainFormData trainFormData
    ) {
        patientServiceInArea.setPatientTrainFrom(
                getId(request), areaId, patientId, trainFormData
        );
    }

    @GetMapping(value = "getPatientTrainFrom/{areaId}/{patientId}")
    @Operation(
            summary = "گرفتن فرم آموزش یک بیمار در منطقه"
    )
    public ResponseEntity<TrainForm> getPatientTrainFrom(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return patientServiceInArea.getPatientTrainFrom(
                fullTokenInfo.getUserId(), areaId, patientId,
                fullTokenInfo.getAccesses().contains(Access.GROUP) ? fullTokenInfo.getGroupId() : null
        );
    }


    @GetMapping(value = "getPatientReferrals/{areaId}/{patientId}")
    @Operation(
            summary = "گرفتن ارجاعاتی که یک بیمار در کل منطقه داشته است"
    )
    public ResponseEntity<List<PatientReferral>> getPatientReferrals(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        return patientServiceInArea.getPatientReferrals(
                getId(request), areaId, patientId
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
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @RequestBody(required = false) @Valid PatientReferralData data
    ) {
        patientServiceInArea.addReferralForPatientByOwner(
                getId(request), areaId, patientId, moduleId, data != null ? data.getDesc() : null
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
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestBody(required = false) @Valid PatientReferralData data
    ) {
        patientServiceInArea.addReferralForPatient(
                getId(request), areaId, patientId, moduleId, data != null ? data.getDesc() : null
        );
    }

    @PutMapping(value = "addReferralForPatientBySubModule/{areaId}/{moduleId}/{subModuleId}/{patientId}")
    @Operation(
            summary = "ارجاع یک بیمار توسط دکتر در یک ساب ماژول که ماژول مقصد توسط سیستم تشخیص داده می شود"
    )
    public void addReferralForPatientBySubModule(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId subModuleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        patientServiceInArea.addReferralForPatientBySubModule(
                getId(request), areaId, patientId, moduleId, subModuleId
        );
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "setPatientForm/{areaId}/{moduleId}/{subModuleId}/{patientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "ست کردن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public void setPatientForm(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId subModuleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestParam(name = "forms") String tmp,
            @RequestPart(name = "files", required = false) MultipartFile[] files
    ) {
//        @Valid @Size(min = 1) ValidList<PatientFormData> forms
        try {
            List<PatientFormData> forms = new ObjectMapper().readValue(tmp, new TypeReference<>() {
            });
            if (forms.size() == 0)
                throw new InvalidFieldsException("form size is 0");

            patientServiceInArea.setPatientForm(
                    getId(request), areaId, moduleId,
                    subModuleId, patientId, forms, files
            );
        } catch (Exception x) {
            throw new InvalidFieldsException(x.getMessage());
        }
    }

    @GetMapping(value = "getPatientForm/{areaId}/{moduleId}/{subModuleId}/{patientId}")
    @Operation(
            summary = "گرفتن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public ResponseEntity<List<PatientAnswer>> getPatientForm(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId subModuleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        return patientServiceInArea.getPatientForm(
                getId(request), areaId, moduleId, subModuleId, patientId
        );
    }


    @GetMapping(value = "patientReport/{patientId}")
    @Operation(
            summary = "گرفتن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public void patientReport(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            HttpServletResponse response
    ) {
        patientServiceInArea.patientReport(
                patientId, null, false, null, response
        );
    }
}
