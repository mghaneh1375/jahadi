package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Models.Group;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/trip")
@Validated
public class TripAPIRoutes extends Router {

    @Autowired
    private TripService tripService;

    @GetMapping(value = "getGroupsWhichHasActiveTrip")
    public ResponseEntity<List<Group>> getGroupsWhichHasActiveTrip() {
        return tripService.getGroupsWhichHasActiveTrip();
    }
}
