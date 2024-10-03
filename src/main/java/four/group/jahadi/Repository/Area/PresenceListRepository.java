package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.PresenceList;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresenceListRepository extends MongoRepository<PresenceList, ObjectId>, FilterableRepository<PresenceList> {

    @Query(value = "{area_id: ?0}")
    List<PresenceList> findByAreaId(ObjectId areaId);

}
