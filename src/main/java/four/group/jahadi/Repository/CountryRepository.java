package four.group.jahadi.Repository;

import four.group.jahadi.Models.Country;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@MyRepository(model = "Country")
public interface CountryRepository extends MongoRepository<Country, ObjectId> {
    @Query(value = "{'name': ?0}")
    Optional<Country> findByName(String name);
}