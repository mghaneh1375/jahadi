package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Service.GroupService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    public ResponseEntity<Page<Group>> list(
            @RequestParam(value = "pageIndex", required = false) @Min(0) @Max(1000) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) @Min(5) @Max(100) Integer pageSize,
            @RequestParam(required = false, value = "name") @Size(min = 2, max = 100) String name
    ) {
        return groupService.list(
                pageIndex == null ? 0 : pageIndex,
                pageSize == null ? Integer.MAX_VALUE : pageSize,
                name
        );
    }

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Group> store(final @RequestBody @Valid GroupData groupData) {
        return groupService.store(groupData);
    }

    @PutMapping(value = "toggleActivityStatus/{userId}")
    @ResponseBody
    public void toggleActivityStatus(final @PathVariable @ObjectIdConstraint ObjectId userId) {
        groupService.toggleActivityStatus(userId);
    }


    @PutMapping(value = "setNewOwner/{groupId}/{userId}")
    @ResponseBody
    public void setNewOwner(final @PathVariable @ObjectIdConstraint ObjectId groupId,
                            final @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        groupService.setNewOwner(groupId, userId);
    }

    @GetMapping(value = "groupStatisticData/{groupId}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> statisticData(
            @PathVariable @ObjectIdConstraint ObjectId groupId
    ) {
        return groupService.statisticData(groupId);
    }
}
