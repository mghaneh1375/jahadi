package four.group.jahadi.Repository;

import four.group.jahadi.Models.WareHouseAccessForGroup;
import four.group.jahadi.Models.WareHouseAccessForGroupJoinWithUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WareHouseAccessForGroupRepository extends
        MongoRepository<WareHouseAccessForGroup, ObjectId>, FilterableRepository<WareHouseAccessForGroup> {

    @Query(value = "{groupId: ?0, hasAccessForDrug: true, userId: ?1}", exists = true)
    boolean existsDrugAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0, hasAccessForEquipment: true, userId: ?1}", exists = true)
    boolean existsEquipmentAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0, userId: ?1}")
    Optional<WareHouseAccessForGroup> findAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0, userId: ?1}", delete = true)
    void removeAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Aggregation(pipeline = {
            "{$match: {groupId: ?0}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'has_access_for_equipment': 1, 'has_access_for_drug': 1, 'created_at': 1}}",
    })
    List<WareHouseAccessForGroupJoinWithUser> findByGroupId(ObjectId groupId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{groupId: ?0}, {userId: ?1}]}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'has_access_for_equipment': 1, 'has_access_for_drug': 1, 'created_at': 1}}",
    })
    List<WareHouseAccessForGroupJoinWithUser> findAggregateByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

}
