package four.group.jahadi.Service;

import four.group.jahadi.DTO.TripData;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;

import java.util.List;

public class TripService extends AbstractService<Trip, TripData> {

    @Override
    PaginatedResponse<Trip> list(List<String> filters) {
        return null;
    }

    @Override
    String update(ObjectId id, TripData dto) {
        return null;
    }

    @Override
    String store(TripData dto) {
        return null;
    }

    @Override
    Trip populateEntity(Trip trip, TripData tripData) {
        return null;
    }

    @Override
    Trip findById(ObjectId id) {
        return null;
    }
}
