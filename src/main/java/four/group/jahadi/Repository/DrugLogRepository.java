package four.group.jahadi.Repository;

import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Models.DrugLogJoinModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrugLogRepository extends MongoRepository<DrugLog, ObjectId>, FilterableRepository<DrugLog> {

  @Query(value = "{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { 'drug_id': [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'created_at': { $gte: [1] } } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'created_at': { $lte: [2] } } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'amount': { $gt: [3] } } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'amount': { $lt: [4] } } },"
            + "]}"
)
  List<DrugLog> findByFilters(ObjectId drugId, Long startAt, Long endAt, Boolean justPositives, Boolean justNegatives);

  @Query(value = "{groupId: null}")
  List<DrugLog> findByGroupIdIsNull();


  @Aggregation(pipeline = {
          "{ $match:  {$and :[" +
                  "?#{ [0] == null ? { '_id': {$exists: true}} : { 'drug_id': [0] } }," +
                  "?#{ [1] == null ? { '_id': {$exists: true}} : { 'user_id': [1] } }," +
                  "?#{ [2] == null ? { '_id': {$exists: true}} : { 'area_id': [2] } }," +
                  "?#{ [3] == null ? { '_id': {$exists: true}} : { 'created_at' : {$gte: [3]} } }," +
                  "?#{ [4] == null ? { '_id': {$exists: true}} : { 'created_at' : {$lte: [4]} } }," +
                  "?#{ [5] == null ? { '_id': {$exists: true}} : { 'group_id': [5] } }," +
                  "]}}",
          "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo'}}",
          "{$lookup: {from: 'user', localField: 'user_id', foreignField: '_id', as: 'userInfo'}}",
          "{$lookup: {from: 'group', localField: 'group_id', foreignField: '_id', as: 'groupInfo'}}",
          "{$unwind: '$drugInfo'}",
          "{$unwind: '$userInfo'}",
          "{$unwind: {path: '$groupInfo', preserveNullAndEmptyArrays: true}}",
          "{ $project: {" +
                  "'drugInfo.name': 1," +
                  "'drugInfo.dose': 1," +
                  "'drugInfo.producer': 1," +
                  "'drugInfo.drug_type': 1," +
                  "'drugInfo.location': 1," +
                  "'drugInfo.box_no': 1," +
                  "'drugInfo.shelf_no': 1," +
                  "'amount': 1, 'desc': 1, 'createdAt': 1, " +
                  "'username': '$userInfo.name', " +
                  "'group': '$groupInfo.name' " +
                  "} }"
  })
  List<DrugLogJoinModel> findWithJoin(
          ObjectId drugId, ObjectId userId, ObjectId areaId,
          LocalDateTime from, LocalDateTime end, ObjectId groupId
  );

  @Query(value = "{drugId: {$in: ?0}}", delete = true)
  void deleteAllByDrugIdIn(List<ObjectId> ids);
}
