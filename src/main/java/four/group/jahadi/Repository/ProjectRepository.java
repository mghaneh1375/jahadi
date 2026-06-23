package four.group.jahadi.Repository;

import four.group.jahadi.Models.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId>, FilterableRepository<Project> {

    @Query(value = "{'group_ids': {$in: ?0}}")
    List<Project> findByOwner(List<ObjectId> owner);

    @Query(value = "{$and: [{'group_ids': {$in: ?0}}, {'endAt': {$gte: ?1}}]}", sort = "{'createdAt': -1}")
    List<Project> findActivesByOwner(List<ObjectId> owner, LocalDateTime curr);

    @Query(value = "{'name': ?0}", count = true)
    Integer countByName(String name);

    @Query(value = "{'_id': {$in: ?0}}")
    List<Project> findByIds(List<ObjectId> ids);


    @Query(value = "{'_id': {$in: ?0}}", fields = "{'name': 1}")
    List<Project> findDigestByIds(List<ObjectId> ids);

    @Query(value = "{ $or: [ { start_at: { $gte: ?0 } }, { end_at: { $gte: ?0 } } ] }", count = true)
    Long getTotalProjectsCountInLastMonth(LocalDateTime oneMonthAgo);
}
