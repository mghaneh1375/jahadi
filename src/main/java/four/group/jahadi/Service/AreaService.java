package four.group.jahadi.Service;

import four.group.jahadi.DTO.AreaData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService extends AbstractService<Area, AreaData> {

    @Autowired
    private TripRepository tripRepository;

    @Override
    public ResponseEntity<List<Area>> list(Object... filters) {
        return null;
    }

    @Override
    public void update(ObjectId id, AreaData dto, Object... params) {

    }

    @Override
    public ResponseEntity<Area> store(AreaData dto, Object... params) {

        boolean hasAdminAccess = (boolean) params[0];
        ObjectId tripId = (ObjectId) params[1];

        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);

        if(!hasAdminAccess && !trip.getOwner().equals(params[1]))
            throw new NotAccessException();

        Area area = dto.convertToArea();
        trip.getAreas().add(area);
        tripRepository.save(trip);

        return new ResponseEntity<>(area, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Area> findById(ObjectId id, Object... params) {
        return null;
    }
}
