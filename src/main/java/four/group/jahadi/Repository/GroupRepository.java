package four.group.jahadi.Repository;

import four.group.jahadi.Models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, ObjectId>, FilterableRepository<Group> {}
