package four.group.jahadi.Repository;

import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends MongoRepository<Module, ObjectId>, FilterableRepository<Group> {}
