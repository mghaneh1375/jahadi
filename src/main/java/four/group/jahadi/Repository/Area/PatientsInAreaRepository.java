package four.group.jahadi.Repository.Area;

import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Repository.FilterableRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientsInAreaRepository extends MongoRepository<PatientsInArea, ObjectId>, FilterableRepository<PatientsInArea> {

    @Query(value = "{areaId: ?0}", count = true)
    Integer countByAreaId(ObjectId areaId);

    @Query(value = "{areaId: ?0, patientId: ?1}", exists = true)
    Boolean existByAreaIdAndPatientId(ObjectId areaId, ObjectId patientId);

    @Aggregation(pipeline = {
            "{$match: {areaId: ?0}}",
            "{$lookup: {from: 'patient', localField: 'id', foreignField: 'patients_in_area.patientId', as: 'patients'}}",
            "{$unwind: '$patients'}",
            "{$project: {'identifier': '$patients.identifier'}}",
    })
    List<Patient> findPatientsIdentifierByAreaId(ObjectId areaId);


}
