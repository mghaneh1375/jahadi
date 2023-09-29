package four.group.jahadi.Repository;

import four.group.jahadi.Models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>, FilterableRepository<User> {

    @Query(value = "{'_id':  ?0, 'status':  'ACTIVE'}", count = true)
    Integer countActiveBy_id(ObjectId id);

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'first_name': 1, 'last_name': 1, 'phone': 1, 'pic': 1, 'color': 1  }")
    List<User> findBy_idIn(List<ObjectId> ids);

    @Query(value = "{'nid':  ?0}")
    Optional<User> findByNID(String nid);

    @Query(value = "{'phone':  ?0}", count = true)
    Integer countByPhone(String phone);

    @Query(value = "{'nid':  ?0}", count = true)
    Integer countByNID(String nid);

}
