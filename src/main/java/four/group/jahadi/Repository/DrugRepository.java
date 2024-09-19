package four.group.jahadi.Repository;

import four.group.jahadi.Enums.Drug.DrugLocation;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Models.Drug;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends MongoRepository<Drug, ObjectId>, FilterableRepository<Drug> {

    @Query(value = "{_id: ?0, userId: ?1}")
    Optional<Drug> findByIdAndUserId(ObjectId id, ObjectId userId);

    @Query(value = "{_id: {$in: ?0}, userId: ?1}")
    List<Drug> findAllByIdsAndUserId(List<ObjectId> ids, ObjectId userId);

    @Query(value = "{ _id: {$in: ?0}}", fields = "{ 'name': 1, 'howToUse': 1, 'description': 1 }")
    List<Drug> findByIds(List<ObjectId> ids);

    @Query(value = "{ _id: {$in: ?0}}", count = true)
    Integer countByIds(List<ObjectId> ids);

    @Query(value = "{ name: { $regex: ?0, $options:'i'}}", fields = "{ 'name': 1, '_id': 1, 'price': 1, 'visibility': 1, 'available': 1, 'priority': 1 }", sort = "{'priority': 1}")
    List<Drug> findLikeName(String name);

    @Query(value = "{$and :[{$or: [{'deleted_at': null}, {'deleted_at': {$exists: false}}]},"
            + "?#{ [0] == null ? { $where : 'true'} : { 'user_id' : [0] } },"
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
            ObjectId userId, String name,
            Integer minAvailableCount, Integer maxAvailableCount,
            DrugLocation drugLocation, DrugType drugType,
            Date fromExpireAt, Date toExpireAt,
            String boxNo, String shelfNo
    );

    @Query(value = "{_id: {$exists: true}}", fields = "{ 'name': 1, '_id': 1, 'price': 1, 'visibility': 1, 'available': 1, 'priority': 1 }", sort = "{'priority': 1}")
    List<Drug> findAllDigests();

    @Query(value = "{ name: { $regex: ?0, $options:'i'}, visibility: true }", fields = "{ 'name': 1, '_id': 1 }", sort = "{'priority': 1}")
    List<Drug> findLikeNameAndVisible(String name);

    @Query(value = "{ visibility: true }", fields = "{ 'name': 1, '_id': 1 }", sort = "{'priority': 1}")
    List<Drug> findVisibleDigests();
}
