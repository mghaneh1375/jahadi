package four.group.jahadi.Repository;

import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Models.Patient;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PatientRepository extends MongoRepository<Patient, ObjectId>, FilterableRepository<Patient> {

    @Query(value = "{identifier: ?0, identifierType: ?1}", count = true)
    Integer countByIdentifierAndIdentifierType(String identifier, IdentifierType identifierType);

    @Query(value = "{identifier: ?0, identifierType: ?1}")
    Optional<Patient> findByIdentifierAndIdentifierType(String identifier, IdentifierType identifierType);

}
