package four.group.jahadi.Repository;

import four.group.jahadi.Models.Area.AreaDrugs;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugsInAreaRepository extends MongoRepository<AreaDrugs, ObjectId>, FilterableRepository<AreaDrugs> {
  @Query(value = "{drugId: {$in: ?0}}", delete = true)
  void deleteAllByDrugIdIn(List<ObjectId> ids);

  @Query(value = "{drugId: {$in: ?0}}", count = true)
  Integer countByDrugIdIn(List<ObjectId> ids);
}
