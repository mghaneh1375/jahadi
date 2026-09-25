package four.group.jahadi.Repository.Area;

import four.group.jahadi.DTO.dashboard.PerProvinceData;
import four.group.jahadi.Models.Area.PatientJoinArea;
import four.group.jahadi.Models.Area.PatientJoinForReferrals;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Repository.Area.impl.*;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PatientsInAreaRepository extends MongoRepository<PatientsInArea, ObjectId>, FilterableRepository<PatientsInArea> {

    @Query(value = "{areaId: ?0}", count = true)
    Integer countByAreaId(ObjectId areaId);

    @Query(value = "{areaId: {$in: ?0}}", count = true)
    Integer countByAreaIds(List<ObjectId> areaIds);

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
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$unwind: '$created_at'}",
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained', 'referrals': '$referrals', 'trainForm': '$trainForm'}}",
    })
    List<PatientJoinArea> findPatientsByAreaId(
            ObjectId areaId
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', let: {patientId: '$patient_id'}, " +
                    "pipeline: [{$match: {$expr: {$and: [{$eq: ['$_id', '$$patientId']}, " +
                    "{$ne: ['$insurance', 'NONE']}]}}}], as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?3, options: 'i'}}]}]}}}",
            "{$unwind: '$created_at'}",
            "{$sort: {'createdAt': -1}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained'}}",
    })
    List<PatientJoinArea> findPatientsHasInsuranceByAreaId(
            ObjectId areaId,
            int skip, int limit, String key
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', let: {patientId: '$patient_id'}, " +
                    "pipeline: [{$match: {$expr: {$and: [{$eq: ['$_id', '$$patientId']}, " +
                    "{$ne: ['$insurance', 'NONE']}]}}}], as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?1, options: 'i'}}]}]}}}",
            "{$count: 'total'}"
    })
    Long countPatientsHasInsuranceByAreaId(
            ObjectId areaId, String key
    );
    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', let: {patientId: '$patient_id'}, " +
                    "pipeline: [{$match: {$expr: {$and: [{$eq: ['$_id', '$$patientId']}, " +
                    "{$eq: ['$insurance', 'NONE']}]}}}], as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?3, options: 'i'}}]}]}}}",
            "{$unwind: '$created_at'}",
            "{$sort: {'createdAt': -1}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained'}}",
    })
    List<PatientJoinArea> findPatientsNotHasInsuranceByAreaId(
            ObjectId areaId,
            int skip, int limit, String key
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', let: {patientId: '$patient_id'}, " +
                    "pipeline: [{$match: {$expr: {$and: [{$eq: ['$_id', '$$patientId']}, " +
                    "{$eq: ['$insurance', 'NONE']}]}}}], as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?1, options: 'i'}}]}]}}}",
            "{$count: 'total'}"
    })
    Long countPatientsNotHasInsuranceByAreaId(
            ObjectId areaId, String key
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?3, options: 'i'}}]}]}}}",
            "{$unwind: '$created_at'}",
            "{$sort: {'createdAt': -1}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained'}}",
    })
    List<PatientJoinArea> findPatientsByAreaId(
            ObjectId areaId, int skip, int limit,
            String key
    );

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?1, options: 'i'}}]}, {$or: [{$eq: [?1, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?1, options: 'i'}}]}]}}}",
            "{$count: 'total'}"
    })
    Long countPatientsByAreaId(
            ObjectId areaId, String key
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {trained: ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unset: 'patientInfo.created_at'}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?5, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?5, options: 'i'}}]}, {$or: [{$eq: [?5, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?5, options: 'i'}}]}, {$or: [{$eq: [?5, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?5, options: 'i'}}]}, {$or: [{$eq: [?5, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?5, options: 'i'}}]}]}}}",
            "{$unwind: '$created_at'}",
            "{$match: {'patientInfo.age_type': ?2}}",
            "{$sort: {'createdAt': -1}}",
            "{$skip: ?3}",
            "{$limit: ?4}",
            "{$unwind: {'path': '$trained', 'preserveNullAndEmptyArrays': true}}",
            "{$project: {'patientInfo': '$patientInfo', 'created_at': '$created_at', 'trained': '$trained'}}",
    })
    List<PatientJoinArea> findPatientsByAreaIdByTrainStatusAndAgeType(
            ObjectId areaId, boolean trainStatus, String ageType,
            int skip, int limit, String key
    );

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {trained: ?1}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
            "{$match: {$expr: {$or: [{$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.name', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.identifier', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.phone', regex: ?3, options: 'i'}}]}, {$or: [{$eq: [?3, null]}, {$regexMatch: {input: '$patientInfo.patient_no', regex: ?3, options: 'i'}}]}]}}}",
            "{$match: {'patientInfo.age_type': ?2}}",
            "{$count: 'total'}"
    })
    Long countPatientsByAreaIdByTrainStatusAndAgeType(
            ObjectId areaId, boolean trainStatus,
            String ageType, String key
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
    List<PatientsInArea> findByAreaIdAndModuleIdInAndSubModuleIdIn(ObjectId areaId, List<ObjectId> moduleIds, List<ObjectId> subModuleId, Pageable pageable);

    @Query(value = "{areaId: ?0, referrals: { $elemMatch: {moduleId: {$in: ?1}, forms: {$exists: true}, 'forms.subModuleId': {$in: ?2}} } }", count = true)
    Integer countByAreaIdAndModuleIdInAndSubModuleIdIn(ObjectId areaId, List<ObjectId> moduleIds, List<ObjectId> subModuleId);

    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0}, {referrals: { $elemMatch: {moduleId: {$in: ?1}, forms: {$exists: true}, 'forms.subModuleId': {$in: ?2}} }}]}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$unwind: '$patientInfo'}",
    })
    List<PatientJoinForReferrals> findByAreaIdAndModuleIdInAndSubModuleIdInWithJoin(ObjectId areaId, List<ObjectId> moduleIds, List<ObjectId> subModuleId);

    @Aggregation(pipeline = {
            "{ $project: { referrals: 0 } }",
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
            "{ $group: { _id: '$areaInfo.state', uniquePatients: { $addToSet: '$patient_id' } } }",
            "{ $project: { province: '$_id', count: { $size: '$uniquePatients' }, _id: 0 } }"
    })
    List<PerProvinceData> countPatientsByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "{ created_at: { $gte: ?0 } }", count = true)
    Long getTotalPatientsCountInLastMonth(LocalDateTime oneMonthAgo);

    @Aggregation(pipeline = {
            "{ $match: { area_id: { $in: ?0 } } }",
            "{ $match: { 'referrals.module_id': { $in: ?1 } } }",
            "{ $unwind: { path: '$referrals', preserveNullAndEmptyArrays: false } }",
            "{ $match: { 'referrals.module_id': { $in: ?1 } } }",
            "{ $unwind: { path: '$referrals.forms', preserveNullAndEmptyArrays: true } }",

            "{ $group: { " +
                    "_id: { " +
                    "areaId: '$area_id', " +
                    "moduleId: '$referrals.module_id', " +
                    "patientId: '$patient_id' " +
                    "}, " +
                    "accepted: { $max: '$referrals.recepted' }, " +
                    "acceptedByDoctor: { " +
                    "$max: { " +
                    "$cond: [ " +
                    "{ $eq: ['$referrals.forms.doctor_id', ?2] }, " +
                    "true, " +
                    "false " +
                    "] " +
                    "} " +
                    "} " +
                    "} }",

            "{ $group: { " +
                    "_id: { " +
                    "areaId: '$_id.areaId', " +
                    "moduleId: '$_id.moduleId' " +
                    "}, " +
                    "total: { $sum: 1 }, " +
                    "accepted: { $sum: { $cond: ['$accepted', 1, 0] } }, " +
                    "acceptedByDoctor: { $sum: { $cond: ['$acceptedByDoctor', 1, 0] } } " +
                    "} }",

            "{ $project: { " +
                    "total: 1, " +
                    "accepted: 1, " +
                    "acceptedByDoctor: 1, " +
                    "_id: 1 " +
                    "} }",
    })
    List<PatientStatistics> getPatientStatisticsByAreasAndModules(
            List<ObjectId> areaIds,
            List<ObjectId> moduleIds,
            ObjectId doctorId
    );


    @Aggregation(pipeline = {
            "{ $match: { area_id: { $in: ?0 } } }",
            "{ $match: { 'referrals.module_id': { $in: ?1 } } }",
            "{ $unwind: { path: '$referrals', preserveNullAndEmptyArrays: false } }",
            "{ $match: { 'referrals.module_id': { $in: ?1 } } }",
            "{ $unwind: { path: '$referrals.forms', preserveNullAndEmptyArrays: true } }",

            "{ $group: { " +
                    "_id: { " +
                    "areaId: '$area_id', " +
                    "moduleId: '$referrals.module_id', " +
                    "patientId: '$patient_id' " +
                    "}, " +
                    "accepted: { $max: '$referrals.recepted' } " +
                    "} }",

            "{ $group: { " +
                    "_id: { " +
                    "areaId: '$_id.areaId', " +
                    "moduleId: '$_id.moduleId' " +
                    "}, " +
                    "total: { $sum: 1 }, " +
                    "accepted: { $sum: { $cond: ['$accepted', 1, 0] } } " +
                    "} }",

            "{ $project: { " +
                    "total: 1, " +
                    "accepted: 1, " +
                    "_id: 1 " +
                    "} }",
    })
    List<PatientStatistics> getPatientStatisticsByAreasAndModules(
            List<ObjectId> areaIds,
            List<ObjectId> moduleIds
    );

    @Aggregation(pipeline = {
            "{ $unwind: { path: '$referrals', preserveNullAndEmptyArrays: false } }",
            "{ $unwind: { path: '$referrals.forms', preserveNullAndEmptyArrays: false } }",
            "{ $match: { 'referrals.forms.doctor_id': ?0 } }",
            "{ $group: { " +
                    "_id: '$patient_id' " +
                    "} }",
            "{ $count: 'totalAccepted' }"
    })
    Integer getTotalAcceptedPatientsByDoctor(ObjectId doctorId);

    @Aggregation(pipeline = {
            "{ $match: { area_id: { $in: ?0 } } }",
            "{ $group: { " +
                    "_id: '$area_id', " +
                    "totalPatients: { $sum: 1 } " +
                    "} }",

            "{ $project: { " +
                    "totalPatients: 1, " +
                    "_id: 1 " +
                    "} }",
    })
    List<AreaPatientCount> getTotalPatientsByAreas(List<ObjectId> areaIds);

    @Aggregation(pipeline = {
            "{ $match: { " +
                    "$expr: { " +
                    "$and: [ " +
                    "{ $or: [ " +
                    "{ $eq: [?0, null] }, " +
                    "{ $in: [?0, { $ifNull: ['$referrals.module_id', []] }] } " +
                    "] }, " +
                    "{ $or: [ " +
                    "{ $eq: [?1, null] }, " +
                    "{ $eq: ['$area_id', ?1] } " +
                    "] } " +
                    "] " +
                    "} " +
                    "} }",

            "{ $unwind: { " +
                    "path: '$referrals', " +
                    "preserveNullAndEmptyArrays: false " +
                    "} }",

            "{ $match: { " +
                    "$expr: { " +
                    "$or: [ " +
                    "{ $eq: [?0, null] }, " +
                    "{ $eq: ['$referrals.module_id', ?0] } " +
                    "] " +
                    "} " +
                    "} }",

            "{ $group: { " +
                    "_id: '$patient_id', " +
                    "areaId: { $first: '$area_id' } " +
                    "} }",

            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaId: '$area_id' }, " +
                    "pipeline: [ " +
                    "{ $unwind: '$areas' }," +

                    "{ $match: { " +
                    "$expr: { " +
                    "$eq: ['$areas._id', '$$areaId'] " +
                    "} " +
                    "} }," +

                    "{ $match: { " +
                    "$or: [ " +
                    "{ $expr: { $eq: [?2, null] } }, " +
                    "{ $expr: { $eq: ['$_id', ?2] } } " +
                    "] " +
                    "} }," +

                    "{ $match: { " +
                    "'groups_with_access.group_id': ?3 " +
                    "} }," +

                    "{ $project: { " +
                    "_id: 1 " +
                    "} }," +

                    "{ $limit: 1 } " +

                    "], " +
                    "as: 'tripInfo' " +
                    "} }",

            "{ $match: { $expr: { $gt: [ { $size: '$tripInfo' }, 0 ] } } }",

            "{ $lookup: { " +
                    "from: 'patient', " +
                    "localField: '_id', " +
                    "foreignField: '_id', " +
                    "as: 'patientInfo' " +
                    "} }",

            "{ $unwind: '$patientInfo' }",

            "{ $facet: { " +
                    "total: [ " +
                    "{ $count: 'count' } " +
                    "], " +

                    "male: [ " +
                    "{ $match: { " +
                    "'patientInfo.sex': 'MALE' " +
                    "} }, " +
                    "{ $count: 'count' } " +
                    "], " +

                    "female: [ " +
                    "{ $match: { " +
                    "'patientInfo.sex': 'FEMALE' " +
                    "} }, " +
                    "{ $count: 'count' } " +
                    "], " +

                    "child: [ " +
                    "{ $match: { " +
                    "'patientInfo.age_type': 'CHILD' " +
                    "} }, " +
                    "{ $count: 'count' } " +
                    "], " +

                    "adult: [ " +
                    "{ $match: { " +
                    "'patientInfo.age_type': 'ADULT' " +
                    "} }, " +
                    "{ $count: 'count' } " +
                    "] " +

                    "} }",

            "{ $project: { " +
                    "total: { " +
                    "$ifNull: [ " +
                    "{ $arrayElemAt: ['$total.count', 0] }, " +
                    "0" +
                    "] " +
                    "}, " +

                    "male: { " +
                    "$ifNull: [ " +
                    "{ $arrayElemAt: ['$male.count', 0] }, " +
                    "0" +
                    "] " +
                    "}, " +

                    "female: { " +
                    "$ifNull: [ " +
                    "{ $arrayElemAt: ['$female.count', 0] }, " +
                    "0" +
                    "] " +
                    "}, " +

                    "child: { " +
                    "$ifNull: [ " +
                    "{ $arrayElemAt: ['$child.count', 0] }, " +
                    "0" +
                    "] " +
                    "}, " +

                    "adult: { " +
                    "$ifNull: [ " +
                    "{ $arrayElemAt: ['$adult.count', 0] }, " +
                    "0" +
                    "] " +
                    "}, " +

                    "_id: 0 " +
                    "} }"
    })
    PatientStats getPatientStatistics(
            ObjectId moduleId,
            ObjectId areaId,
            ObjectId tripId,
            ObjectId groupId
    );

    @Aggregation(pipeline = {
            "{ $match: { " +
                    "$or: [ " +
                    "{ $expr: { $eq: [?4, null] } }, " +
                    "{ $expr: { $eq: ['$area_id', ?4] } } " +
                    "] " +
                    "} }",

            "{ $group: { " +
                    "_id: '$patient_id', " +
                    "uniqueArea: { $addToSet: '$area_id' }, " +
                    "acceptedSections: { " +
                    "$sum: { " +
                    "$size: { " +
                    "$filter: { " +
                    "input: { $ifNull: ['$referrals', []] }, " +
                    "as: 'referral', " +
                    "cond: { $eq: ['$$referral.recepted', true] } " +
                    "} " +
                    "} " +
                    "} " +
                    "} " +
                    "} }",

            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaIds: '$uniqueArea' }, " +
                    "pipeline: [ " +

                    "{ $match: { " +
                    "'groups_with_access.group_id': ?2 " +
                    "} }, " +

                    "{ $unwind: '$areas' }, " +

                    "{ $match: { " +
                    "$and: [ " +

                    "{ $expr: { " +
                    "$in: ['$areas._id', '$$areaIds'] " +
                    "} }, " +

                    "{ $or: [ " +
                    "{ $expr: { $eq: [?3, null] } }, " +
                    "{ $expr: { $eq: ['$_id', ?3] } } " +
                    "] } " +

                    "] " +
                    "} }, " +

                    "{ $project: { " +
                    "_id: 0, " +
                    "areaId: '$areas._id', " +
                    "areaName: '$areas.name', " +
                    "tripName: '$name' " +
                    "} } " +

                    "], " +
                    "as: 'tripInfo' " +
                    "} }",

            "{ $match: { " +
                    "$expr: { $gt: [{ $size: '$tripInfo' }, 0] } " +
                    "} }",

            "{ $limit: 10 }",

            "{ $lookup: { " +
                    "from: 'patient', " +
                    "localField: '_id', " +
                    "foreignField: '_id', " +
                    "as: 'patientInfo' " +
                    "} }",

            "{ $unwind: '$patientInfo' }",

            "{ $project: { " +
                    "acceptedSections: 1, " +
                    "tripInfo: 1, " +
                    "'patientInfo._id': 1, " +
                    "'patientInfo.name': 1, " +
                    "'patientInfo.identifier': 1, " +
                    "'patientInfo.phone': 1, " +
                    "'patientInfo.birth_date': 1, " +
                    "'patientInfo.insurance': 1, " +
                    "'patientInfo.patient_no': 1, " +
                    "_id: 0 " +
                    "} }"
    })
    List<PatientAreaReport> getGroupPatients(
            int skip, int limit,
            ObjectId groupId, ObjectId tripId,
            ObjectId areaId
    );


    @Aggregation(pipeline = {
            "{ $match: { " +
                    "$or: [ " +
                    "{ $expr: { $eq: [?2, null] } }, " +
                    "{ $expr: { $eq: ['$area_id', ?2] } } " +
                    "] " +
                    "} }",

            "{ $group: { " +
                    "_id: '$patient_id', " +
                    "uniqueArea: { $addToSet: '$area_id' } " +
                    "} }",

            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaIds: '$uniqueArea' }, " +
                    "pipeline: [ " +

                    "{ $match: { " +
                    "'groups_with_access.group_id': ?0 " +
                    "} }, " +

                    "{ $unwind: '$areas' }, " +

                    "{ $match: { " +
                    "$and: [ " +
                    "{ $expr: { " +
                    "$in: ['$areas._id', '$$areaIds'] " +
                    "} }, " +
                    "{ $or: [ " +
                    "{ $expr: { $eq: [?1, null] } }, " +
                    "{ $expr: { $eq: ['$_id', ?1] } } " +
                    "] } " +
                    "] " +
                    "} }, " +

                    "{ $project: { " +
                    "_id: 1 " +
                    "} } " +

                    "], " +
                    "as: 'tripInfo' " +
                    "} }",

            "{ $match: { " +
                    "$expr: { $gt: [{ $size: '$tripInfo' }, 0] } " +
                    "} }",

            "{ $count: 'total' }"
    })
    Long getGroupPatientsCount(
            ObjectId groupId, ObjectId tripId,
            ObjectId areaId
    );


    @Aggregation(pipeline = {
            "{ $match: { " +
                    "$expr: { " +
                    "$and: [ " +
                    "{ $in: [?0, { $ifNull: ['$referrals.module_id', []] }] }, " +
                    "{ $or: [ " +
                    "{ $eq: [?1, null] }, " +
                    "{ $eq: ['$area_id', ?1] } " +
                    "] } " +
                    "] " +
                    "} " +
                    "} }",

            "{ $unwind: { " +
                    "path: '$referrals', " +
                    "preserveNullAndEmptyArrays: false " +
                    "} }",

            "{ $match: { " +
                    "$expr: { " +
                    "$and: [ " +
                    "{ $eq: ['$referrals.module_id', ?0] }, " +
                    "{ $gt: [{ $size: { $ifNull: ['$referrals.forms', []] } }, 0] } " +
                    "] " +
                    "} " +
                    "} }",

            "{ $lookup: { " +
                    "from: 'trip', " +
                    "let: { areaId: '$area_id' }, " +
                    "pipeline: [ " +
                    "{ $unwind: '$areas' }," +
                    "{ $match: { " +
                    "$expr: { " +
                    "$eq: ['$areas._id', '$$areaId'] " +
                    "} " +
                    "} }," +

                    "{ $match: { " +
                    "$or: [ " +
                    "{ $expr: { $eq: [?2, null] } }, " +
                    "{ $expr: { $eq: ['$_id', ?2] } } " +
                    "] " +
                    "} }," +

                    "{ $match: { " +
                    "'groups_with_access.group_id': ?3 " +
                    "} }," +

                    "{ $project: { " +
                    "_id: 1 " +
                    "} }," +

                    "{ $limit: 1 } " +

                    "], " +
                    "as: 'tripInfo' " +
                    "} }",

            "{ $match: { $expr: { $gt: [ { $size: '$tripInfo' }, 0 ] } } }",

            "{ $unwind: '$referrals.forms' }",

            "{ $unwind: '$referrals.forms.answers' }",

            "{ $match: { 'referrals.forms.answers.question_id': { $in: ?4 } } }",

            "{ $group: { " +
                    "_id: '$referrals.forms.answers.question_id', " +
                    "answers: { $push: '$referrals.forms.answers.answer' } " +
                    "} }",
    })
    List<GroupedAnswer> getPatientsAnswers(
            ObjectId moduleId,
            ObjectId areaId,
            ObjectId tripId,
            ObjectId groupId,
            Set<ObjectId> questionIds
    );
}
