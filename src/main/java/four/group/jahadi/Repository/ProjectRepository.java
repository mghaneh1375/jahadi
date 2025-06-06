package four.group.jahadi.Repository;

import four.group.jahadi.Models.Project;
import four.group.jahadi.Models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId>, FilterableRepository<Project> {

    @Query(value = "{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { 'name' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'startAt' : {$gt: [2]} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { 'endAt' : {$lt: [2]} } },"
            + "]}", fields = "{'id': 1, 'createdAt': 1, 'name': 1, 'nid': 1, 'phone': 1, 'sex': 1, 'groupName': 1, 'status': 1, 'accesses': 1}")
    List<User> findAll(String name, Boolean justActive, Boolean justArchive, LocalDateTime now);

    @Query(value = "{'group_ids': {$in: ?0}}")
    List<Project> findByOwner(List<ObjectId> owner);

    @Query(value = "{'name': ?0}", count = true)
    Integer countByName(String name);

    @Query(value = "{'_id': {$in: ?0}}")
    List<Project> findByIds(List<ObjectId> ids);


    @Query(value = "{'_id': {$in: ?0}}", fields = "{'name': 1}")
    List<Project> findDigestByIds(List<ObjectId> ids);

}
