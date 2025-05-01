package four.group.jahadi.Repository;

import four.group.jahadi.Models.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "Project")
public interface ProjectRepository extends MongoRepository<Project, ObjectId>, FilterableRepository<Project> {

    @Query(value = "{'group_ids': {$in: ?0}}")
    List<Project> findByOwner(List<ObjectId> owner);

    @Query(value = "{'_id': {$in: ?0}}")
    List<Project> findByIds(List<ObjectId> ids);


    @Query(value = "{'_id': {$in: ?0}}", fields = "{'name': 1}")
    List<Project> findDigestByIds(List<ObjectId> ids);

}
