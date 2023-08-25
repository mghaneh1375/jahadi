package four.group.jahadi.Repository;

import four.group.jahadi.DTO.Digest.GroupDigest;
import four.group.jahadi.Models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, ObjectId>, FilterableRepository<Group> {

    @Query(value = "{name:{$regex:?0,$options:'i'}}")
    List<Group> findLikeName(String name);

    @Query(value = "{_id: {$in : ?0}}", count = true)
    Integer countBy_idIn(List<ObjectId> ids);

    @Query(value = "{_id: {$in : ?0}}", fields = "{ 'name': 1, 'color': 1 }")
    List<GroupDigest> findBy_idIn(List<ObjectId> ids);

    @Query(value = "{owner: ?0}", fields = "{ '_id': 1 }")
    List<Group> findByUserId(ObjectId userId);
}
