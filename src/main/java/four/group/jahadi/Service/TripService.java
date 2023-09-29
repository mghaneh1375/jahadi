package four.group.jahadi.Service;

import four.group.jahadi.DTO.TripData;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TripService extends AbstractService<Trip, TripData> {

    @Override
    public String list(Object ... filters) {
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
    Trip populateEntity(Trip trip, TripData tripData) {
        return null;
    }

    @Override
    Trip findById(ObjectId id) {
        return null;
    }
}
