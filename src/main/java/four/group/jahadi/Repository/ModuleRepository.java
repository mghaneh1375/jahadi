package four.group.jahadi.Repository;

import four.group.jahadi.Models.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends MongoRepository<Module, ObjectId>, FilterableRepository<Module> {

    @Query(value = "{}", fields = "{'id': 1, 'name': 1}")
    List<Module> findAllDigests();

    @Query(value = "{id: ?0}", fields = "{'id': 1, 'name': 1}")
    Optional<Module> findDigestById(ObjectId id);

    @Query(value = "{id: {$in : ?0}}", count = true)
    Integer countByIds(List<ObjectId> ids);

    @Query(value = "{id: {$in : ?0}}", fields = "{'name': 1, 'id': 1, 'tabName': 1}")
    List<Module> findByIds(List<ObjectId> ids);

    @Query(value = "{id: {$in : ?0}}", fields = "{'tabName': 1, 'id': 1}")
    List<Module> findTabNamesByIds(List<ObjectId> ids);

    @Query(value = "{name: ?0}", fields = "{'id': 1}")
    Module findByName(String name);

    @Query(value = "{'subModules.name': ?0}", fields = "{'id': 1, 'subModules': 1}")
    List<Module> findAllBySubModuleName(String name);
}
