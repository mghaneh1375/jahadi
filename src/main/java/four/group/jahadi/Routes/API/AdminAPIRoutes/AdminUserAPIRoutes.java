package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.SignUp.PasswordData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Validator.EnumValidator;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "api/admin/user")
@Validated
public class AdminUserAPIRoutes {

    @Autowired
    private UserService userService;

    @PutMapping(value = "changeStatus/{userId}/{status}")
    @ResponseBody
    public void changeStatus(@PathVariable @ObjectIdConstraint ObjectId userId,
                             @PathVariable @EnumValidator(enumClazz = AccountStatus.class) AccountStatus status
    ) {
        userService.changeStatus(userId, status);
    }


    @PutMapping(value = "changePassword/{userId}")
    @ResponseBody
    public void changePassword(@PathVariable @ObjectIdConstraint ObjectId userId,
                               @RequestBody @Valid PasswordData passwordData
    ) {
        userService.changePassword(userId, passwordData);
    }

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<User>> list(
            @RequestParam(required = false, value = "status") AccountStatus status,
            @RequestParam(required = false, value = "access") Access access,
            @RequestParam(required = false, value = "sex") Sex sex,
            @RequestParam(required = false, value = "NID") String NID,
            @RequestParam(required = false, value = "phone") String phone,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "justGroupRequests") Boolean justGroupRequests,
            @RequestParam(required = false, value = "groupName") String groupName
    ) {
        return userService.list(status, access, name, NID, phone, sex, groupName, null, justGroupRequests);
    }

    @GetMapping(value = "get/{userId}")
    @ResponseBody
    public ResponseEntity<User> get(@PathVariable @ObjectIdConstraint ObjectId userId){
        return userService.findById(userId);
    }

    @PutMapping(value = "setGroup/{userId}/{code}")
    @ResponseBody
    public void setGroup(@PathVariable @ObjectIdConstraint ObjectId userId,
                         @PathVariable @Min(1111) @Max(999999) Integer code) {
        userService.setGroup(userId, code);
    }

    @PutMapping(value = "/toggleStatus")
    @ResponseBody
    public String toggleStatus(@RequestBody @ObjectIdConstraint ObjectId id) {
        return userService.toggleStatus(id);
    }

    @DeleteMapping(value = "removeFromGroup/{userId}/{groupId}")
    @ResponseBody
    public void removeFromGroup(final @PathVariable @ObjectIdConstraint ObjectId userId,
                                final @PathVariable @ObjectIdConstraint ObjectId groupId
    ) {
        userService.removeFromGroup(userId, groupId);
    }

}
