package four.group.jahadi.Repository;

import four.group.jahadi.Models.ExternalReferralService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExternalReferralRepository extends
        MongoRepository<ExternalReferralService, ObjectId>, FilterableRepository<ExternalReferralService>  {
    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2}")
    List<ExternalReferralService> findServices(ObjectId patientId, ObjectId areaId, ObjectId formId);
    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2, id: ?3}")
    Optional<ExternalReferralService> findService(ObjectId patientId, ObjectId areaId, ObjectId formId, ObjectId serviceId);

    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2, id: ?3}", delete = true)
    void removeService(ObjectId patientId, ObjectId areaId, ObjectId formId, ObjectId serviceId);

}
