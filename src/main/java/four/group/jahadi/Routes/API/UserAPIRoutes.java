package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static four.group.jahadi.Utility.StaticValues.JSON_OK;
import static four.group.jahadi.Utility.Utility.convertPersianDigits;

@RestController
@RequestMapping(path = "/api/user")
@Validated
public class UserAPIRoutes {

    @Autowired
    UserService userService;

//    @PostMapping(value = "store")
//    @ResponseBody
//    public String store(final @RequestBody @Valid UserData userData) {
//        return userService.store(userData);
//    }

    @GetMapping(value = "list")
    @ResponseBody
    public String list() {
        return userService.list();
    }

    @PostMapping(value = "checkSignUpFormStep1")
    @ResponseBody
    public String checkSignUpFormStep1(@RequestBody @Valid SignUpStep1Data data) {
        return userService.checkUniqueness(data);
    }

    @PostMapping(value = "checkSignUpFormStep2")
    @ResponseBody
    public String checkSignUpFormStep2(@RequestBody @Valid SignUpStep2Data data) {
        return JSON_OK;
    }

    @PostMapping(value = "checkSignUpFormStep2ForGroups")
    @ResponseBody
    public String checkSignUpFormStep2ForGroups(@RequestBody @Valid SignUpStep2ForGroupData data) {
        return JSON_OK;
    }

    @PostMapping(value = "checkSignUpFormStep3")
    @ResponseBody
    public String checkSignUpFormStep3(@RequestBody @Valid SignUpStep3Data data) {
        return userService.checkGroup(data);
    }

    @PutMapping(value = "/toggleStatus")
    @ResponseBody
    public String toggleStatus(@RequestBody @ObjectIdConstraint ObjectId id) {
        return userService.toggleStatus(id);
    }

    @PostMapping(value = "/signIn")
    @ResponseBody
    public String signIn(@RequestBody @Valid SignInData signInData) {
        return userService.signIn(signInData);
    }

    @PostMapping(value = "/groupSignUp")
    @ResponseBody
    public String groupSignUp(@RequestBody @Valid GroupSignUpData groupSignUpData) {
        return userService.groupStore(groupSignUpData);
    }

    @PostMapping(value = "/signUp")
    @ResponseBody
    public String signUp(@RequestBody @Valid SignUpData signUpData) {
        return userService.store(signUpData);
    }


    @PostMapping(value = "/forgetPassword")
    @ResponseBody
    public String forgetPassword(@RequestParam String NID) {
        return userService.forgetPass(convertPersianDigits(NID));
    }

    @PostMapping(value = "/checkCode")
    @ResponseBody
    public String checkCode(@RequestBody @Valid CheckCodeRequest checkCodeRequest) {
        return userService.checkCode(checkCodeRequest);
    }

    @PostMapping(value = "/resetPassword")
    @ResponseBody
    public String resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

}
