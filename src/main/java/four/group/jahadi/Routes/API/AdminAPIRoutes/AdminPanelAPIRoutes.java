package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Service.AdminService;
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
@RequestMapping(path = "/api/admin/panel")
@Validated
public class AdminPanelAPIRoutes {

    @Autowired
    private AdminService adminService;

    @GetMapping(value = "statistic")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statistic() {
        return adminService.statistic();
    }


}
