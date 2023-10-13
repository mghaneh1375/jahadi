package four.group.jahadi.Repository;

import four.group.jahadi.DTO.Digest.GroupDigest;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId>, FilterableRepository<Trip> {

}
