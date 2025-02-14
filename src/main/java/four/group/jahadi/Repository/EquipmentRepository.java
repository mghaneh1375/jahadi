package four.group.jahadi.Repository;

import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Models.Equipment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, ObjectId>, FilterableRepository<Equipment> {
    @Query(value = "{_id: ?0, groupId: ?1}")
    Optional<Equipment> findByIdAndGroupId(ObjectId id, ObjectId groupId);

    @Query(value = "{_id: {$in: ?0}, groupId: ?1}")
    List<Equipment> findAllByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ _id: {$in: ?0}}", fields = "{ '_id': 1 }")
    List<Equipment> findByIds(List<ObjectId> ids);

    @Query(value = "{$and :["
            + "{$or: [{'deletedAt': null}, {'deletedAt': {$exists: false}}]},"
            + "?#{ [0] == null ? { $where : 'true'} : { 'group_id' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'name': { $regex: [1], $options:'i'} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'available' : {$gte: [2]} } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'available' : {$lte: [3]} } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'health_status' : [4] } },"
            + "?#{ [5] == null ? { $where : 'true'} : { 'property_id' : [5] } },"
            + "?#{ [6] == null ? { $where : 'true'} : { 'location': { $regex: [6], $options:'i'} } },"
            + "?#{ [7] == null ? { $where : 'true'} : { 'equipment_type': { $regex: [7], $options:'i'} } },"
            + "?#{ [8] == null ? { $where : 'true'} : { 'row_no' : [8] } },"
            + "?#{ [9] == null ? { $where : 'true'} : { 'shelf_no' : [9] } },"
            + "?#{ [10] == null ? { $where : 'true'} : { 'buy_at' : {$gte: [10]} } },"
            + "?#{ [11] == null ? { $where : 'true'} : { 'buy_at' : {$lte: [11]} } },"
            + "?#{ [12] == null ? { $where : 'true'} : { 'guarantee_expire_at' : {$gte: [12]} } },"
            + "?#{ [13] == null ? { $where : 'true'} : { 'guarantee_expire_at' : {$lte: [13]} } },"
            + "]}", fields = "{userId: 0, groupId: 0}")
    List<Equipment> findByFilters(
            ObjectId groupId, String name,
            Integer minAvailable, Integer maxAvailable,
            EquipmentHealthStatus healthStatus,
            String propertyId, String location,
            EquipmentType equipmentType,
            String rowNo, String shelfNo,
            Date fromBuyAt, Date toBuyAt,
            Date fromGuaranteeAt, Date toGuaranteeAt
    );

}
