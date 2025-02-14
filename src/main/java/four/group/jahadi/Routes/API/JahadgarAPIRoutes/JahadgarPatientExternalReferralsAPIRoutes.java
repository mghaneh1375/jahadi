package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.AddPatientExternalReferralsServiceDAO;
import four.group.jahadi.DTO.CallHistoryDAO;
import four.group.jahadi.DTO.PaymentsToPatientDAO;
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
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "hasExternalReferralAccess")
    @Operation(summary = "چک کردن اینکه آیا شخص به ارجاعات خارجی اردو دسترسی دارد یا خیر")
    public ResponseEntity<Boolean> hasExternalReferralAccess(
            HttpServletRequest request
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        if(fullTokenInfo.getAccesses().contains(Access.GROUP))
            return new ResponseEntity<>(true, HttpStatus.OK);

        return patientExternalReferralsService.hasExternalReferralAccess(
                fullTokenInfo.getGroupId(), fullTokenInfo.getUserId()
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

    @DeleteMapping(value = "removePatientExternalReferralsService/{areaId}/{patientId}/{formId}/{serviceId}")
    @Operation(
            summary = "حذف خدمت برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void removePatientExternalReferralsService(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId,
            @PathVariable @ObjectIdConstraint ObjectId serviceId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.removePatientExternalReferralsService(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId, serviceId
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

    @PutMapping(value = "addPatientExternalReferralsServicesFinanceHistory/{areaId}/{patientId}/{formId}/{serviceId}")
    @Operation(
            summary = "افزودن تاریخچه مالی خدمت ارائه شده برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void addPatientExternalReferralsServicesFinanceHistory(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId,
            @PathVariable @ObjectIdConstraint ObjectId serviceId,
            @RequestBody @Valid PaymentsToPatientDAO dao
            ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.addPatientExternalReferralsServicesFinanceHistory(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId, serviceId, dao
        );
    }

    @DeleteMapping(value = "removePatientExternalReferralsServicesFinanceHistory/{areaId}/{patientId}/{formId}/{serviceId}/{financeId}")
    @Operation(
            summary = "حذف تاریخچه مالی خدمت ارائه شده برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void removePatientExternalReferralsServicesFinanceHistory(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId,
            @PathVariable @ObjectIdConstraint ObjectId serviceId,
            @PathVariable @ObjectIdConstraint ObjectId financeId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.removePatientExternalReferralsServicesFinanceHistory(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId, serviceId, financeId
        );
    }

    @PutMapping(value = "addPatientExternalReferralsServicesCallHistory/{areaId}/{patientId}/{formId}/{serviceId}")
    @Operation(
            summary = "افزودن تاریخچه مالی خدمت ارائه شده برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void addPatientExternalReferralsServicesCallHistory(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId,
            @PathVariable @ObjectIdConstraint ObjectId serviceId,
            @RequestBody @Valid CallHistoryDAO dao
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.addPatientExternalReferralsServicesCallHistory(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId, serviceId, dao
        );
    }

    @DeleteMapping(value = "removePatientExternalReferralsServicesCallHistory/{areaId}/{patientId}/{formId}/{serviceId}/{callId}")
    @Operation(
            summary = "حذف تاریخچه مالی خدمت ارائه شده برای مراجعه خارج از اردو برای یک بیمار"
    )
    public void removePatientExternalReferralsServicesCallHistory(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId formId,
            @PathVariable @ObjectIdConstraint ObjectId serviceId,
            @PathVariable @ObjectIdConstraint ObjectId callId
    ) {
        TokenInfo fullTokenInfo = getFullTokenInfo(request);
        patientExternalReferralsService.removePatientExternalReferralsServicesCallHistory(
                fullTokenInfo.getAccesses().contains(Access.GROUP)
                        ? null
                        : fullTokenInfo.getUserId(),
                getGroup(request), areaId, patientId, formId, serviceId, callId
        );
    }
}
