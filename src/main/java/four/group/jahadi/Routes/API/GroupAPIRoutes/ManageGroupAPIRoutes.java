package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "api/group/group")
@Validated
public class ManageGroupAPIRoutes extends Router {

    @Autowired
    private GroupService groupService;

    @PutMapping(value = "changeCode")
    public void changeCode(HttpServletRequest request,
                           @RequestBody @Min(100000) @Max(111111) int code) {
        groupService.changeCode(getGroup(request), code);
    }

}
