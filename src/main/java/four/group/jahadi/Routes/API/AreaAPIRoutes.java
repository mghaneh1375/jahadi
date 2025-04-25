package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.Area.AreaData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(value = "/api/area")
@Validated
public class AreaAPIRoutes extends Router {

    @Autowired
    private AreaService areaService;

    @PostMapping(value = "store/{tripId}")
    @ResponseBody
    public ResponseEntity<List<Area>> store(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId tripId,
            @RequestBody @Valid @Size(min = 1) ValidList<AreaData> areas
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return areaService.store(areas, user.getAccesses().contains(Access.ADMIN), tripId, user.getGroupId());
    }

    @DeleteMapping(value = "removeAreaFromTrip/{tripId}/{areaId}")
    @Operation(summary = "حذف منطقه از اردو توسط مسئول گروهی که دسترسی write برای آن اردو داشته باشد")
    public void removeAreaFromTrip(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId tripId,
            @PathVariable @ObjectIdConstraint ObjectId areaId
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        areaService.removeAreaFromTrip(
                tripId, areaId, getTokenInfo(request).getGroupId(),
                user.getId(), user.getNid()
        );
    }

}
