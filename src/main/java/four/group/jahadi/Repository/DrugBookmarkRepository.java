package four.group.jahadi.Repository;

import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Models.EquipmentLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "DrugBookmark")
public interface DrugBookmarkRepository extends MongoRepository<DrugBookmark, ObjectId> {
  @Query(value = "{userId: ?0}")
  List<DrugBookmark> findByUserId(ObjectId userId);
  @Query(value = "{drugId: {$in: ?0}}")
  List<DrugBookmark> findByDrugIds(List<ObjectId> drugIds);

  @Query(value = "{userId: ?0, drugId: ?1}", delete = true)
  void removeByUserIdAndDrugId(ObjectId userId, ObjectId drugId);
}
