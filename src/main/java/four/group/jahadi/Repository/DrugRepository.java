package four.group.jahadi.Repository;

import four.group.jahadi.Models.Drug;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends MongoRepository<Drug, ObjectId>, FilterableRepository<Drug> {

  @Query(value = "{_id: {$in: ?0}}", fields = "{ 'name': 1, 'howToUse': 1, 'description': 1 }")
  List<Drug> findByIds(List<ObjectId> ids);
}
