package four.group.jahadi.Repository;

import four.group.jahadi.Models.Activation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationRepository extends MongoRepository<Activation, ObjectId>, FilterableRepository<Activation> {

    @Query(value = "{phone: ?0, created_at:  {$gt: ?1}}")
    Optional<Activation> findByPhone(String phone, long createdAt);

    @Query(value = "{phone: ?0, validated: true}")
    Optional<Activation> findByPhone(String phone);

    @Query(value = "{phone: ?0, code: ?1, token: ?2}")
    Optional<Activation> findByPhoneAndCodeAndToken(String NID, Integer code, String token);

    @Query(value = "{phone: ?0, created_at:  {$lt: ?1}}", delete = true)
    void deleteByPhone(String phone, long createdAt);

}
