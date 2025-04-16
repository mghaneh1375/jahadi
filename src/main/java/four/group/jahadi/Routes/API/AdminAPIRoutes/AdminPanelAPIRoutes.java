package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.AdminService;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/api/admin/panel")
@Validated
public class AdminPanelAPIRoutes extends Router {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AreaService areaService;

    @GetMapping(value = "statistic")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statistic() {
        return adminService.statistic();
    }


    @GetMapping(value = "exportAllForConfigLocalServer/{areaId}")
    @Operation(summary = "گرفتن خروجی از کل دیتابیس")
    public void exportAllForConfigLocalServer(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        areaService.exportAllForConfigLocalServer(
                areaId, getId(request), response
        );
    }
}
