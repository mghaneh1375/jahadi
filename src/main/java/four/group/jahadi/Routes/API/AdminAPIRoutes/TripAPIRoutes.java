package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Models.ActiveGroups;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.TripService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/trip")
@Validated
public class TripAPIRoutes extends Router {

    @Autowired
    private TripService tripService;

    @GetMapping(value = "getGroupsWhichHaveActiveTrip")
    @ResponseBody
    public ResponseEntity<List<ActiveGroups>> getGroupsWhichHaveActiveTrip() {
        return tripService.getGroupsWhichHaveActiveTrip();
    }

    @GetMapping(value = "getGroupsTrips/{groupId}")
    @ResponseBody
    public ResponseEntity<List<Trip>> getGroupsTrips(
            @PathVariable @ObjectIdConstraint ObjectId groupId
    ) {
        return tripService.getGroupsTrips(groupId);
    }

    @GetMapping(value = "getGroupsTripsWithPagination/{groupId}")
    @ResponseBody
    public ResponseEntity<Page<Trip>> getGroupsTrips(
            @PathVariable @ObjectIdConstraint ObjectId groupId,
            @RequestParam(value = "pageIndex") @NotNull @Min(0) @Max(10000) int pageIndex,
            @RequestParam(value = "pageSize") @NotNull @Min(5) @Max(100) int pageSize
    ) {
        return tripService.getGroupsTripsWithPagination(groupId, pageIndex, pageSize);
    }
}
