package four.group.jahadi.Repository;

import four.group.jahadi.DTO.UserDigest;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Models.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>, FilterableRepository<User> {

    @Query(value = "{'_id':  ?0, 'status':  'ACTIVE', 'removeAt': null}", count = true)
    Integer countActiveBy_id(ObjectId id);

    @Query(value = "{'accesses': ?0, 'status':  'ACTIVE', 'removeAt': null}", count = true)
    Integer countUsersByAccess(String access);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'university': 1, 'pic': 1, 'color': 1, 'sex': 1  }"
    )
    List<User> findByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }")
    List<User> findFullInfoByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'pic': 1, 'color': 1 }"
    )
    List<User> findDigestByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'name': 1 }")
    List<User> findJustNameByIdsIn(List<ObjectId> ids);

    @Query(value = "{ 'groupId': ?0, 'status': 'ACTIVE' }", fields = "{ 'name': 1 }")
    List<UserDigest> findGroupActiveMembersName(ObjectId groupId);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'pic': 1, 'color': 1, 'sex': 1, 'status': 1, 'lodgment': 1 }"
    )
    List<User> findGroupUsersByIdsIn(List<ObjectId> ids);

    @Query(value = "{ $and: [{'_id': { $in: ?0 }}, {'groupId': ?1}] }", count = true)
    Integer countByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ 'groupId': ?0, 'removeAt': null }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @Query(value = "{'nid':  ?0}")
    Optional<User> findByNID(String nid);

    @Query(value = "{'_id':  ?0}", fields = "{'name': 1, '_id': 1, 'accesses': 1, 'pic': 1}")
    Optional<User> findDigestById(ObjectId id);

    @Query(value = "{'phone':  ?0}", count = true)
    Integer countByPhone(String phone);

    @Query(value = "{'nid':  ?0}", count = true)
    Integer countByNID(String nid);

    @Query(value = "{$and: [{'groupId': ?0}, {'accesses': 'GROUP'}]}", fields = "{_id: 1}")
    User findIdByGroupOwnerId(ObjectId groupId);
}
