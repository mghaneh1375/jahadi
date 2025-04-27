package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.SignUp.SignInData;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Security.JwtTokenFilter;
import four.group.jahadi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/user")
@Validated
public class UserAPIRoutes extends Router {

    @Autowired
    UserService userService;

    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInData signInData) {
        return userService.signIn(signInData);
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
}
