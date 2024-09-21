package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentsInAreaRepository extends MongoRepository<AreaEquipments, ObjectId>, FilterableRepository<AreaEquipments> {

    @Query(value = "{areaId: ?0, equipmentId: ?1}")
    Optional<AreaEquipments> findByAreaIdAndEquipmentId(ObjectId areaId, ObjectId equipmentId);

    @Query(value = "{areaId: ?0, equipmentId: {$in: ?1}}", fields = "{_id: 1}")
    List<ObjectId> findIdsByAreaIdAndIds(ObjectId areaId, List<ObjectId> ids);

    @Query(value = "{areaId: ?0}", fields = "{equipmentName: 1, equipmentId: 1, reminder: 1}")
    List<AreaEquipments> findDigestByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, reminder: {$gt: 0}}", fields = "{equipmentName: 1, equipmentId: 1, reminder: 1}")
    List<AreaEquipments> findAvailableEquipmentsByAreaId(ObjectId areaId);

    @Query(value = "{_id: {$in: ?0}, areaId: ?1}", delete = true)
    List<AreaEquipments> removeAreaEquipmentsByIdAndAreaId(List<ObjectId> ids, ObjectId areaId);
}
