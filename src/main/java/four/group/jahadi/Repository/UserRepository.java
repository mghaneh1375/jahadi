package four.group.jahadi.Repository;

import four.group.jahadi.DTO.UserDigest;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.Area.impl.GenderCount;
import four.group.jahadi.Repository.Area.impl.GroupStatisticsResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>, FilterableRepository<User> {

    @Query(value = "{'_id':  ?0, 'status':  'ACTIVE', 'removeAt': null}", count = true)
    Integer countActiveBy_id(ObjectId id);

    @Query(value = "{'accesses': ?0, 'status':  'ACTIVE', 'removeAt': null}", count = true)
    Integer countUsersByAccess(String access);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'university': 1, 'pic': 1, 'color': 1, 'sex': 1  }"
    )
    List<User> findByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }")
    List<User> findFullInfoByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'pic': 1, 'color': 1 }"
    )
    List<User> findDigestByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'name': 1 }")
    List<User> findJustNameByIdsIn(List<ObjectId> ids);

    @Query(value = "{ 'groupId': ?0, 'status': 'ACTIVE' }", fields = "{ 'name': 1 }")
    List<UserDigest> findGroupActiveMembersName(ObjectId groupId);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'pic': 1, 'color': 1, 'sex': 1, 'status': 1, 'lodgment': 1 }"
    )
    List<User> findGroupUsersByIdsIn(List<ObjectId> ids);

    @Query(value = "{ $and: [{'_id': { $in: ?0 }}, {'groupId': ?1}] }", count = true)
    Integer countByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ 'groupId': ?0, 'removeAt': null }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @Query(value = "{'nid':  ?0}")
    Optional<User> findByNID(String nid);

    @Query(value = "{'_id':  ?0}", fields = "{'name': 1, '_id': 1, 'accesses': 1, 'pic': 1, 'oldTripsCount': 1, 'groupId': 1}")
    Optional<User> findDigestById(ObjectId id);

    @Query(value = "{'phone':  ?0}", count = true)
    Integer countByPhone(String phone);

    @Query(value = "{'phone':  ?0, 'status': 'ACTIVE'}", count = true)
    Integer countActivesByPhone(String phone);

    @Query(value = "{'phone':  ?0}")
    Optional<User> findByPhone(String phone);

    @Query(value = "{'nid':  ?0}", count = true)
    Integer countByNID(String nid);

    @Query(value = "{'nid':  ?0}", fields = "{_id: 1}")
    Optional<User> findIdByNID(String nid);

    @Query(value = "{$and: [{'groupId': ?0}, {'accesses': 'GROUP'}]}", fields = "{_id: 1}")
    User findIdByGroupOwnerId(ObjectId groupId);

    @Aggregation(pipeline = {
            "{ $match: { groupId: ?0 } }",

            "{ $group: { " +
                    "_id: '$sex', " +
                    "count: { $sum: 1 } " +
                    "} }",

            "{ $project: { " +
                    "count: 1, " +
                    "_id: 1 " +
                    "} }"
    })
    List<GenderCount> getGroupMemberCountByGender(ObjectId groupId);

    @Aggregation(pipeline = {
            "{ $match: { groupId: ?0 } }",
            "{ $match: { " +
                    "birth_day: { $type: 'string', $ne: '' } " +
                    "} }",

            "{ $addFields: { " +
                    "birthYear: { " +
                    "$toInt: { " +
                    "$arrayElemAt: [ " +
                    "{ $split: ['$birth_day', '/'] }, " +
                    "0 " +
                    "] " +
                    "} " +
                    "} " +
                    "} }",

            "{ $addFields: { " +
                    "oldTrips: { " +
                    "$convert: { " +
                    "input: '$old_trips_count', " +
                    "to: 'int', " +
                    "onError: 0, " +
                    "onNull: 0 " +
                    "} " +
                    "} " +
                    "} }",

            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { userId: '$_id' }, " +
                    "pipeline: [ " +
                    "{ $unwind: '$areas' }, " +
                    "{ $match: { $expr: { $in: ['$$userId', '$areas.members'] } } } " +
                    "], " +
                    "as: 'userTrips' " +
                    "} }",

            "{ $addFields: { " +
                    "userTripsCount: { " +
                    "$size: { " +
                    "$ifNull: ['$userTrips', []] " +
                    "} " +
                    "} " +
                    "} }",

            "{ $addFields: { " +
                    "totalTripsCount: { " +
                    "$add: [ " +
                    "{ $ifNull: ['$oldTrips', 0] }, " +
                    "{ $ifNull: ['$userTripsCount', 0] } " +
                    "] " +
                    "} " +
                    "} }",

            "{ $facet: { " +
                    "byFieldOfStudy: [ " +
                    "{ $group: { _id: { $ifNull: ['$field', 'نامشخص'] }, count: { $sum: 1 } } }, " +
                    "{ $project: { fieldOfStudy: '$_id', count: 1, _id: 0 } }, " +
                    "{ $sort: { count: -1 } } " +
                    "], " +
                    "byBirthDecade: [ " +
                    "{ $group: { " +
                    "_id: { $multiply: [{ $floor: { $divide: ['$birthYear', 10] } }, 10] }, " +
                    "count: { $sum: 1 } " +
                    "} }, " +
                    "{ $project: { decade: '$_id', count: 1, _id: 0 } }, " +
                    "{ $sort: { decade: 1 } } " +
                    "], " +
                    "byTripBucket: [ " +
                    "{ $bucket: { " +
                    "groupBy: '$totalTripsCount', " +
                    "boundaries: [0, 1, 4, 11, 21, 31, 41, 51, 61, 71, 81, 91, 101], " +
                    "default: 'other', " +
                    "output: { count: { $sum: 1 } } " +
                    "} }, " +
                    "{ $addFields: { " +
                    "label: { " +
                    "$switch: { " +
                    "branches: [ " +
                    "{ case: { $eq: ['$_id', 0] },  then: '۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 1] },  then: '۳ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 4] },  then: '۱۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 11] }, then: '۲۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 21] }, then: '۳۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 31] }, then: '۴۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 41] }, then: '۵۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 51] }, then: '۶۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 61] }, then: '۷۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 71] }, then: '۸۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 81] }, then: '۹۰ دوره‌ای' }, " +
                    "{ case: { $eq: ['$_id', 91] }, then: '۱۰۰ دوره‌ای' } " +
                    "], " +
                    "default: 'بیش از ۱۰۰ دوره' " +
                    "} " +
                    "}, " +
                    "bucketStart: '$_id' " +
                    "} }, " +
                    "{ $sort: { bucketStart: 1 } }, " +
                    "{ $project: { _id: 0, label: 1, count: 1 } } " +
                    "]" +
                    "} }"
    })
    GroupStatisticsResult getGroupStatistics(ObjectId groupId);
}
