package four.group.jahadi.Repository;

import four.group.jahadi.Models.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "Module")
public interface ModuleRepository extends MongoRepository<Module, ObjectId> {

    @Query(value = "{}", fields = "{'id': 1, 'name': 1}")
    List<Module> findAllDigests();

    @Query(value = "{id: {$in : ?0}}", fields = "{'name': 1, 'id': 1, 'tabName': 1}")
    List<Module> findByIds(List<ObjectId> ids);

    @Query(value = "{id: {$in : ?0}}", fields = "{'tabName': 1, 'id': 1}")
    List<Module> findTabNamesByIds(List<ObjectId> ids);
}
