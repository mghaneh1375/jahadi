package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Tests.Seeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.HashMap;

import static four.group.jahadi.Utility.Utility.convertPersianDigits;

@RestController
@RequestMapping(path = "/api/user")
@Validated
public class UserAPIRoutes extends Router {

    @Autowired
    UserService userService;

    @Autowired
    Seeder seeder;

//    @PostMapping(value = "store")
//    @ResponseBody
//    public String store(final @RequestBody @Valid UserData userData) {
//        return userService.store(userData);
//    }

    @PutMapping(value = "setPic")
    @ResponseBody
    public void setPic(HttpServletRequest request,
                       @RequestBody MultipartFile file) throws UnAuthException, NotActivateAccountException {
        userService.setPic(getUserWithOutCheckCompleteness(request).getId(), file);
    }

    @PutMapping(value = "setGroup/{code}")
    @ResponseBody
    public void setGroup(HttpServletRequest request,
                         @PathVariable @Min(1111) @Max(999999) Integer code) throws UnAuthException, NotActivateAccountException {
        userService.setGroup(getUser(request).getId(), code);
    }

    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInData signInData) {
        return userService.signIn(signInData);
    }

    // ******************** INDIVIDUAL SIGNUP ************************

    @PostMapping(value = "/signUp")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpData signUpData) {
        return userService.signUp(signUpData);
    }

    @PostMapping(value = "checkSignUpFormStep1")
    public ResponseEntity<HashMap<String, Object>> checkSignUpFormStep1(@RequestBody @Valid SignUpStep1Data data) {
        return userService.checkUniqueness(data);
    }

    @PostMapping(value = "checkSignUpFormStep2")
    @ResponseBody
    public void checkSignUpFormStep2(@RequestBody @Valid SignUpStep2Data data) {
    }

    @PostMapping(value = "checkSignUpFormStep3")
    @ResponseBody
    public void checkSignUpFormStep3(@RequestBody @Valid SignUpStep3Data data) {
        userService.checkGroup(data);
    }

    // ****************** GROUP SIGNUP **********************

    @PostMapping(value = "/groupSignUp")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> groupSignUp(@RequestBody @Valid SignUpStep1ForGroupData data) {
        return userService.groupStore(data);
    }

    @PutMapping(value = "signUpStep2ForGroups")
    public void signUpStep2ForGroups(
            HttpServletRequest request,
            @RequestBody @Valid SignUpStep2ForGroupData data
    ) throws UnAuthException, NotActivateAccountException {
        userService.signUpStep2ForGroups(getUserWithOutCheckCompleteness(request), data);
    }

    @PutMapping(value = "signUpStep3ForGroups")
    public void signUpStep3ForGroups(
            HttpServletRequest request,
            @RequestBody @Valid SignUpStep3ForGroupData data
    ) throws UnAuthException, NotActivateAccountException {
        userService.signUpStep3ForGroups(getUserWithOutCheckCompleteness(request), data);
    }

    @PutMapping(value = "signUpStep4ForGroups")
    public void signUpStep4ForGroups(
            HttpServletRequest request,
            @RequestBody @Valid SignUpStep4ForGroupData data
    ) throws UnAuthException, NotActivateAccountException {
        userService.signUpStep4ForGroups(getUserWithOutCheckCompleteness(request), data);
    }


    @PostMapping(value = "/update")
    public void update(HttpServletRequest request,
                       @RequestBody @Valid UpdateInfoData updateInfoData) {
        userService.update(getId(request), updateInfoData);
    }

    @PostMapping(value = "/forgetPassword")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> forgetPassword(@RequestParam String NID) {
        return userService.forgetPass(convertPersianDigits(NID));
    }

    @PostMapping(value = "/checkForgetPassCode")
    @ResponseBody
    public ResponseEntity<String> checkForgetPassCode(
            @RequestBody @Valid CheckForgetPassCodeRequest checkCodeRequest
    ) {
        return userService.checkForgetPassCode(checkCodeRequest);
    }

    @PostMapping(value = "/checkCode")
    @ResponseBody
    public ResponseEntity<String> checkCode(@RequestBody @Valid CheckCodeRequest checkCodeRequest) {
        return userService.checkCode(checkCodeRequest);
    }

    @PostMapping(value = "/resetPassword")
    public void resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
    }

    @PostMapping(value = "/changePassword")
    public void changePassword(HttpServletRequest request,
                               @RequestBody @Valid PasswordData passwordData) throws UnAuthException, NotActivateAccountException {
        userService.changePassword(getUser(request).getId(), passwordData);
    }

    @GetMapping(value = "myInfo")
    @ResponseBody
    public ResponseEntity<User> myInfo(HttpServletRequest request) {
        return userService.findById(getId(request));
    }

    @GetMapping(value = "seed")
    public void seed() {
        seeder.seed();
    }

}
