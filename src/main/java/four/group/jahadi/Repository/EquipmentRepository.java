package four.group.jahadi.Repository;

import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Models.Equipment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, ObjectId>, FilterableRepository<Equipment> {
    @Query(value = "{_id: ?0, userId: ?1}")
    Optional<Equipment> findByIdAndUserId(ObjectId id, ObjectId userId);
    @Query(value = "{userId: ?1}")
    List<Equipment> findByFilters(
            ObjectId userId, String name,
            Integer minAvailable, Integer maxAvailable,
            EquipmentHealthStatus healthStatus
    );

}
