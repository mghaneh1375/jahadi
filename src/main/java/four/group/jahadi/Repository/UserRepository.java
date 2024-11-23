package four.group.jahadi.Repository;

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

    @Query(value = "{'_id':  ?0, 'status':  'ACTIVE', 'deletedAt': null}", count = true)
    Integer countActiveBy_id(ObjectId id);

    @Query(value = "{'accesses': ?0, 'status':  'ACTIVE', 'deletedAt': null}", count = true)
    Integer countUsersByAccess(String access);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'pic': 1, 'color': 1, 'sex': 1  }"
    )
    List<User> findByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'pic': 1, 'color': 1 }"
    )
    List<User> findDigestByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1 }"
    )
    List<User> findJustNameByIdsIn(List<ObjectId> ids);

    @Query(value = "{ '_id': { $in: ?0 } }",
            fields = "{ 'name': 1, 'nid': 1, 'phone': 1, 'tel': 1, 'field': 1, 'pic': 1, 'color': 1, 'sex': 1, 'status': 1, 'lodgment': 1 }"
    )
    List<User> findGroupUsersByIdsIn(List<ObjectId> ids);

    @Query(value = "{ $and: [{'_id': { $in: ?0 }}, {'groupId': ?1}] }", count = true)
    Integer countByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ 'groupId': ?0, 'deletedAt': null }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @Query(value = "{'nid':  ?0}")
    Optional<User> findByNID(String nid);

    @Query(value = "{'_id':  ?0}", fields = "{'name': 1, '_id': 1, 'accesses': 1, 'pic': 1}")
    Optional<User> findDigestById(ObjectId id);

    @Query(value = "{$and :[{'deletedAt': null},"
            + "?#{ [0] == null ? { $where : 'true'} : { 'status' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'accesses' : [1] } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'NID' : [3] } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'phone' : [4] } },"
            + "?#{ [5] == null ? { $where : 'true'} : { 'sex' : [5] } },"
            + "?#{ [6] == null ? { $where : 'true'} : { 'group_name' : [6] } },"
            + "?#{ [7] == null ? { $where : 'true'} : { 'group_id' : [7] } },"
            + "?#{ [8] == null ? { $where : 'true'} : { 'total_members' : {$exists: [8]} } },"
            + "]}", fields = "{'id': 1, 'created_at': 1, 'name': 1, 'nid': 1, 'phone': 1, 'sex': 1, " +
            "'group_name': 1, 'status': 1, 'accesses': 1, 'field': 1, 'blood_type': 1, 'university': 1, " +
            "'father_name': 1, 'birth_day': 1, 'abilities': 1, 'nearby_rel': 1, 'nearby_name': 1, 'nearby_phone': 1, " +
            "'allergies': 1, 'diseases': 1, 'group_id': 1, 'tel': 1, 'lodgment': 1, 'university_year': 1, 'lodgment_other': 1}")
    List<User> findAll(AccountStatus status, Access access, String name,
                       String NID, String phone, Sex sex, String groupName,
                       ObjectId groupId, Boolean justGroupRequests);

    @Query(value = "{'phone':  ?0}", count = true)
    Integer countByPhone(String phone);

    @Query(value = "{'nid':  ?0}", count = true)
    Integer countByNID(String nid);

}
