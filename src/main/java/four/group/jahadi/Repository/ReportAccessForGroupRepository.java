package four.group.jahadi.Repository;

import four.group.jahadi.Models.ReportAccessForGroup;
import four.group.jahadi.Models.ReportAccessJoinWithUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAccessForGroupRepository extends
        MongoRepository<ReportAccessForGroup, ObjectId>, FilterableRepository<ReportAccessForGroup> {

    @Query(value = "{groupId: ?0, userId: ?1}", exists = true)
    boolean existsAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0, userId: ?1}", delete = true)
    void revokeAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0}")
    List<ReportAccessForGroup> findAccessesByGroupId(ObjectId groupId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{groupId: ?0}, {userId: ?1}]}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'created_at': 1}}",
    })
    List<ReportAccessJoinWithUser> findAggregateByGroupIdAndUserId(ObjectId groupId, ObjectId userId);
}
