package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.ChangePhoneDAO;
import four.group.jahadi.DTO.ChangePhoneResponseDAO;
import four.group.jahadi.DTO.DoChangePhoneDAO;
import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Security.JwtTokenFilter;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Tests.Seeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @GetMapping(value = "test")
    public void test() {
        userService.test();
    }

    @PutMapping(value = "setPic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public void setPic(HttpServletRequest request,
                       @RequestPart("file") MultipartFile file
    ) throws UnAuthException, NotActivateAccountException {
        userService.setPic(getUserWithOutCheckCompleteness(request).getId(), file);
    }

    @PutMapping(value = "setGroup/{code}")
    @ResponseBody
    public void setGroup(
            HttpServletRequest request,
            @PathVariable @Min(1111) @Max(999999) Integer code
    ) {
        userService.setGroup(getId(request), code);
    }

    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInData signInData) {
        return userService.signIn(signInData);
    }

    // ******************** INDIVIDUAL SIGNUP ************************

    // ****************** GROUP SIGNUP **********************

    @PostMapping(value = "/groupSignUp")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> groupSignUp(@RequestBody @Valid SignUpStep1ForGroupData data) {
        return userService.groupStore(data);
    }

    @DeleteMapping(value = "logout")
    public void logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            userService.logout(token);
            JwtTokenFilter.removeTokenFromCache(token.replace("Bearer ", ""));
        } catch (Exception x) {
            System.out.println(x.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void update(
            HttpServletRequest request,
            @RequestBody @Valid UpdateInfoData updateInfoData
    ) {
        userService.update(getId(request), updateInfoData);
    }

    @PostMapping(value = "/changePhone")
    public ResponseEntity<ChangePhoneResponseDAO> changePhone(
            HttpServletRequest request,
            @RequestBody @Valid ChangePhoneDAO changePhoneDAO
    ) throws UnAuthException, NotActivateAccountException {
        return userService.changePhone(getUser(request), changePhoneDAO);
    }

    @PostMapping(value = "/doChangePhone")
    public void changePhone(
            HttpServletRequest request,
            @RequestBody @Valid DoChangePhoneDAO doChangePhoneDAO
    ) throws UnAuthException, NotActivateAccountException {
        userService.doChangePhone(
                getUser(request), doChangePhoneDAO,
                request.getHeader("Authorization")
        );
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


    @GetMapping(value = "digest")
    @ResponseBody
    public ResponseEntity<User> info(HttpServletRequest request) {
        return userService.info(getId(request));
    }

    @GetMapping(value = "seed")
    public void seed() {
        seeder.seed();
    }

    @GetMapping(value = "moduleSeeder")
    public void moduleSeeder() {
        seeder.moduleSeeder();
    }
}
