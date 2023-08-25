package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/user")
@Validated
public class UserAPIRoutes {

    @Autowired
    UserService userService;

    @PostMapping(value = "store")
    @ResponseBody
    public String store(final @RequestBody @Valid UserData userData) {
        return userService.store(userData);
    }

    @GetMapping(value = "list")
    @ResponseBody
    public String list() {
        return userService.list();
    }

}
