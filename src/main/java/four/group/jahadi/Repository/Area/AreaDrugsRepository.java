package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Repository.MyRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@MyRepository(model = "DrugsInArea")
public interface AreaDrugsRepository extends MongoRepository<AreaDrugs, ObjectId> {

    @Query(value = "{areaId: ?0, drugId: ?1}")
    Optional<AreaDrugs> findByAreaIdAndDrugId(ObjectId areaId, ObjectId drugId);

    @Query(value = "{areaId: ?0}")
    List<AreaDrugs> findByAreaId(ObjectId areaId);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo'}}",
            "{$unset: 'drugInfo.created_at'}",
            "{$unset: 'drugInfo.user_id'}",
            "{$unset: 'drugInfo.group_id'}",
            "{$unset: 'drugInfo.price'}",
            "{$unset: 'drugInfo.available'}",
            "{$unset: 'drugInfo.available_pack'}",
            "{$unset: 'drugInfo.location'}",
            "{$unset: 'drugInfo.box_no'}",
            "{$unset: 'drugInfo.shelf_no'}",
            "{$unwind: '$drugInfo'}",
            "{$unset: 'area_id'}",
            "{$unset: 'drug_id'}",
            "{$unset: 'drug_name'}"
    })
    List<JoinedAreaDrugs> findDigestByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, reminder: {$gt: 0}}")
    List<AreaDrugs> findAvailableDrugsByAreaId(ObjectId areaId);
}
