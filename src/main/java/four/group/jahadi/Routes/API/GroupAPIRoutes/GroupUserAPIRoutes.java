package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.DTO.UserDigest;
import four.group.jahadi.DTO.WareHouseAccessForGroupData;
import four.group.jahadi.DTO.profile.SetTripsCountDto;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.*;
import four.group.jahadi.Validator.EnumValidator;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "api/group/user")
@Validated
@RequiredArgsConstructor
public class GroupUserAPIRoutes extends Router {

    private final UserService userService;
    private final WareHouseAccessService wareHouseAccessService;
    private final GroupService groupService;
    private final ExternalReferralAccessForGroupService externalReferralAccessForGroupService;
    private final GroupReportService groupReportService;

    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن لیست کاربران یک گروه توسط مسئول آن گروه")
    public ResponseEntity<Page<User>> list(
            HttpServletRequest request,
            @RequestParam(required = false, value = "sex") Sex sex,
            @RequestParam(required = false, value = "NID") String NID,
            @RequestParam(required = false, value = "phone") String phone,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(value = "pageIndex") @NotNull @Min(0) @Max(1000) Integer pageIndex,
            @RequestParam(value = "pageSize") @NotNull @Min(5) @Max(1000) Integer pageSize
    ) throws UnAuthException, NotActivateAccountException {
        return userService.paginateList(
                pageIndex, pageSize,
                AccountStatus.ACTIVE, null, name, NID, phone, sex,
                null, getGroup(request), null, searchKey
        );
    }

    @PutMapping(value = "changeStatus/{userId}/{status}")
    @ResponseBody
    @Operation(summary = "Available values for status parameter: 1- ACTIVE, 2- PENDING, 3- BLOCKED")
    public void changeStatus(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId,
            @PathVariable @EnumValidator(enumClazz = AccountStatus.class) String status
    ) {
        userService.changeStatusByGroup(getTokenInfo(request).getGroupId(), userId, AccountStatus.valueOf(status.toUpperCase()));
    }

    @DeleteMapping(value = "removeFromGroup/{userId}")
    @Operation(summary = "حذف کاربر از یک گروه توسط مسئول آن گروه")
    public void removeFromGroup(HttpServletRequest request,
                                final @PathVariable @ObjectIdConstraint ObjectId userId) {
        userService.removeFromGroup(userId, getGroup(request));
    }

//    @PostMapping(value = "signIn")
//    @ResponseBody
//    @Operation(summary = "ورود کردن به اکانت یک کاربر خاص در یک گروه توسط مسئول آن گروه")
//    public ResponseEntity<String> signIn(
//            HttpServletRequest request,
//            @RequestBody @Valid AdminSignInData dto
//    ) {
//        return userService.groupSignIn(dto, getGroup(request));
//    }

    @PostMapping(value = "generateTempCode/{userId}")
    @ResponseBody
    public ResponseEntity<String> generateTempCode(
            HttpServletRequest request,
            @PathVariable @NotNull @ObjectIdConstraint ObjectId userId
    ) {
        return userService.generateTempCode(getFullTokenInfo(request).getGroupId(), userId);
    }

    @GetMapping(value = "get/{userId}")
    @ResponseBody
    @Operation(summary = "گرفتن اطلاعات تکمیلی کاربر یک گروه توسط مسئول آن گروه")
    public ResponseEntity<User> get(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        return userService.findById(userId, getGroup(request));
    }

    @GetMapping(value = "getWareHouseAccesses")
    @ResponseBody
    @Operation(summary = "گرفتن لیستی از کاربرانی که در گروه دسترسی انبارداری دارند")
    public ResponseEntity<List<WareHouseAccessForGroupJoinWithUser>> getWareHouseAccesses(
            HttpServletRequest request
    ) {
        return wareHouseAccessService.list(getGroup(request));
    }

    @GetMapping(value = "getAccesses")
    @ResponseBody
    @Operation(summary = "گرفتن لیستی از کاربرانی که در گروه دسترسی دارند")
    public ResponseEntity<Page<UserAccessInGroupDto>> getAccesses(
            HttpServletRequest request,
            @RequestParam(value = "pageIndex") @NotNull @Min(0) @Max(10000) int pageIndex,
            @RequestParam(value = "pageSize") @NotNull @Min(5) @Max(100) int pageSize
    ) {
        return groupService.getAccesses(getGroup(request), pageIndex, pageSize);
    }

    @PostMapping(value = "addWareHouseAccesses")
    @ResponseBody
    @Operation(summary = "افزودن یا ویرایش کاربری از گروه برای دسترسی انبارداری")
    public ResponseEntity<WareHouseAccessForGroupJoinWithUser> addWareHouseAccesses(
            HttpServletRequest request,
            @RequestBody @Valid WareHouseAccessForGroupData dto
    ) {
        return wareHouseAccessService.store(
                dto, getGroup(request)
        );
    }

    @PostMapping(value = "addReportAccess/{userId}")
    @ResponseBody
    @Operation(summary = "افزودن یا ویرایش کاربری از گروه برای دسترسی انبارداری")
    public ResponseEntity<ReportAccessJoinWithUser> addReportAccess(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        return groupReportService.store(
                userId, getGroup(request)
        );
    }

    @DeleteMapping(value = "removeWareHouseAccesses/{userId}")
    @ResponseBody
    @Operation(summary = "حذف کاربر از دسترسی کاربران انبارداری در یک گروه")
    public void removeWareHouseAccesses(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        wareHouseAccessService.removeFromWareHouseAccesses(
                userId, getGroup(request)
        );
    }

    @DeleteMapping(value = "revokeExternalReferralAccesses/{userId}")
    @ResponseBody
    @Operation(summary = "حذف کاربر از دسترسی کاربران ارجاع خارج در یک گروه")
    public void revokeExternalReferralAccesses(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        externalReferralAccessForGroupService.revokeAccess(
                userId, getGroup(request)
        );
    }

    @DeleteMapping(value = "revokeReportAccess/{userId}")
    @ResponseBody
    @Operation(summary = "حذف کاربر از دسترسی کاربران گزارشگیر در یک گروه")
    public void revokeReportAccess(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        groupReportService.revokeAccess(
                userId, getGroup(request)
        );
    }

    @GetMapping(value = "getExternalReferralAccesses")
    @ResponseBody
    @Operation(summary = "گرفتن لیستی از کاربرانی که در گروه دسترسی بررسی ارجاعات خارجی دارند")
    public ResponseEntity<List<ExternalReferralAccessJoinWithUser>> getExternalReferralAccesses(
            HttpServletRequest request
    ) {
        return externalReferralAccessForGroupService.list(getGroup(request));
    }

    @PostMapping(value = "addToExternalReferralAccesses/{userId}")
    @ResponseBody
    @Operation(summary = "افزودن کاربری از گروه برای دسترسی بررسی ارجاعات خارج از اردو")
    public ResponseEntity<ExternalReferralAccessJoinWithUser> addToExternalReferralAccesses(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        return externalReferralAccessForGroupService.store(
                userId, getGroup(request)
        );
    }

    @GetMapping(value = "findGroupActiveMembersName")
    @ResponseBody
    public ResponseEntity<List<UserDigest>> findGroupActiveMembersName(HttpServletRequest request) {
        return userService.findGroupActiveMembersName(getGroup(request));
    }

    @GetMapping(value = "excel-report")
    public void excelReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false, value = "status") AccountStatus status,
            @RequestParam(required = false, value = "access") Access access,
            @RequestParam(required = false, value = "sex") Sex sex,
            @RequestParam(required = false, value = "NID") String NID,
            @RequestParam(required = false, value = "phone") String phone,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "justGroupRequests") Boolean justGroupRequests,
            @RequestParam(required = false, value = "groupName") String groupName,
            @RequestParam(required = false, value = "searchKey") String searchKey
    ) {
        TokenInfo tokenInfo = getFullTokenInfo(request);
        userService.excelReport(
                response,
                status, access, name,
                NID, phone, sex,
                tokenInfo.getAccesses().contains(Access.ADMIN)
                        ? groupName
                        : null,
                tokenInfo.getAccesses().contains(Access.ADMIN)
                        ? null
                        : tokenInfo.getGroupId(),
                justGroupRequests, searchKey
        );
    }

    @PutMapping(value = "set-user-old-trips-count/{userId}")
    @ResponseBody
    public ResponseEntity setUserOldTripsCount(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId userId,
            @RequestBody @Validated SetTripsCountDto dto
    ) {
        userService.setUserOldTripsCount(getGroup(request), userId, dto.getTripsCount());
        return ResponseEntity.ok().build();
    }
}
