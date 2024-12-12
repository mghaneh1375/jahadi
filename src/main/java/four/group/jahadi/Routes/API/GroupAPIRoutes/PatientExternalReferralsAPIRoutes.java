package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Models.Area.PatientExternalForm;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.PatientExternalReferralsService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/group/external_referrals")
@Validated
public class PatientExternalReferralsAPIRoutes extends Router {

    @Autowired
    private PatientExternalReferralsService patientExternalReferralsService;

    @GetMapping(value = "getExternalReferrals/{areaId}")
    @Operation(
            summary = "گرفتن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public ResponseEntity<List<Patient>> getExternalReferrals(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return patientExternalReferralsService.getAllExternalReferrals(
                getId(request), areaId
        );
    }

    @GetMapping(value = "getPatientExternalReferrals/{areaId}/{patientId}")
    @Operation(
            summary = "گرفتن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public ResponseEntity<List<PatientExternalForm>> getPatientExternalReferrals(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        return patientExternalReferralsService.getPatientExternalReferrals(
                getId(request), areaId, patientId
        );
    }
}
