package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Models.Group;
import four.group.jahadi.Service.GroupService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/group")
@Validated
public class AdminGroupAPIRoutes {

    @Autowired
    GroupService groupService;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Group>> list(@RequestParam(required = false, value = "name") String name) {
        return groupService.list(name);
    }

    @GetMapping(value = "groupStatisticData/{groupId}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statisticData(
            @PathVariable @ObjectIdConstraint ObjectId groupId
    ) {
        return groupService.statisticData(groupId);
    }
}
