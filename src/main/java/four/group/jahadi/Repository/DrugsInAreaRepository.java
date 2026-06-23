package four.group.jahadi.Repository;

import four.group.jahadi.DTO.dashboard.PerProvinceData;
import four.group.jahadi.Models.Area.AreaDrugs;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrugsInAreaRepository extends MongoRepository<AreaDrugs, ObjectId>, FilterableRepository<AreaDrugs> {
  @Query(value = "{drugId: {$in: ?0}}", delete = true)
  void deleteAllByDrugIdIn(List<ObjectId> ids);

  @Query(value = "{drugId: {$in: ?0}}", count = true)
  Integer countByDrugIdIn(List<ObjectId> ids);

  @Aggregation(pipeline = {
          "{ $match: { created_at: { $gte: ?0, $lte: ?1 } } }",
          "{ $lookup: { " +
                  "from: 'trip', " +
                  "let: { areaId: '$area_id', createdAt: '$created_at' }, " +
                  "pipeline: [ " +
                  "{ $unwind: '$areas' }, " +
                  "{ $match: { $expr: { $and: [ " +
                  "{ $eq: ['$areas._id', '$$areaId'] }, " +
                  "] } } }, " +
                  "{ $project: { state: '$areas.state', _id: 0 } } " +
                  "], " +
                  "as: 'areaInfo' " +
                  "} }",
          "{ $unwind: { path: '$areaInfo', preserveNullAndEmptyArrays: false } }",
          "{ $group: { _id: '$areaInfo.state', uniquePatients: { $addToSet: '$drug_id' } } }",
          "{ $project: { province: '$_id', count: { $size: '$uniquePatients' }, _id: 0 } }"
  })
  List<PerProvinceData> countDrugsByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
