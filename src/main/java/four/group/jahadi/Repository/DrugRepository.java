package four.group.jahadi.Repository;

import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugJoinModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DrugRepository extends MongoRepository<Drug, ObjectId>, FilterableRepository<Drug> {

    @Query(value = "{_id: ?0, groupId: ?1}")
    Optional<Drug> findByIdAndGroupId(ObjectId id, ObjectId groupId);

    @Query(value = "{_id: {$in: ?0}, groupId: ?1}")
    List<Drug> findAllByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Aggregation(pipeline = {
            "{ $match:  {$and :[{$or: [{'deleted_at': null}, {'deleted_at': {$exists: false}}]}, { 'group_id': [0] } ]}}",
            "{$lookup: {from: 'group', localField: 'group_id', foreignField: '_id', as: 'groupInfo'}}",
            "{$unwind: '$groupInfo'}"
    })
    List<DrugJoinModel> findAllByGroupId(ObjectId groupId);

    @Aggregation(pipeline = {
            "{ $match:  {$and :[{$or: [{'deleted_at': null}, {'deleted_at': {$exists: false}}]} ]}}",
            "{$lookup: {from: 'group', localField: 'group_id', foreignField: '_id', as: 'groupInfo'}}",
            "{$unwind: '$groupInfo'}"
    })
    List<DrugJoinModel> findAllJoinByGroup();

    @Query(value = "{ _id: {$in: ?0}}", fields = "{ 'name': 1, 'howToUse': 1, 'description': 1 }")
    List<Drug> findByIds(List<ObjectId> ids);

    @Query(value = "{ _id: {$in: ?0}}")
    List<Drug> findFullInfoByIds(List<ObjectId> ids);

    @Query(value = "{ _id: {$in: ?0}}", count = true)
    Integer countByIds(List<ObjectId> ids);

    @Query(value = "{ code: {$in: ?0}, groupId: ?1}")
    List<Drug> findIdsByCodes(Set<String> codes, ObjectId groupId);

    @Query(value = "{ code: { $exists: false } }")
    List<Drug> findAllByCodeNotExist();


    @Aggregation(pipeline = {
            "{$match: {$expr: {$and: [{'group_id': ?1}, {$eq: [{$type: '$deleted_at'}, 'missing']}, {$regexMatch: { input: '$name', regex: ?2, options:'i'}}]} }}",
            "{$lookup: {from: 'drugs_in_area', localField: '_id', foreignField: 'drug_id', as: 'areaDrugInfo', pipeline: [{$match: { " +
                    "'area_id': ?0" +
                    "}}]}}",
            "{$unset: 'areaDrugInfo.area_id'}",
            "{$unset: 'areaDrugInfo.drug_id'}",
            "{$unset: 'areaDrugInfo.drug_name'}",
            "{$unset: 'price'}",
            "{$unset: 'available'}",
            "{$unset: 'available_pack'}",
            "{$unset: 'location'}",
            "{$unset: 'box_no'}",
            "{$unset: 'shelf_no'}",
            "{$unset: 'created_at'}",
            "{$unset: 'user_id'}",
            "{$unset: 'group_id'}",
            "{$unwind: '$areaDrugInfo'}",
            "{$project: {'_id': '$areaDrugInfo._id', 'reminder': '$areaDrugInfo.reminder', 'total_count': '$areaDrugInfo.total_count', 'drugInfo._id': '$_id', 'drugInfo.name': '$name', 'drugInfo.expire_at': '$expire_at', 'drugInfo.drug_type': '$drug_type', 'drugInfo.dose': '$dose', 'drugInfo.code': '$code', 'drugInfo.producer': '$producer'}}"
    })
    List<JoinedAreaDrugs> searchInAreaByName(
            ObjectId areaId, ObjectId groupId, String name
    );
}
