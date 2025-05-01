package four.group.jahadi.Repository;

import four.group.jahadi.Models.Notif;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "Notif")
public interface NotifRepository extends MongoRepository<Notif, ObjectId>, FilterableRepository<Notif> {

    @Query(value = "{user_id: ?0, seen: false}", fields = "{'id': 1, 'msg': 1, 'seen':  1, 'created_at':  1}")
    List<Notif> findByUserIdAndUnseen(ObjectId userId);

    @Query(value = "{user_id: ?0}", fields = "{'id': 1, 'msg': 1, 'seen':  1, 'created_at':  1}")
    List<Notif> findByUserId(ObjectId userId);
}
