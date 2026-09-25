package four.group.jahadi.Repository;

import four.group.jahadi.DTO.UserAccessInGroupFacetDto;
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

    @Query(value = "{groupId: ?0, userId: ?1}", exists = true)
    boolean existsAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0}")
    List<WareHouseAccessForGroup> findAccessByGroupId(ObjectId groupId);

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

    @Query(value = "{groupId: {$in: ?0}}")
    List<WareHouseAccessForGroup> findByGroupIds(List<ObjectId> groupIds);

    @Aggregation(pipeline = {
            "{$match: {$and: [{groupId: ?0}, {userId: ?1}]}}",
            "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userObj'}}",
            "{$unwind: '$userObj'}",
            "{$project: {'user': '$userObj', 'has_access_for_equipment': 1, 'has_access_for_drug': 1, 'created_at': 1}}",
    })
    List<WareHouseAccessForGroupJoinWithUser> findAggregateByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Aggregation(pipeline = {
            "{ $match: { group_id: ?0 } }",

            "{ $project: { " +
                    "user_id: '$user_id', " +
                    "drugAccess: { $ifNull: ['$has_access_for_drug', false] }, " +
                    "equipmentAccess: { $ifNull: ['$has_access_for_equipment', false] }, " +
                    "externalReferralAccess: { $literal: false }, " +
                    "reportAccess: { $literal: false }, " +
                    "createdAt: '$created_at' " +
                    "} }",

            "{ $unionWith: { " +
                    "coll: 'external_referral_access_for_group', " +
                    "pipeline: [" +
                    "{ $match: { group_id: ?0 } }, " +
                    "{ $project: { " +
                    "user_id: '$user_id', " +
                    "drugAccess: { $literal: false }, " +
                    "equipmentAccess: { $literal: false }, " +
                    "reportAccess: { $literal: false }, " +
                    "externalReferralAccess: { $literal: true }, " +
                    "createdAt: '$created_at' " +
                    "} }" +
                    "] " +
                    "} }",

            "{ $unionWith: { " +
                    "coll: 'report_access_for_group', " +
                    "pipeline: [" +
                    "{ $match: { group_id: ?0 } }, " +
                    "{ $project: { " +
                    "user_id: '$user_id', " +
                    "drugAccess: { $literal: false }, " +
                    "equipmentAccess: { $literal: false }, " +
                    "externalReferralAccess: { $literal: false }, " +
                    "reportAccess: { $literal: true }, " +
                    "createdAt: '$created_at' " +
                    "} }" +
                    "] " +
                    "} }",

            "{ $group: { " +
                    "_id: '$user_id', " +
                    "drugAccess: { $max: '$drugAccess' }, " +
                    "equipmentAccess: { $max: '$equipmentAccess' }, " +
                    "externalReferralAccess: { $max: '$externalReferralAccess' }, " +
                    "reportAccess: { $max: '$reportAccess' }, " +
                    "createdAt: { $max: '$createdAt' } " +
                    "} }",

            "{ $match: { $or: [ " +
                    "{ drugAccess: true }, " +
                    "{ equipmentAccess: true }, " +
                    "{ externalReferralAccess: true }, " +
                    "{ reportAccess: true } " +
                    "] } }",

            "{ $lookup: { " +
                    "from: 'user', " +
                    "localField: '_id', " +
                    "foreignField: '_id', " +
                    "as: 'user' " +
                    "} }",

            "{ $unwind: '$user' }",

            "{ $project: { " +
                    "_id: 0, " +
                    "user: '$user', " +
                    "drugAccess: '$drugAccess', " +
                    "equipmentAccess: '$equipmentAccess', " +
                    "externalReferralAccess: '$externalReferralAccess', " +
                    "reportAccess: '$reportAccess', " +
                    "createdAt: '$createdAt' " +
                    "} }",

            "{ $facet: { " +
                    "content: [ " +
                    "{ $sort: { createdAt: -1 } }, " +
                    "{ $skip: ?1 }, " +
                    "{ $limit: ?2 }, " +
                    "{ $project: { " +
                    "user: 1, " +
                    "drugAccess: 1, " +
                    "equipmentAccess: 1, " +
                    "externalReferralAccess: 1, " +
                    "reportAccess: 1 " +
                    "} }" +
                    "], " +
                    "totalElements: [ " +
                    "{ $count: 'count' }" +
                    "] " +
                    "} }"
    })
    List<UserAccessInGroupFacetDto> findUserAccessesByGroupIdPaged(
            ObjectId groupId,
            long skip,
            long limit
    );


}
