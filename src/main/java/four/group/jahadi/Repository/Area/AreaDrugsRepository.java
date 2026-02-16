package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AreaDrugsRepository extends MongoRepository<AreaDrugs, ObjectId>, FilterableRepository<AreaDrugs> {

    @Query(value = "{areaId: ?0}", count = true)
    Long countByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, drugId: ?1}", exists = true)
    Boolean existByAreaIdAndDrugId(ObjectId areaId, ObjectId drugId);

    @Query(value = "{areaId: ?0, drugId: ?1}")
    Optional<AreaDrugs> findByAreaIdAndDrugId(ObjectId areaId, ObjectId drugId);

    @Query(value = "{areaId: ?0}")
    List<AreaDrugs> findByAreaId(ObjectId areaId);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo'}}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaId(ObjectId areaId, Integer skip, Integer limit);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo', pipeline: [{$match: {$expr: {$and: [" +
                    "?#{ [3] == null ? { } : { $regexMatch: { input: '$name', regex: [3], options:'i'} } }," +
                    "?#{ [4] == null ? { } : { $regexMatch: { input: '$drug_type', regex: [4], options:'i'} } }," +
            "]}}}]}}",
            "{$skip:  ?1}",
            "{$limit:  ?2}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaId(
            ObjectId areaId,
            Integer limit, Integer skip,
            String name, String drugType
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo', pipeline: [{$match: {$expr: {$and: [" +
                    "?#{ [3] == null ? { } : { $regexMatch: { input: '$name', regex: [3], options:'i'} } }," +
                    "?#{ [4] == null ? { } : { $regexMatch: { input: '$drug_type', regex: [4], options:'i'} } }," +
                    "{$lte: [ '$expire_at', { $ifNull: [ ?5, Infinity ] } ]}," +
                    "]}}}]}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaIdEndExpireAt(
            ObjectId areaId, Integer skip, Integer limit,
            String name, String drugType,
            LocalDateTime endExpireAt
    );
    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo', pipeline: [{$match: {$expr: {$and: [" +
                    "?#{ [3] == null ? { } : { $regexMatch: { input: '$name', regex: [3], options:'i'} } }," +
                    "?#{ [4] == null ? { } : { $regexMatch: { input: '$drug_type', regex: [4], options:'i'} } }," +
                    "{$gte: [ '$expire_at', { $ifNull: [ ?5, -Infinity ] } ]}," +
                    "]}}}]}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaIdStartExpireAt(
            ObjectId areaId, Integer skip, Integer limit,
            String name, String drugType,
            LocalDateTime fromExpireAt
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo', pipeline: [{$match: {$expr: {$and: [" +
                    "?#{ [3] == null ? { } : { $regexMatch: { input: '$name', regex: [3], options:'i'} } }," +
                    "?#{ [4] == null ? { } : { $regexMatch: { input: '$drug_type', regex: [4], options:'i'} } }," +
                    "{$gte: [ '$expire_at', { $ifNull: [ ?5, -Infinity ] } ]}," +
                    "{$lte: [ '$expire_at', { $ifNull: [ ?6, Infinity ] } ]}," +
                    "]}}}]}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaId(
            ObjectId areaId, Integer skip, Integer limit,
            String name, String drugType,
            LocalDateTime fromExpireAt, LocalDateTime endExpireAt
    );

    @Query(value = "{areaId: ?0, drugId: {$in: ?1}}", fields = "{drugId: 1}")
    List<AreaDrugs> findDrugIdsByAreaIdAndDrugIds(ObjectId areaId, List<ObjectId> ids);

    @Query(value = "{areaId: ?0, drugId: {$in: ?1}}")
    List<AreaDrugs> findByAreaIdAndDrugIds(ObjectId areaId, List<ObjectId> ids);

    @Query(value = "{areaId: ?0, reminder: {$gt: 0}}")
    List<AreaDrugs> findAvailableDrugsByAreaId(ObjectId areaId);

    @Query(value = "{_id: {$in: ?0}, areaId: ?1}", delete = true)
    List<AreaDrugs> removeAreaDrugsByIdAndAreaId(List<ObjectId> ids, ObjectId areaId);
}
