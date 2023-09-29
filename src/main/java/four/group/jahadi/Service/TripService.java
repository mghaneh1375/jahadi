package four.group.jahadi.Service;

import four.group.jahadi.DTO.TripData;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TripService extends AbstractService<Trip, TripData> {

    @Override
    public ResponseEntity<List<Trip>> list(Object ... filters) {
        return null;
    }

    @Override
    String update(ObjectId id, TripData dto, Object ... params) {
        return null;
    }

    @Override
    String store(TripData dto, Object ... params) {
        return null;
    }

    @Override
    public ResponseEntity<Trip> findById(ObjectId id, Object... params) {
        return null;
    }

    @Override
    Trip populateEntity(Trip trip, TripData tripData) {
        return null;
    }

}
