package four.group.jahadi.Repository;

import four.group.jahadi.Models.WareHouseAccessForGroup;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseAccessForGroupRepository extends
        MongoRepository<WareHouseAccessForGroup, ObjectId>, FilterableRepository<WareHouseAccessForGroup> {

    @Query(value = "{groupId: ?0, hasAccessForDrug: true, userId: ?1}", exists = true)
    boolean existsDrugAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

    @Query(value = "{groupId: ?0, hasAccessForEquipment: true, userId: ?1}", exists = true)
    boolean existsEquipmentAccessByGroupIdAndUserId(ObjectId groupId, ObjectId userId);

}
