package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.ModuleInArea;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.ModuleServiceInArea;
import four.group.jahadi.Service.Area.UserServiceInArea;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/user")
@Validated
public class RegionUserAPIRoutes extends Router {

    @Autowired
    private ModuleServiceInArea moduleServiceInArea;

    @GetMapping(value = "modules/{areaId}")
    @ResponseBody
    @Operation(
            summary = "گرفتن ماژول ها در یک منطقه خاص برای یک جهادگر",
            description = "در صورتی که کاربر به یک ماژول خاص دسترسی داشته باشد فیلد دسترسی های متناظر با آن پر میشود"
    )
    public ResponseEntity<List<ModuleInArea>> modules(HttpServletRequest request,
                                                      @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return moduleServiceInArea.modules(getId(request), areaId);
    }

}
