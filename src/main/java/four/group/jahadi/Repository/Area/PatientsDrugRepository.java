package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Area.PatientsInArea;
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
//            "{ $: ?14 }",
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

    @Query(value = "{_id: ?0, doctorId: ?1, dedicated: false}", delete = true)
    Optional<PatientDrug> deleteUnDedicatedPatientDrugByIdAAndDoctorId(ObjectId id, ObjectId doctorId);

}
