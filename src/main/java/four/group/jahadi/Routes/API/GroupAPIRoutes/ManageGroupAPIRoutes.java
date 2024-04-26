package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;

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

    @PostMapping(value = "setPic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "ست کردن تصویر لوگو گروه توسط مسئول آن گروه")
    public void setPic(
            HttpServletRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        groupService.setPic(getGroup(request), file);
    }

    @GetMapping(value = "statisticData")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statisticData(HttpServletRequest request) {
        return groupService.statisticData(getGroup(request));
    }

}
