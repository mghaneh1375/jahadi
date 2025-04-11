package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Models.User;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/admin/user")
@Validated
public class AdminUserAPIRoutes {

    @Autowired
    private UserService userService;

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

    @PostMapping(value = "signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody @Valid AdminSignInData dto) {
        return userService.adminSignIn(dto);
    }

}
