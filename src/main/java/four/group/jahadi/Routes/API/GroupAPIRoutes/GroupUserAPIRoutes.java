package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Validator.ObjectIdConstraint;
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

    @GetMapping(value = "list")
    @ResponseBody
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
    public void removeFromGroup(HttpServletRequest request,
                                final @PathVariable @ObjectIdConstraint ObjectId userId) {
        userService.removeFromGroup(userId, getGroup(request));
    }

    @PostMapping(value = "signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(
            HttpServletRequest request,
            @RequestBody @Valid AdminSignInData dto
    ) {
        return userService.groupSignIn(dto, getGroup(request));
    }

}
