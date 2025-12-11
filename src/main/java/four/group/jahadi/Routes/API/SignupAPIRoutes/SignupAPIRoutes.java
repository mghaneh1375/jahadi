package four.group.jahadi.Routes.API.SignupAPIRoutes;

import four.group.jahadi.DTO.SignUp.PersonSignUpCheckPhoneData;
import four.group.jahadi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
