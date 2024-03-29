package four.group.jahadi.Repository;

import four.group.jahadi.Models.Drug;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends MongoRepository<Drug, ObjectId>, FilterableRepository<Drug> {

  @Query(value = "{ _id: {$in: ?0}}", fields = "{ 'name': 1, 'howToUse': 1, 'description': 1 }")
  List<Drug> findByIds(List<ObjectId> ids);

  @Query(value = "{ _id: {$in: ?0}}", count=true)
  Integer countByIds(List<ObjectId> ids);
  
  @Query(value = "{ name: { $regex: ?0, $options:'i'}}", fields = "{ 'name': 1, '_id': 1, 'price': 1, 'visibility': 1, 'available': 1, 'priority': 1 }", sort = "{'priority': 1}")
  List<Drug> findLikeName(String name);
  
  @Query(value = "{_id: {$exists: true}}", fields = "{ 'name': 1, '_id': 1, 'price': 1, 'visibility': 1, 'available': 1, 'priority': 1 }", sort = "{'priority': 1}")
  List<Drug> findAllDigests();
  
  @Query(value = "{ name: { $regex: ?0, $options:'i'}, visibility: true }", fields = "{ 'name': 1, '_id': 1 }", sort = "{'priority': 1}")
  List<Drug> findLikeNameAndVisible(String name);
  
  @Query(value = "{ visibility: true }", fields = "{ 'name': 1, '_id': 1 }", sort = "{'priority': 1}")
  List<Drug> findVisibleDigests();
}
