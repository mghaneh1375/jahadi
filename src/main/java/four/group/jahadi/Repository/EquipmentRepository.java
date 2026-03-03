package four.group.jahadi.Repository;

import four.group.jahadi.Models.Equipment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, ObjectId>, FilterableRepository<Equipment> {
    @Query(value = "{_id: ?0, groupId: ?1}")
    Optional<Equipment> findByIdAndGroupId(ObjectId id, ObjectId groupId);

    @Query(value = "{_id: {$in: ?0}, groupId: ?1}")
    List<Equipment> findAllByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ _id: {$in: ?0}}")
    List<Equipment> findFullInfoByIds(List<ObjectId> ids);

}
