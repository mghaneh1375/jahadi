package four.group.jahadi.Repository;

import four.group.jahadi.Models.DrugBookmark;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugBookmarkRepository extends MongoRepository<DrugBookmark, ObjectId>, FilterableRepository<DrugBookmark> {
  @Query(value = "{userId: ?0}")
  List<DrugBookmark> findByUserId(ObjectId userId);
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
