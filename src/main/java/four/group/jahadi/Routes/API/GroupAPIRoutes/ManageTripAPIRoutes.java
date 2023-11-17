package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.Trip.TripStep2Data;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.Status;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.TripService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/group/trip")
@Validated
public class ManageTripAPIRoutes extends Router {

    @Autowired
    private TripService tripService;

    @PostMapping(value = "store/{projectId}")
    @ResponseBody
    public ResponseEntity<Trip> store(
            @PathVariable @ObjectIdConstraint ObjectId projectId,
            @RequestBody @Valid TripStep1Data tripStep1Data
    ) {
        return tripService.store(tripStep1Data, projectId);
    }

    @PutMapping(value = "update/{tripId}")
    public void update(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId tripId,
            @RequestBody @Valid TripStep2Data tripStep2Data
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        tripService.update(tripId, tripStep2Data,
                user.getAccesses().contains(Access.ADMIN), user.getGroupId()
        );
    }

    @GetMapping(value = "myTrips")
    public ResponseEntity<List<Trip>> myTrips(
            HttpServletRequest request,
            @RequestParam(value = "status", required = false) Status status
    ) {
        return tripService.list(getGroup(request), status);
    }

}
