package four.group.jahadi.Service;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.DTO.Trip.TripStep2Data;
import four.group.jahadi.DTO.Trip.TripStepData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.ProjectRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TripService extends AbstractService<Trip, TripStepData> {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ResponseEntity<List<Trip>> list(Object ... filters) {
        return null;
    }

    @Override
    public void update(ObjectId id, TripStepData data, Object... params) {

        Trip trip = tripRepository.findById(id).orElseThrow(InvalidIdException::new);

        boolean hasAdminAccess = (boolean) params[0];

        if(!hasAdminAccess && !trip.getOwner().equals(params[1]))
            throw new NotAccessException();

        TripStep2Data dto = (TripStep2Data) data;

        trip.setName(dto.getName());
        trip.setStartAt(new Date(dto.getStartAt()));
        trip.setEndAt(new Date(dto.getEndAt()));

        tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<Trip> store(TripStepData data, Object... params) {

        ObjectId projectId = (ObjectId) params[0];

        Project project = projectRepository.findById(projectId).orElseThrow(InvalidIdException::new);
        // todo : check for update policies

        TripStep1Data dto = (TripStep1Data) data;

        Trip trip = Trip
                .builder()
                .projectId(project.getId())
                .name(project.getName() + " - ").build();

        trip.setOwner(dto.getOwner());
        tripRepository.save(trip);

        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Trip> findById(ObjectId id, Object... params) {
        return null;
    }

}
