package four.group.jahadi.Repository;

import four.group.jahadi.Models.EquipmentLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentLogRepository extends MongoRepository<EquipmentLog, ObjectId>, FilterableRepository<EquipmentLog> {

  @Query(value = "{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { 'equipment_id': [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'created_at': { $gte: [1] } } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'created_at': { $lte: [2] } } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'amount': { $gt: [3] } } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'amount': { $lt: [4] } } },"
            + "?#{ [5] == null ? { $where : 'true'} : { 'user_id': [5] } },"
            + "?#{ [6] == null ? { $where : 'true'} : { 'area_id': [6] } },"
            + "]}"
)
  List<EquipmentLog> findByFilters(
          ObjectId equipmentId,
          Long startAt, Long endAt,
          Boolean justPositives, Boolean justNegatives,
          ObjectId userId, ObjectId areaId
  );

}
