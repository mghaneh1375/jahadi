package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.ExternalReferralAccessJoinWithUser;
import four.group.jahadi.Models.User;
import four.group.jahadi.Models.WareHouseAccessForGroupJoinWithUser;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.ExternalReferralAccessForGroupService;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Service.WareHouseAccessService;
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
@RequestMapping(value = "api/group/user")
@Validated
public class GroupUserAPIRoutes extends Router {

    @Autowired
    private UserService userService;
    @Autowired
    private WareHouseAccessService wareHouseAccessService;
    @Autowired
    private ExternalReferralAccessForGroupService externalReferralAccessForGroupService;

    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن لیست کاربران یک گروه توسط مسئول آن گروه")
    public ResponseEntity<List<User>> list(
            HttpServletRequest request,
            @RequestParam(required = false, value = "sex") Sex sex,
            @RequestParam(required = false, value = "NID") String NID,
            @RequestParam(required = false, value = "phone") String phone,
            @RequestParam(required = false, value = "name") String name
    ) throws UnAuthException, NotActivateAccountException {
        return userService.list(null, null, name, NID, phone, sex, null, getGroup(request), null);
    }

    @DeleteMapping(value = "removeFromGroup/{userId}")
    @Operation(summary = "حذف کاربر از یک گروه توسط مسئول آن گروه")
    public void removeFromGroup(HttpServletRequest request,
                                final @PathVariable @ObjectIdConstraint ObjectId userId) {
        userService.removeFromGroup(userId, getGroup(request));
    }

    @PostMapping(value = "signIn")
    @ResponseBody
    @Operation(summary = "ورود کردن به اکانت یک کاربر خاص در یک گروه توسط مسئول آن گروه")
    public ResponseEntity<String> signIn(
            HttpServletRequest request,
            @RequestBody @Valid AdminSignInData dto
    ) {
        return userService.groupSignIn(dto, getGroup(request));
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
}
