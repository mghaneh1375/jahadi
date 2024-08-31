package four.group.jahadi.Repository;

import four.group.jahadi.Models.City;
import four.group.jahadi.Models.Country;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, ObjectId>, FilterableRepository<Country> {
    @Query(value = "{'name': ?0}")
    Optional<Country> findByName(String name);
}