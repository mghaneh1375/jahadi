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
    public ResponseEntity<List<User>> members(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
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


    @GetMapping(value = "getDispatchers/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن مسئولین پذیرش در منطقه توسط مسئول منطفه")
    public ResponseEntity<List<User>> getDispatchers(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return membersServiceInArea.getDispatchers(getId(request), areaId);
    }

    @PutMapping(value = "addDispatcher/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن جهادگر/جهادگران به عنوان مسئول پذیرش در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void addDispatcher(HttpServletRequest request,
                              @PathVariable @ObjectIdConstraint ObjectId areaId,
                              @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addDispatchers(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removeDispatcher/{areaId}/{userId}")
    @ResponseBody
    @Operation(summary = "حذف جهادگر از مسئول پذیرشی در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void removeDispatcher(HttpServletRequest request,
                                 @PathVariable @ObjectIdConstraint ObjectId areaId,
                                 @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeDispatcher(getId(request), areaId, userId);
    }

    @PutMapping(value = "addInsurancer/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن جهادگر/جهادگران به عنوان مسئول بیمه در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void addInsurancer(HttpServletRequest request,
                              @PathVariable @ObjectIdConstraint ObjectId areaId,
                              @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addInsurancer(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removeInsurancer/{areaId}/{userId}")
    @ResponseBody
    @Operation(summary = "حذف جهادگر از مسئول بیمه در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void removeInsurancer(HttpServletRequest request,
                                 @PathVariable @ObjectIdConstraint ObjectId areaId,
                                 @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeInsurancer(getId(request), areaId, userId);
    }

    @GetMapping(value = "getInsurancers/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن مسئولین بیمه در منطقه توسط مسئول منطفه")
    public ResponseEntity<List<User>> getInsurancers(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return membersServiceInArea.getInsurancers(getId(request), areaId);
    }

    @PutMapping(value = "addPharmacyManager/{areaId}")
    @ResponseBody
    @Operation(
            summary = "افزودن جهادگر/جهادگران به عنوان مسئول داروخانه در منطقه توسط مسئول منطفه",
            description = "قبل از شروع اردو باید صدا زده شود"
    )
    public void addPharmacyManager(HttpServletRequest request,
                                   @PathVariable @ObjectIdConstraint ObjectId areaId,
                                   @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addPharmacyManager(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removePharmacyManager/{areaId}/{userId}")
    @ResponseBody
    @Operation(
            summary = "حذف جهادگر از مسئول داروخانه در منطقه توسط مسئول منطفه",
            description = "قبل از شروع اردو باید صدا زده شود"
    )
    public void removePharmacyManager(HttpServletRequest request,
                                      @PathVariable @ObjectIdConstraint ObjectId areaId,
                                      @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removePharmacyManager(getId(request), areaId, userId);
    }

    @GetMapping(value = "getPharmacyManagers/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن مسئولین داروخانه در منطقه توسط مسئول منطفه")
    public ResponseEntity<List<User>> getPharmacyManagers(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return membersServiceInArea.getPharmacyManagers(getId(request), areaId);
    }

    @PutMapping(value = "addLaboratoryManager/{areaId}")
    @ResponseBody
    @Operation(
            summary = "افزودن جهادگر/جهادگران به عنوان مسئول آزمایشگاه در منطقه توسط مسئول منطفه",
            description = "قبل از شروع اردو باید صدا زده شود"
    )
    public void addLaboratoryManager(HttpServletRequest request,
                                     @PathVariable @ObjectIdConstraint ObjectId areaId,
                                     @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addLaboratoryManager(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removeLaboratoryManager/{areaId}/{userId}")
    @ResponseBody
    @Operation(
            summary = "حذف جهادگر از مسئول آزمایشگاه در منطقه توسط مسئول منطفه",
            description = "قبل از شروع اردو باید صدا زده شود"
    )
    public void removeLaboratoryManager(HttpServletRequest request,
                                        @PathVariable @ObjectIdConstraint ObjectId areaId,
                                        @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeLaboratoryManager(getId(request), areaId, userId);
    }

    @GetMapping(value = "getLaboratoryManager/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن مسئولین آزمایشگاه در منطقه توسط مسئول منطفه")
    public ResponseEntity<List<User>> getLaboratoryManager(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return membersServiceInArea.getLaboratoryManagers(getId(request), areaId);
    }

    @PutMapping(value = "addTrainer/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن جهادگر/جهادگران به عنوان مسئول آموزش در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void addTrainer(HttpServletRequest request,
                           @PathVariable @ObjectIdConstraint ObjectId areaId,
                           @RequestBody List<ObjectId> userIds
    ) {
        membersServiceInArea.addTrainer(getId(request), areaId, userIds);
    }

    @DeleteMapping(value = "removeTrainer/{areaId}/{userId}")
    @ResponseBody
    @Operation(summary = "حذف جهادگر از مسئول آموزش در منطقه توسط مسئول منطفه", description = "قبل از شروع اردو باید صدا زده شود")
    public void removeTrainer(HttpServletRequest request,
                              @PathVariable @ObjectIdConstraint ObjectId areaId,
                              @PathVariable @ObjectIdConstraint ObjectId userId
    ) {
        membersServiceInArea.removeTrainer(getId(request), areaId, userId);
    }

    @GetMapping(value = "getTrainers/{areaId}")
    @ResponseBody
    @Operation(summary = "گرفتن مسئولین آموزش در منطقه توسط مسئول منطفه")
    public ResponseEntity<List<User>> getTrainers(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) {
        return membersServiceInArea.getTrainers(getId(request), areaId);
    }
}
