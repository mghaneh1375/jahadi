package four.group.jahadi.Repository;

import four.group.jahadi.DTO.dashboard.FinantialPerProvinceData;
import four.group.jahadi.DTO.dashboard.PerProvinceData;
import four.group.jahadi.Models.ExternalReferralService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExternalReferralRepository extends
        MongoRepository<ExternalReferralService, ObjectId>, FilterableRepository<ExternalReferralService>  {
    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2}")
    List<ExternalReferralService> findServices(ObjectId patientId, ObjectId areaId, ObjectId formId);

    @Query(value = "{areaId: {$in: ?0}}", fields = "{totalCost: 1}")
    List<ExternalReferralService> findByAreaIds(List<ObjectId> areaIds);

    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2, id: ?3}")
    Optional<ExternalReferralService> findService(ObjectId patientId, ObjectId areaId, ObjectId formId, ObjectId serviceId);

    @Query(value = "{patientId: ?0, areaId: ?1, formId: ?2, id: ?3}", delete = true)
    void removeService(ObjectId patientId, ObjectId areaId, ObjectId formId, ObjectId serviceId);

    @Query(value = "{ created_at: { $gte: ?0 } }", count = true)
    Long getTotalExternalServicesCountInLastMonth(LocalDateTime oneMonthAgo);

    @Aggregation(pipeline = {
            "{ $project: { form_id:1, area_id: 1, created_at: 1, patient_id: 1 } }",
            "{ $match: { created_at: { $gte: ?0, $lte: ?1 } } }",
            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaId: '$area_id', createdAt: '$created_at' }, " +
                    "pipeline: [ " +
                    "{ $unwind: '$areas' }, " +
                    "{ $match: { $expr: { $and: [ " +
                    "{ $eq: ['$areas._id', '$$areaId'] }, " +
                    "] } } }, " +
                    "{ $project: { state: '$areas.state', _id: 0 } } " +
                    "], " +
                    "as: 'areaInfo' " +
                    "} }",
            "{ $unwind: { path: '$areaInfo', preserveNullAndEmptyArrays: false } }",
            "{ $group: { _id: '$areaInfo.state', uniquePatients: { $addToSet: '$form_id' } } }",
            "{ $project: { province: '$_id', count: { $size: '$uniquePatients' }, _id: 0 } }"
    })
    List<PerProvinceData> countExternalServicesByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(pipeline = {
//            "{ $project: { area_id: 1, created_at: 1, jahadi_paid: 1 } }",
            "{ $match: { created_at: { $gte: ?0, $lte: ?1 } } }",
            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaId: '$area_id', createdAt: '$created_at' }, " +
                    "pipeline: [ " +
                    "{ $unwind: '$areas' }, " +
                    "{ $match: { $expr: { $and: [ " +
                    "{ $eq: ['$areas._id', '$$areaId'] }, " +
                    "] } } }, " +
                    "{ $project: { state: '$areas.state', _id: 0 } } " +
                    "], " +
                    "as: 'areaInfo' " +
                    "} }",
            "{ $unwind: { path: '$areaInfo', preserveNullAndEmptyArrays: false } }",
            "{ $group: { _id: '$areaInfo.state', totalAmount: { $sum: '$jahadi_paid' } } }",
            "{ $project: { province: '$_id', count: '$totalAmount', _id: 0 } }"
    })
    List<FinantialPerProvinceData> amountSumExternalServicesByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
