package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.TripService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/trip")
@Validated
public class TripAPIRoutes extends Router {

    @Autowired
    private TripService tripService;

    @GetMapping(value = "getGroupsWhichHasActiveTrip")
    @ResponseBody
    public ResponseEntity<List<Group>> getGroupsWhichHasActiveTrip() {
        return tripService.getGroupsWhichHasActiveTrip();
    }

    @GetMapping(value = "getGroupsTrips/{groupId}")
    @ResponseBody
    public ResponseEntity<List<Trip>> getGroupsTrips(
            @PathVariable @ObjectIdConstraint ObjectId groupId
    ) {
        return tripService.getGroupsTrips(groupId);
    }
}
