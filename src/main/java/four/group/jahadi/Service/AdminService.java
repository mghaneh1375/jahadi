package four.group.jahadi.Service;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ProjectRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TripRepository tripRepository;

    public ResponseEntity<HashMap<String, Object>> statistic() {

        HashMap<String, Object> statisticData = new HashMap<>();
        List<Trip> trips;

        try {
            trips = tripRepository.findActivesOrNotStartedProjects(Utility.getCurrDate());
        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }

        List<User> users = userRepository.findByIdsIn(
                trips.stream().map(Trip::getAreas)
                        .map(areas -> areas.stream().map(Area::getOwnerId).collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList())
        );

        trips.forEach(trip -> trip.getAreas().forEach(area -> users.stream()
                .filter(user -> user.getId().equals(area.getOwnerId()))
                .findFirst().ifPresent(area::setOwner)));

        statisticData.put("members", userRepository.countUsersByAccess(Access.JAHADI.getName().toUpperCase()));
        statisticData.put("surgeries", 0);
        statisticData.put("postRef", 0);
        statisticData.put("totalCosts", 0);
        statisticData.put("totalRefs", 0);
//        statisticData.put("activeProjects", projectService.myProjectsNeedAction(groupId));
        statisticData.put("activeTrips", trips);

        return new ResponseEntity<>(statisticData, HttpStatus.OK);

    }

}
