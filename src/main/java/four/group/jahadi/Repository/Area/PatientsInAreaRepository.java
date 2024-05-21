package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.PatientJoinArea;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientsInAreaRepository extends MongoRepository<PatientsInArea, ObjectId>, FilterableRepository<PatientsInArea> {

    @Query(value = "{areaId: ?0}", count = true)
    Integer countByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, patientId: ?1}", exists = true)
    Boolean existByAreaIdAndPatientId(ObjectId areaId, ObjectId patientId);

    @Query(value = "{areaId: ?0, patientId: ?1}")
    Optional<PatientsInArea> findByAreaIdAndPatientId(ObjectId areaId, ObjectId patientId);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', localField: 'id', foreignField: 'patients_in_area.patientId', as: 'patients'}}",
            "{$unwind: '$patients'}",
            "{$project: {'identifier': '$patients.identifier'}}",
    })
    List<Patient> findPatientsIdentifierByAreaId(ObjectId areaId);


    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$unwind: '$created_at'}",
            "{$unwind: '$trained'}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at'}}",
    })
    List<PatientJoinArea> findPatientsByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, referrals.moduleId: ?1}")
    List<Patient> findByAreaIdAndModuleId(ObjectId areaId, ObjectId moduleId);

    @Query(value = "{areaId: ?0, referrals.moduleId: ?1, referrals.recepted: ?2}")
    List<Patient> findByAreaIdAndModuleIdAndRecepted(ObjectId areaId, ObjectId moduleId, boolean recepted);

}
