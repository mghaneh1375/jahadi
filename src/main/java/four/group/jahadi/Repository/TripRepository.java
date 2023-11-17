package four.group.jahadi.Repository;

import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId>, FilterableRepository<Trip> {

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}]  }")
    List<Trip> findActives(Date curr);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesProjectIdsByAreaOwnerId(Date curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesByGroupId(Date curr, ObjectId groupId);

    @Query(value = "{$and: [{'startAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }")
    List<Trip> findNotStartedByAreaOwnerId(Date curr, ObjectId areaOwnerId);
}
