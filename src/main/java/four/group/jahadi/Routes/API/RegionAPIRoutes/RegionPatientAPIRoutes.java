package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
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


//    @GetMapping(value = "getMyPatients/{areaId}/{moduleId}")
//    @ResponseBody
//    @Operation(
//            summary = "گرفتن لبستی از بیماران پذیرش شده/نشده در یک ماژول خاص در منطقه مدنظر توسط مسئول آن ماژول"
//    )
//    public ResponseEntity<List<Patient>> getMyPatients(HttpServletRequest request,
//                                                       @PathVariable @ObjectIdConstraint ObjectId areaId,
//                                                       @PathVariable @ObjectIdConstraint ObjectId moduleId,
//                                                       @RequestParam(required = false, value = "justRecepted") Boolean justRecepted,
//                                                       @RequestParam(required = false, value = "justUnRecepted") Boolean justUnRecepted
//    ) {
//        return patientServiceInArea.getMyPatients(
//                getId(request), areaId, moduleId, justRecepted, justUnRecepted
//        );
//    }

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

}
