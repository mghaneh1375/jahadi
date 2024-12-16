package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.AddPatientExternalReferralsServiceDAO;
import four.group.jahadi.DTO.SetPatientExternalReferralsTrackingStatusDAO;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Area.PatientExternalForm;
import four.group.jahadi.Models.ExternalReferralService;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.TokenInfo;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.PatientExternalReferralsService;
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
@RequestMapping(value = "/api/jahadgar/external_referrals")
@Validated
public class JahadgarPatientExternalReferralsAPIRoutes extends Router {

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
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return patientExternalReferralsService.getAllExternalReferrals(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                fullTokenInfo.getGroupId(), areaId
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
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return patientExternalReferralsService.getPatientExternalReferrals(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId
        );
    }

    @PutMapping(value = "setPatientExternalReferralsTrackingStatus")
    @Operation(
            summary = "گرفتن فرم بیمار توسط پزشک در یک ماژول خاص در یک منطقه"
    )
    public void setPatientExternalReferralsTrackingStatus(
            HttpServletRequest request,
            @RequestBody @Valid SetPatientExternalReferralsTrackingStatusDAO dao
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.setPatientExternalReferralsTrackingStatus(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), dao
        );
    }

    @PutMapping(value = "addPatientExternalReferralsService")
    @Operation(
            summary = "افزودن خدمت برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void addPatientExternalReferralsService(
            HttpServletRequest request,
            @RequestBody @Valid AddPatientExternalReferralsServiceDAO dao
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.addPatientExternalReferralsService(
                fullTokenInfo.getAccesses().contains(Access.GROUP),
                fullTokenInfo.getUserId(), getGroup(request), dao
        );
    }

    @GetMapping(value = "getPatientExternalReferralsServices/{areaId}/{patientId}/{formId}")
    @Operation(
            summary = "گرفتن خدمات ارائه شده برای مراجعه خارج از اردو برای یک بیمار"
    )
    public ResponseEntity<List<ExternalReferralService>> getPatientExternalReferralsServices(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        return patientExternalReferralsService.getPatientExternalReferralsServices(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId
        );
    }
}
