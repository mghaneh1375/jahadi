package four.group.jahadi.Repository;

import four.group.jahadi.Enums.Drug.DrugLocation;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugJoinModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            "{ $match:  {$and :[" +
                    "?#{ [0] == null ? { '_id': {$exists: true}} : { 'group_id': [0] } }," +
                    "]}}",
            "{$lookup: {from: 'group', localField: 'group_id', foreignField: '_id', as: 'groupInfo'}}",
            "{$unwind: '$groupInfo'}"
    })
    List<DrugJoinModel> findAllByGroupId(ObjectId groupId);

    @Query(value = "{ _id: {$in: ?0}}", fields = "{ 'name': 1, 'howToUse': 1, 'description': 1 }")
    List<Drug> findByIds(List<ObjectId> ids);

    @Query(value = "{ _id: {$in: ?0}}")
    List<Drug> findFullInfoByIds(List<ObjectId> ids);

    @Query(value = "{ _id: {$in: ?0}}", count = true)
    Integer countByIds(List<ObjectId> ids);

    @Query(value = "{$and :[{$or: [{'deleted_at': null}, {'deleted_at': {$exists: false}}]},"
            + "?#{ [0] == null ? { $where : 'true'} : { 'group_id' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { name: { $regex: [1], $options:'i'} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'available' : {$gte: [2]} } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'available' : {$lte: [3]} } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'location' : [4] } },"
            + "?#{ [5] == null ? { $where : 'true'} : { 'drug_type' : [5] } },"
            + "?#{ [6] == null ? { $where : 'true'} : { 'expire_at' : {$gte: [6]} } },"
            + "?#{ [7] == null ? { $where : 'true'} : { 'expire_at' : {$lte: [7]} } },"
            + "?#{ [8] == null ? { $where : 'true'} : { 'box_no' : [8] } },"
            + "?#{ [9] == null ? { $where : 'true'} : { 'shelf_no' : [9] } },"
            + "]}")
    List<Drug> findByFilters(
            ObjectId groupId, String name,
            Integer minAvailableCount, Integer maxAvailableCount,
            DrugLocation drugLocation, DrugType drugType,
            LocalDateTime fromExpireAt, LocalDateTime toExpireAt,
            String boxNo, String shelfNo
    );

    @Query(value = "{ code: {$in: ?0}, groupId: ?1}")
    List<Drug> findIdsByCodes(Set<String> codes, ObjectId groupId);

    @Query(value = "{ code: { $exists: false } }")
    List<Drug> findAllByCodeNotExist();
}
