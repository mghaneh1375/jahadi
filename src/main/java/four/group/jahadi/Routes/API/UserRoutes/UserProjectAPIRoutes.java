package four.group.jahadi.Routes.API.UserRoutes;

import four.group.jahadi.Service.ProjectService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/user/project")
@Validated
public class UserProjectAPIRoutes {

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/list")
    @ResponseBody
    public String list(HttpServletRequest request,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "justActive", required = false) Boolean justActive,
                       @RequestParam(value = "justArchive", required = false) Boolean justArchive
    ) {
        return projectService.list(name, justActive, justArchive, new ObjectId("64bae4ab6e6022659a59dc23"));
    }

}
