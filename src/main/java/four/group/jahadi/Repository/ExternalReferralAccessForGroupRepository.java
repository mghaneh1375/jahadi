package four.group.jahadi.Repository;

import four.group.jahadi.Models.ExternalReferralAccessForGroup;
import four.group.jahadi.Models.ExternalReferralAccessJoinWithUser;
import four.group.jahadi.Models.WareHouseAccessForGroupJoinWithUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExternalReferralAccessForGroupRepository extends
        MongoRepository<ExternalReferralAccessForGroup, ObjectId>, FilterableRepository<ExternalReferralAccessForGroup> {

    @Query(value = "{groupId: ?0, userId: ?1}", exists = true)
    boolean existsAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{groupId: ?0}, {userId: ?1}]}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'created_at': 1}}",
    })
    List<ExternalReferralAccessJoinWithUser> findAggregateByGroupIdAndUserId(ObjectId groupId, ObjectId userId);
    @Aggregation(pipeline = {
            "{$match: {groupId: ?0}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'created_at': 1}}",
    })
    List<ExternalReferralAccessJoinWithUser> findByGroupId(ObjectId groupId);

    @Query(value = "{groupId: ?0, userId: ?1}", delete = true)
    void revokeAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);
}
