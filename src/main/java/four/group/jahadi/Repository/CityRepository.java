package four.group.jahadi.Repository;

import four.group.jahadi.Models.City;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "City")
public interface CityRepository extends MongoRepository<City, ObjectId>, FilterableRepository<City> {

    @Query(value = "{'stateId': ?0}")
    List<City> findByStateId(ObjectId stateId);

}