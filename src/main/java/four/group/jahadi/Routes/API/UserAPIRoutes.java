package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Utility.Utility.generateErr;

@RestController
@RequestMapping(path = "/api/user")
@Validated
public class UserAPIRoutes {

    @Autowired
    UserService userService;

    @PostMapping(value = "store")
    @ResponseBody
    public String store(final @RequestBody @Valid UserData userData) {

        List<String> filters = new ArrayList<>();
        filters.add("phone|eq|" + userData.getPhone());

        if(userService.exist(filters))
            return generateErr("شماره همراه وارد شده در سیستم موجود است");

        System.out.println("dwq");
        filters.set(0, "NID|eq|" + userData.getNid());
        if(userService.exist(filters))
            return generateErr("کد ملی وارد شده در سیستم موجود است");

        return userService.store(userData);
    }

}
