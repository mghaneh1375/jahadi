package four.group.jahadi.Repository;

import four.group.jahadi.Models.DrugLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "DrugLogs")
public interface DrugLogRepository extends MongoRepository<DrugLog, ObjectId> {

  @Query(value = "{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { 'drug_id': [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'created_at': { $gte: [1] } } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'created_at': { $lte: [2] } } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'amount': { $gt: [3] } } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'amount': { $lt: [4] } } },"
            + "]}"
)
  List<DrugLog> findByFilters(ObjectId drugId, Long startAt, Long endAt, Boolean justPositives, Boolean justNegatives);

}
