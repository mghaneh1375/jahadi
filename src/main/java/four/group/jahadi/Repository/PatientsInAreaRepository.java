package four.group.jahadi.Repository;

import four.group.jahadi.Models.PatientsInArea;
import four.group.jahadi.Models.SMSQueue;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientsInAreaRepository extends MongoRepository<PatientsInArea, ObjectId>, FilterableRepository<PatientsInArea> {

    @Query(value = "{areaId: ?0}", count = true)
    Integer countByAreaId(ObjectId areaId);

}
