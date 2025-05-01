package four.group.jahadi.Repository;

import four.group.jahadi.Models.EquipmentLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@MyRepository(model = "EquipmentLogs")
public interface EquipmentLogRepository extends MongoRepository<EquipmentLog, ObjectId>, FilterableRepository<EquipmentLog> { }
