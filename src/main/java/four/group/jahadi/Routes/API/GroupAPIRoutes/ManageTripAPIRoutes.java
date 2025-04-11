package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Enums.Status;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "api/group/trip")
@Validated
public class ManageTripAPIRoutes extends Router {

    @Autowired
    private TripService tripService;

    @GetMapping(value = "myTrips")
    public ResponseEntity<List<Trip>> myTrips(
            HttpServletRequest request,
            @RequestParam(value = "status", required = false) Status status
    ) {
        return tripService.list(getGroup(request), status);
    }

    @GetMapping(value = "myActiveTrips")
    public ResponseEntity<List<Trip>> myActiveTrips(
            HttpServletRequest request
    ) {
        return tripService.myActiveTrips(getGroup(request));
    }
}
