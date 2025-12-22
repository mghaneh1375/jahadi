package four.group.jahadi.Routes.API.SignupAPIRoutes;

import four.group.jahadi.DTO.SignUp.PersonSignUpCheckPhoneData;
import four.group.jahadi.DTO.SignUp.PersonalSignUpData;
import four.group.jahadi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Validated
@RestController
@RequestMapping(path = "/api/signup")
@RequiredArgsConstructor
public class SignupAPIRoutes {

    private final UserService userService;

    @PostMapping(value = "personSignupCheckPhone")
    public ResponseEntity<HashMap<String, Object>> personSignupCheckPhone(@RequestBody @Valid PersonSignUpCheckPhoneData data) {
        return userService.checkPhone(data);
    }

    @PostMapping(value = "/personalSignUp")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody @Valid PersonalSignUpData signUpData) {
        return userService.newSignUp(signUpData);
    }
}
