package four.group.jahadi.Repository;

import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Models.Patient;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@MyRepository(model = "Patient")
public interface PatientRepository extends MongoRepository<Patient, ObjectId>, FilterableRepository<Patient> {

    @Query(value = "{identifier: ?0, identifierType: ?1}", count = true)
    Integer countByIdentifierAndIdentifierType(String identifier, IdentifierType identifierType);

    @Query(value = "{identifier: ?0, identifierType: ?1}")
    Optional<Patient> findByIdentifierAndIdentifierType(String identifier, IdentifierType identifierType);

    @Query(value = "{_id: {$in: ?0}}", fields = "{job: 0, phone: 0, fatherName: 0, ageType: 0, createdAt: 0}")
    List<Patient> findPublicInfoByIdIn(List<ObjectId> ids);

    @Query(value = "{_id: {$in: ?0}}", fields = "{name: 1, sex: 1, insurance: 1, ageType: 1}")
    List<Patient> findExcelInfoByIdIn(List<ObjectId> ids);

//    @Aggregation(pipeline = {
//            "{$lookup: {from: 'patients_in_area', let: {id: 'patient._id'}, pipeline: [{$match: {$expr: {$and: [{$eq: ['$area_id', ?0]}, {$eq: ['$patient_id', '$$id']}]}}}], as: 'areaInfo'}}",
////            "{$lookup: {from: 'patients_in_area', let: {id: 'patient._id'}, pipeline: [{$match: {$expr: {$and: [{$eq: ['$area_id', ?0]}]}}}], as: 'areaInfo'}}",
////            "{$unwind: '$areaInfo'}",
////            "{$project: {'areaInfo.referrals': 0}}",
//    })
//    List<Patient> findPatientsByAreaId(ObjectId areaId);

}
