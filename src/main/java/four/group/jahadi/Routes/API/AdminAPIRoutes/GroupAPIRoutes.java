package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Service.GroupService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/group")
@Validated
public class GroupAPIRoutes {

    @Autowired
    GroupService groupService;

    @GetMapping(value = "list")
    @ResponseBody
    public String list(@RequestParam(required = false, value = "name") String name) {
        return groupService.list(name);
    }

    @PostMapping(value = "store")
    @ResponseBody
    public String store(final @RequestBody @Valid GroupData groupData) {
        return groupService.store(groupData);
    }


}
