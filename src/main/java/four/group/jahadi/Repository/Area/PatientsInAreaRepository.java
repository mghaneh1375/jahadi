package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.PatientJoinArea;
import four.group.jahadi.Models.Area.PatientJoinForReferrals;
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

    @Query(value = "{areaId: ?0}", delete = true)
    void deleteByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0}")
    List<PatientsInArea> findByAreaId(ObjectId areaId);

    @Query(value = "{patientId: ?0}")
    List<PatientsInArea> findByPatientId(ObjectId patientId);

    @Query(value = "{areaId: ?0, patientId: ?1}", exists = true)
    Boolean existByAreaIdAndPatientId(ObjectId areaId, ObjectId patientId);

    @Query(value = "{areaId: ?0, patientId: ?1}", delete = true)
    void deleteByAreaIdAndPatientId(ObjectId areaId, ObjectId patientId);

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
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained'}}",
    })
    List<PatientJoinArea> findPatientsByAreaId(ObjectId areaId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$unset: 'referrals.forms'}",
            "{$project: {'patientInfo': '$patientInfo', 'referrals': '$referrals'}}"
    })
    List<PatientJoinArea> findPatientsListInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId,
            int skip, int limit
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$count: 'total'}"
    })
    Long countPatientsInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId
    );
    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?4, options: 'i'}}]}]}}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', patientInfo: {$first: '$patientInfo'}, areaId: {$first: '$areaId'}, trained: {$first: '$trained'}, lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': true}}",
            "{$sort: {'lastReferral.recepted_at': -1}}",
            "{$skip: ?2}",
            "{$limit: ?3}",
            "{$project: {'patientInfo': '$patientInfo', 'createdAt': '$lastReferral.recepted_at'}}"
    })
    List<PatientJoinArea> findReceptedPatientsListInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId,
            int skip, int limit,
            String search
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': true}}",
            "{$count: 'total'}"
    })
    Long countReceptedPatientsInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?2, options: 'i'}}]}]}}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': true}}",
            "{$count: 'total'}"
    })
    Long countReceptedPatientsInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId, String search
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?4, options: 'i'}}]}, {$or: [{$eq: [?4, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?4, options: 'i'}}]}]}}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', patientInfo: {$first: '$patientInfo'}, areaId: {$first: '$areaId'}, lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': false}}",
            "{$sort: {'lastReferral.created_at': 1}}",
            "{$skip: ?2}",
            "{$limit: ?3}",
            "{$project: {'patientInfo': '$patientInfo', 'createdAt': '$lastReferral.created_at'}}"
    })
    List<PatientJoinArea> findUnReceptedPatientsListInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId,
            int skip, int limit,
            String search
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': false}}",
            "{$count: 'total'}"
    })
    Long countUnReceptedPatientsInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {'referrals.moduleId': ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?2, options: 'i'}}]}, {$or: [{$eq: [?2, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?2, options: 'i'}}]}]}}}",
            "{$unwind: '$referrals'}",
            "{$match: {'referrals.moduleId': ?1}}",
            "{$sort: {'referrals.createdAt': -1}}",
            "{$group: {_id: '$_id', lastReferral: {$first: '$referrals'}}}",
            "{$match: {'lastReferral.recepted': false}}",
            "{$count: 'total'}"
    })
    Long countUnReceptedPatientsInModuleByAreaId(
            ObjectId areaId, ObjectId moduleId, String search
    );

//    @Query(value = "{areaId: ?0, referrals: { $elemMatch: {moduleId: ?1, forms: {$exists: true}} } }")
    @Query(value = "{areaId: ?0, referrals: { $elemMatch: {moduleId: ?1} } }")
    List<PatientsInArea> findByAreaIdAndModuleId(ObjectId areaId, ObjectId moduleId);

    @Query(value = "{areaId: ?0, referrals: { $elemMatch: {moduleId: {$in: ?1}, forms: {$exists: true}, 'forms.subModuleId': {$in: ?2}} } }", fields = "{_id: 1, patientId: 1}")
    List<PatientsInArea> findByAreaIdAndModuleIdInAndSubModuleIdIn(ObjectId areaId, List<ObjectId> moduleIds, List<ObjectId> subModuleId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {referrals: { $elemMatch: {moduleId: {$in: ?1}, forms: {$exists: true}, 'forms.subModuleId': {$in: ?2}} }}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
    })
    List<PatientJoinForReferrals> findByAreaIdAndModuleIdInAndSubModuleIdInWithJoin(ObjectId areaId, List<ObjectId> moduleIds, List<ObjectId> subModuleId);

}
