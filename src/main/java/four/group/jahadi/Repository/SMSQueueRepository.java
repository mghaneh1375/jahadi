package four.group.jahadi.Repository;

import four.group.jahadi.Models.SMSQueue;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSQueueRepository extends MongoRepository<SMSQueue, ObjectId>, FilterableRepository<SMSQueue> {

    @Query(value = "{sent: false}", fields = "{'phone': 1, 'msg': 1}", sort = "{'created_at': 1}")
    List<SMSQueue> getNext10();

}
