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

    @Query(value = "{'_id':  ?0, 'status':  'ACTIVE'}", count = true)
    Integer countActiveBy_id(ObjectId id);

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'name': 1, 'phone': 1, 'pic': 1, 'color': 1  }")
    List<User> findBy_idIn(List<ObjectId> ids);

    @Query(value = "{ $and: [{'_id': { $in: ?0 }}, {'groupId': ?1}] }", count = true)
    Integer countByIdsAndGroupId(List<ObjectId> ids, ObjectId groupId);

    @Query(value = "{ 'groupId': ?0 }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @NotNull
    @Query(value = "{ '_id': ?0 }", fields = "{ 'notifs': 0  }")
    Optional<User> findById(@NotNull ObjectId id);

    @Query(value = "{'nid':  ?0}")
    Optional<User> findByNID(String nid);

    @Query(value = "{'phone':  ?0}")
    Optional<User> findByPhone(String phone);

//    @Query(value = "{'removeAt':  null, $or: [ {$where: ':#{#status} == null'}, { 'status': :#{#status} } ] }")
//    List<User> findAll(@Param(value = "status") AccountStatus status, @Param(value = "access") Access access);


    @Query(value = "{$and :["
            + "?#{ [0] == null ? { $where : 'true'} : { 'status' : [0] } },"
            + "?#{ [1] == null ? { $where : 'true'} : { 'accesses' : [1] } },"
            + "?#{ [3] == null ? { $where : 'true'} : { 'nid' : [3] } },"
            + "?#{ [4] == null ? { $where : 'true'} : { 'phone' : [4] } },"
            + "?#{ [5] == null ? { $where : 'true'} : { 'sex' : [5] } },"
            + "?#{ [6] == null ? { $where : 'true'} : { 'group_name' : [6] } },"
            + "?#{ [7] == null ? { $where : 'true'} : { 'group_id' : [7] } },"
            + "?#{ [8] == null ? { $where : 'true'} : { 'members' : {$exists: [8]} } },"
            + "]}", fields = "{'id': 1, 'createdAt': 1, 'name': 1, 'nid': 1, 'phone': 1, 'sex': 1, 'groupName': 1, 'status': 1, 'accesses': 1, 'field': 1}")
    List<User> findAll(AccountStatus status, Access access, String name,
                       String NID, String phone, Sex sex, String groupName,
                       ObjectId groupId, Boolean justGroupRequests);

    @Query(value = "{'phone':  ?0}", count = true)
    Integer countByPhone(String phone);

    @Query(value = "{'nid':  ?0}", count = true)
    Integer countByNID(String nid);

}
