package four.group.jahadi.Routes.API;

import four.group.jahadi.DTO.AreaData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Area;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/area")
@Validated
public class AreaAPIRoutes extends Router {

    @Autowired
    private AreaService areaService;

    @PostMapping(value = "store/{tripId}")
    @ResponseBody
    public ResponseEntity<Area> store(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId tripId,
            @RequestBody @Valid AreaData areaData
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return areaService.store(areaData, user.getAccesses().contains(Access.ADMIN), tripId, user.getGroupId());
    }

//    @PutMapping(value = "update/{tripId}")
//    public void update(
//            HttpServletRequest request,
//            @PathVariable @ObjectIdConstraint ObjectId tripId,
//            @RequestBody @Valid TripStep2Data tripStep2Data
//    ) throws UnAuthException, NotActivateAccountException {
//        User user = getUser(request);
//        areaService.update(tripId, tripStep2Data,
//                user.getAccesses().contains(Access.ADMIN), user.getId()
//        );
//    }

}
