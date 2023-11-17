package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.AreaService;
import four.group.jahadi.Service.UserService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "api/region/user")
@Validated
public class RegionUserAPIRoutes extends Router {

    @Autowired
    private UserService userService;

    @Autowired
    private AreaService areaService;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<User>> getList(HttpServletRequest request) {
        return userService.findGroupMembersByRegionOwner(
                getId(request), getGroup(request)
        );
    }

    @PutMapping(value = "addMembers/{tripId}")
    @ResponseBody
    public void addMembers(HttpServletRequest request,
                           @PathVariable @ObjectIdConstraint ObjectId tripId,
                           @RequestBody List<ObjectId> userIds
    ) {
        areaService.addMembers(
                getId(request), getGroup(request), tripId, userIds
        );
    }

    @DeleteMapping(value = "removeMember/{tripId}/{userId}")
    @ResponseBody
    public void removeMember(HttpServletRequest request,
                             @PathVariable @ObjectIdConstraint ObjectId tripId,
                             @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        areaService.removeMember(
                getId(request), tripId, userId
        );
    }
}
