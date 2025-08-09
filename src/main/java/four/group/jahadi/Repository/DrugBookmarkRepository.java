package four.group.jahadi.Repository;

import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.Area.JoinedDrugBookmarkWithAreaDto;
import four.group.jahadi.Models.DrugBookmark;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugBookmarkRepository extends MongoRepository<DrugBookmark, ObjectId>, FilterableRepository<DrugBookmark> {
  @Query(value = "{userId: ?0}")
  List<DrugBookmark> findByUserId(ObjectId userId);


  @Aggregation(pipeline = {
          "{$match: {userId: ?0}}",
          "{$lookup: {from: 'drugs_in_area', " +
                  "let: { drugId: '$drug_id' }," +
                  "pipeline: [{" +
                  "$match: {" +
                  "$expr: {" +
                  "$and: [" +
                  "{ $eq: ['$drug_id', '$$drugId'] }," +
                  "{ $eq: ['$area_id', ?1] }" +
                  "]}}}]," +
                  "as: 'areaDrugInfo'}}",
          "{$unwind: '$areaDrugInfo'}",
          "{ $project: {" +
                  "'areaDrugId': '$areaDrugInfo._id'," +
                  "'drugName': 1, " +
                  "'drugId': 1, " +
                  "'amount': 1, " +
                  "'howToUse': 1, " +
                  "'amountOfUse': 1, " +
                  "'useTime': 1, " +
                  "} }"
  })
  List<JoinedDrugBookmarkWithAreaDto> findByUserIdAndAreaId(ObjectId userId, ObjectId areaId);

  @Query(value = "{drugId: {$in: ?0}}")
  List<DrugBookmark> findByDrugIds(List<ObjectId> drugIds);

  @Query(value = "{userId: ?0, drugId: ?1}", delete = true)
  void removeByUserIdAndDrugId(ObjectId userId, ObjectId drugId);

  @Query(value = "{userId: ?0, _id: ?1}", delete = true)
  void removeByUserIdAndId(ObjectId userId, ObjectId id);
  // use by reflection
  @Query(value = "{_id: {$in: ?0}}", delete = true)
  void deleteByIdsIn(List<ObjectId> ids);
}
