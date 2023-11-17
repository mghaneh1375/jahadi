package four.group.jahadi.Repository;

import four.group.jahadi.Models.Country;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends MongoRepository<Country, ObjectId>, FilterableRepository<Country> {}