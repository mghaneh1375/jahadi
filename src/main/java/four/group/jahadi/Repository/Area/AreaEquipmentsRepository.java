package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.Area.JoinedAreaEquipments;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AreaEquipmentsRepository extends MongoRepository<AreaEquipments, ObjectId>, FilterableRepository<AreaEquipments> {

    @Query(value = "{areaId: ?0, equipmentId: {$in: ?1}}")
    List<AreaEquipments> findAllByAreaIdAndEquipmentId(ObjectId areaId, Set<ObjectId> equipmentIds);

    @Query(value = "{areaId: ?0, equipmentId: {$in: ?1}}", fields = "{_id: 1, equipmentId: 1}")
    List<AreaEquipments> findIdsByAreaIdAndIds(ObjectId areaId, List<ObjectId> ids);

    @Query(value = "{areaId: ?0}")
    List<AreaEquipments> findByArea(ObjectId areaId);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'equipment', localField: 'equipment_id', foreignField: '_id', as: 'equipmentInfo'}}",
            "{$unset: 'equipmentInfo.created_at'}",
            "{$unset: 'equipmentInfo.user_id'}",
            "{$unset: 'equipmentInfo.group_id'}",
            "{$unset: 'equipmentInfo.property_id'}",
            "{$unset: 'equipmentInfo.price'}",
            "{$unset: 'equipmentInfo.location'}",
            "{$unset: 'equipmentInfo.available'}",
            "{$unset: 'equipmentInfo.buy_at'}",
            "{$unset: 'equipmentInfo.used_at'}",
            "{$unset: 'equipmentInfo.row_no'}",
            "{$unset: 'equipmentInfo.shelf_no'}",
            "{$unwind: '$equipmentInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'equipment_id'}",
            "{$unset: 'equipment_name'}"
    })
    List<JoinedAreaEquipments> findDigestByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, reminder: {$gt: 0}}")
    List<AreaEquipments> findAvailableEquipmentsByAreaId(ObjectId areaId);

    @Query(value = "{_id: {$in: ?0}, areaId: ?1}", delete = true)
    List<AreaEquipments> removeAreaEquipmentsByIdAndAreaId(List<ObjectId> ids, ObjectId areaId);
}
