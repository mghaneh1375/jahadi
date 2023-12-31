package four.group.jahadi.Repository;

import four.group.jahadi.Models.State;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends MongoRepository<State, ObjectId>, FilterableRepository<State> {

    @Query(value = "{'countryId': ?0}")
    List<State> findByCountryId(ObjectId countryId);

}