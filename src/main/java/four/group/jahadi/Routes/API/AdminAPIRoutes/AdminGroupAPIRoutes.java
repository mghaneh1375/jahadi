package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Service.GroupService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Group> store(final @RequestBody @Valid GroupData groupData) {
        return groupService.store(groupData);
    }

    @PutMapping(value = "toggleActivityStatus/{groupId}")
    @ResponseBody
    public void toggleActivityStatus(final @PathVariable @ObjectIdConstraint ObjectId groupId,
                                     final @RequestBody @NotBlank String password) {
        groupService.toggleActivityStatus(groupId, password);
    }


    @PutMapping(value = "setNewOwner/{groupId}/{userId}")
    @ResponseBody
    public void setNewOwner(final @PathVariable @ObjectIdConstraint ObjectId groupId,
                            final @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        groupService.setNewOwner(groupId, userId);
    }

}
