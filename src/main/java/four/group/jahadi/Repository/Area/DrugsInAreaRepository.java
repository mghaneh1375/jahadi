package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugsInAreaRepository extends MongoRepository<AreaDrugs, ObjectId>, FilterableRepository<AreaDrugs> {

    @Query(value = "{areaId: ?0}", count = true)
    Integer countByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, drugId: ?1}", exists = true)
    Boolean existByAreaIdAndDrugId(ObjectId areaId, ObjectId drugId);

    @Query(value = "{areaId: ?0, drugId: ?1}")
    Optional<AreaDrugs> findByAreaIdAndDrugId(ObjectId areaId, ObjectId drugId);

    @Query(value = "{areaId: ?0}", fields = "{drugName: 1, drugId: 1, reminder: 1}")
    List<AreaDrugs> findDigestByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, reminder: {$gt: 0}}", fields = "{drugName: 1, drugId: 1, reminder: 1}")
    List<AreaDrugs> findAvailableDrugsByAreaId(ObjectId areaId);

    @Query(value = "{_id: {$in: ?0}}", delete = true)
    void removeAreaDrugsById(List<ObjectId> ids);
}
