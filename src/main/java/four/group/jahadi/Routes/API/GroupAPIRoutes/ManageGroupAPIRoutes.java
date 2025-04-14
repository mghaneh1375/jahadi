package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping(value = "api/group/group")
@Validated
public class ManageGroupAPIRoutes extends Router {

    @Autowired
    private GroupService groupService;

    @GetMapping(value = "statisticData")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statisticData(HttpServletRequest request) {
        return groupService.statisticData(getGroup(request));
    }

}
