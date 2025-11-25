package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.DrugAggregationModel;
import four.group.jahadi.Models.Area.PatientDrugJoinModel;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.PatientDrug;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientsDrugRepository extends MongoRepository<PatientDrug, ObjectId>, FilterableRepository<PatientsInArea> {

    @Aggregation(pipeline = {
            "{ $match:  {$and :[{'areaId': ?0}," +
                    "?#{ [1] == null ? { '_id': {$exists: true}} : { 'patient_id' : [1] } }," +
                    "?#{ [2] == null ? { '_id': {$exists: true}} : { 'module_id': [2] } }," +
                    "?#{ [3] == null ? { '_id': {$exists: true}} : { 'doctor_id': [3] } }," +
                    "?#{ [4] == null ? { '_id': {$exists: true}} : { 'dedicated': [4] } }," +
                    "?#{ [5] == null ? { '_id': {$exists: true}} : { 'drug_id': [5] } }," +
                    "?#{ [6] == null ? { '_id': {$exists: true}} : { 'created_at' : {$gte: [6]} } }," +
                    "?#{ [7] == null ? { '_id': {$exists: true}} : { 'created_at' : {$lte: [7]} } }," +
                    "?#{ [8] == null ? { '_id': {$exists: true}} : { 'give_at' : {$gte: [8]} } }," +
                    "?#{ [9] == null ? { '_id': {$exists: true}} : { 'give_at' : {$lte: [9]} } }," +
                    "?#{ [10] == null ? { '_id': {$exists: true}} : { 'suggest_count' : {$gte: [10]} } }," +
                    "?#{ [11] == null ? { '_id': {$exists: true}} : { 'suggest_count' : {$lte: [11]} } }," +
                    "?#{ [12] == null ? { '_id': {$exists: true}} : { 'giver_id': [12] } }," +
                    "]}}",
            "{ $project: {areaId: 0, doctorId: 0, giverId: 0, moduleId: 0, description: 0, giveDescription: 0, givenDrugId: 0} }",
            "{ $skip: ?13 }",
            "{ $limit: ?14 }",
    })
    List<PatientDrug> findByFilters(
            ObjectId areaId, ObjectId patientId,
            ObjectId moduleId, ObjectId doctorId,
            Boolean justGiven, ObjectId drugId,
            LocalDateTime startAdviceAt, LocalDateTime endAdviceAt,
            LocalDateTime startGiveAt, LocalDateTime endGiveAt,
            Integer startSuggestCount, Integer endSuggestCount,
            ObjectId giverId, Integer skip, Integer limit
    );

    @Aggregation(pipeline = {
            "{ $match:  {$and :[{'areaId': ?0}," +
                    "?#{ [1] == null ? { '_id': {$exists: true}} : { 'doctor_id': [1] } }," +
                    "?#{ [2] == null ? { '_id': {$exists: true}} : { 'dedicated': [2] } }," +
                    "?#{ [3] == null ? { '_id': {$exists: true}} : { 'drug_id': [3] } }," +
                    "?#{ [4] == null ? { '_id': {$exists: true}} : { 'created_at' : {$gte: [4]} } }," +
                    "?#{ [5] == null ? { '_id': {$exists: true}} : { 'created_at' : {$lte: [5]} } }," +
                    "?#{ [6] == null ? { '_id': {$exists: true}} : { 'give_at' : {$gte: [6]} } }," +
                    "?#{ [7] == null ? { '_id': {$exists: true}} : { 'give_at' : {$lte: [7]} } }," +
                    "?#{ [8] == null ? { '_id': {$exists: true}} : { 'giver_id': [8] } }," +
                    "]}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo'}}",
            "{$lookup: {from: 'patient', localField: 'patient_id', foreignField: '_id', as: 'patientInfo'}}",
            "{$lookup: {from: 'user', localField: 'doctor_id', foreignField: '_id', as: 'doctorInfo'}}",
            "{$lookup: {from: 'user', localField: 'giver_id', foreignField: '_id', as: 'giverInfo'}}",
            "{$unwind: '$drugInfo'}",
            "{$unwind: '$patientInfo'}",
            "{$unwind: '$doctorInfo'}",
            "{$unwind: {path: '$giverInfo', preserveNullAndEmptyArrays: true}}",
            "{ $project: {" +
                    "'doctorInfo': '$doctorInfo.name', " +
                    "'giverInfo': '$giverInfo.name', " +
                    "howToUse: 1, amountOfUse: 1, useTime: 1," +
                    "suggestCount: 1, giveCount: 1, description: 1," +
                    "giveDescription: 1, createdAt: 1, giveAt: 1," +
                    "'patientInfo.name': 1, " +
                    "'patientInfo.identifier': 1, " +
                    "'drugInfo.name': 1, " +
                    "'drugInfo.dose': 1, " +
                    "'drugInfo.producer': 1, " +
                    "'drugInfo.drugType': 1, " +
                    "} }",
            "{ $skip: ?9 }",
            "{ $limit: ?10 }",
    })
    List<PatientDrugJoinModel> findByFiltersJoinWithDrugAndPatient(
            ObjectId areaId, ObjectId doctorId,
            Boolean justGiven, ObjectId drugId,
            LocalDateTime startAdviceAt, LocalDateTime endAdviceAt,
            LocalDateTime startGiveAt, LocalDateTime endGiveAt,
            ObjectId giverId, Integer skip, Integer limit
    );


    @Aggregation(pipeline = {
            "{$match: {$and: [{areaId: ?0},{moduleId: ?1}]}}",
            "{$lookup: {from: 'drug', localField: 'drug_id', foreignField: '_id', as: 'drugInfo'}}",
            "{$lookup: {from: 'user', localField: 'giver_id', foreignField: '_id', as: 'giverInfo'}}",
            "{$unwind: '$drugInfo'}",
            "{$unwind: {path: '$giverInfo', preserveNullAndEmptyArrays: true}}",
            "{ $project: {" +
                    "'giverInfo': '$giverInfo.name', " +
                    "suggestCount: 1, giveCount: 1, description: 1," +
                    "patientId: 1, " +
                    "'drugInfo.name': 1, " +
                    "'drugInfo.dose': 1, " +
                    "} }",
    })
    List<PatientDrugJoinModel> findByFiltersJoinWithDrug(
            ObjectId areaId, ObjectId moduleId
    );
    @Aggregation(pipeline = {
            "{ $match:  {$and :[{'areaId': ?0}]}}",
            "{ $group: {'_id': '$drug_id', 'sumSuggestCount': { '$sum': '$suggest_count' }, 'sumGiveCount': { '$sum': '$give_count' } } }",
            "{$lookup: {from: 'drug', localField: '_id', foreignField: '_id', as: 'drugInfo'}}",
            "{$lookup: {from: 'drugs_in_area', " +
            "let: { drugId: '$_id' }," +
            "pipeline: [{" +
            "$match: {" +
            "$expr: {" +
            "$and: [" +
            "{ $eq: ['$drug_id', '$$drugId'] }," +
            "{ $eq: ['$area_id', ?0] }" +
            "]}}}]," +
            "as: 'areaDrugInfo'}}",
            "{$unwind: '$drugInfo'}",
            "{$unwind: '$areaDrugInfo'}",
            "{ $project: {" +
                    "'name': '$drugInfo.name'," +
                    "'sumSuggestCount': 1, " +
                    "'sumGiveCount': 1, " +
                    "'totalCount': '$areaDrugInfo.total_count', " +
                    "'reminder': '$areaDrugInfo.reminder', " +
                    "} }"
    })
    List<DrugAggregationModel> findSumOfSuggestPerDrug(ObjectId areaId);

    @Query(value = "{_id: ?0, doctorId: ?1, dedicated: false}", delete = true)
    Optional<PatientDrug> deleteUnDedicatedPatientDrugByIdAAndDoctorId(ObjectId id, ObjectId doctorId);

    @Query(value = "{$or: [{drugId: {$in: ?0}}, {givenDrugId: {$in: ?0}}]}", fields = "{drugId: 1, givenDrugId: 1}")
    List<PatientDrug> findAllByDrugIdIsInOrGivenDrugIdIn(List<ObjectId> ids);

}
