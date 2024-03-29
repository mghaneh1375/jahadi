package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.MembersServiceInArea;
import four.group.jahadi.Service.UserService;
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
@RequestMapping(value = "api/region/manage_user")
@Validated
public class RegionManageUserAPIRoutes extends Router {

    @Autowired
    private UserService userService;

    @Autowired
    private MembersServiceInArea membersServiceInArea;


    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن اعضای گروه توسط مسئول منطقه")
    public ResponseEntity<List<User>> getList(HttpServletRequest request) {
        return userService.findGroupMembersByRegionOwner(
                getId(request), getGroup(request)
        );
    }

    @GetMapping(value = "members/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن اعضای افزوده شده توسط مسئول منطقه")
    public ResponseEntity<List<User>> members(HttpServletRequest request,
                                              @PathVariable @ObjectIdConstraint ObjectId areaId) {
        return membersServiceInArea.members(getId(request), areaId);
    }

    @PutMapping(value = "addMembers/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن جهادگر به منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void addMembers(HttpServletRequest request,
                           @PathVariable @ObjectIdConstraint ObjectId areaId,
                           @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addMembers(
                getId(request), getGroup(request), areaId, userIds
        );
    }

    @DeleteMapping(value = "removeMember/{areaId}/{userId}")
    @ResponseBody
    @Operation(summary = "حذف جهادگر از منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void removeMember(HttpServletRequest request,
                             @PathVariable @ObjectIdConstraint ObjectId areaId,
                             @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeMember(
                getId(request), areaId, userId
        );
    }

    @PutMapping(value = "addDispatcher/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن جهادگر/جهادگران به عنوان نوبت ده در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void addDispatcher(HttpServletRequest request,
                              @PathVariable @ObjectIdConstraint ObjectId areaId,
                              @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addDispatchers(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removeDispatcher/{areaId}/{userId}")
    @ResponseBody
    @Operation(summary = "حذف جهادگر از نوبت دهی در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void removeDispatcher(HttpServletRequest request,
                                 @PathVariable @ObjectIdConstraint ObjectId areaId,
                                 @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeDispatcher(getId(request), areaId, userId);
    }
}
