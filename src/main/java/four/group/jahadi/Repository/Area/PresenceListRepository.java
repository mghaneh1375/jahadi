package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.PresenceList;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceListRepository extends MongoRepository<PresenceList, ObjectId>, FilterableRepository<PresenceList> {

    @Query(value = "{area_id: ?0}")
    List<PresenceList> findByAreaId(ObjectId areaId);

    @Query(value = "{area_id: ?0}", delete = true)
    void deleteByAreaId(ObjectId areaId);

    @Aggregation(pipeline = {
            "{ $match: { area_id: ?0 } }",
            "{ $match: { user_id: { $in: ?1 } } }",
            "{ $sort: { entrance: -1 } }",

            "{ $group: { " +
                    "_id: '$user_id', " +
                    "lastRecord: { $first: '$$ROOT' } " +
                    "} }",

            "{ $match: { 'lastRecord.exit': null } }",

            "{ $project: { " +
                    "_id: 1 " +
                    "} }"
    })
    List<ObjectId> getLastPresenceByUsersAndAreaId(ObjectId areaId, List<ObjectId> userIds);

    @Aggregation(pipeline = {
            "{ $match: { area_id: ?0, user_id: ?1 } }",
            "{ $sort: { entrance: -1 } }",
            "{ $limit: 1 }"
    })
    Optional<PresenceList> getLastPresenceByUserAndAreaId(ObjectId areaId, ObjectId userId);
}
