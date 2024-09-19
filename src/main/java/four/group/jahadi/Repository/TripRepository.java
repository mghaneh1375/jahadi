package four.group.jahadi.Repository;

import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId>, FilterableRepository<Trip> {

    @Query(value = "{ 'groupsWithAccess.groupId': ?0 }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @Query(value = "{'groupsWithAccess.groupId': ?0}", fields = "{'name': 1, 'areas.name': 1, 'projectId': 1, 'groupsWithAccess': 1}")
    List<Trip> findByGroupId(ObjectId groupId);

    @Query(value = "{'projectId': ?0}", delete = true)
    void deleteTripByProjectId(ObjectId projectId);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}]  }")
    List<Trip> findActives(Date curr);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesProjectIdsByAreaOwnerId(Date curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesOrNotStartedProjectIdsByAreaOwnerId(Date curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'startAt': {$exists: false}, 'projectId': ?2}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findNeedActionByGroupId(Date curr, ObjectId groupId, ObjectId projectId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjectsByGroupId(Date curr, ObjectId groupId);

    @Query(value = "{$and: [{$or: [{'endAt': {$exists: false}}, {'endAt': {$gte: ?0}}]}, {'groupsWithAccess': { $elemMatch: {'groupId': ?1, 'writeAccess': true} }} ] }", exists = true)
    boolean hasExistActiveAreaByGroupIdAndAreaIdAndWriteAccess(Date curr, ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{$or: [{'endAt': {$exists: false}}, {'endAt': {$gte: ?0}}]}, {'groupsWithAccess': { $elemMatch: {'groupId': ?1, 'writeAccess': true} }} ] }", fields = "{'name': 1, 'areas.name': 1, 'areas.id': 1}")
    Optional<Trip> findActiveAreaByGroupIdAndAreaIdAndWriteAccess(Date curr, ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjects(Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'startAt': {$gte: ?0}, 'id': ?1, 'ownerId': ?2 } } }")
    Optional<Trip> findNotStartedByAreaOwnerId(Date curr, ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }",
            fields = "{'groupsWithAccess': false, 'projectId':  false, " +
                    "'createdAt':  false, 'areas.members': false, " +
                    "'areas.country': false, 'areas.city': false, " +
                    "'areas.state': false, 'areas.lat': false, " +
                    "'areas.lng': false, 'areas.dispatchers': false, 'areas.modules': false}"
    )
    List<Trip> findNotFinishedByAreaOwnerId(Date curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", exists = true)
    Boolean existNotFinishedByAreaOwnerId(Date curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.members': ?1}]  }", exists = true)
    Boolean existNotFinishedByResponsibleId(Date curr, ObjectId userId);

    @Query(value = "{'areas': {$elemMatch: {'id': ?0, 'ownerId': ?1}} }", fields = "{'areas.members': true, 'areas.id': true, 'areas.ownerId': 1}")
    Optional<Trip> getMembersByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{'areas': {$elemMatch: {'id': ?0, 'ownerId': ?1}} }")
    Optional<Trip> findByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{'areas': {$elemMatch: {'finished': true, 'id': ?0, 'modules.moduleId': ?2, $or: [{'ownerId': ?1}, {'members': ?1}] }} }")
    Optional<Trip> findByAreaIdAndResponsibleIdAndModuleId(ObjectId areaId, ObjectId userId, ObjectId moduleId);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'insurancers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndInsurancerId(ObjectId areaId, ObjectId userId, Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'laboratoryManager': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndLaboratoryManager(ObjectId areaId, ObjectId userId, Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'pharmacyManager': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndPharmacyManager(ObjectId areaId, ObjectId userId, Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'trainers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndTrainerId(ObjectId areaId, ObjectId userId, Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'members': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId, Date curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'id': ?0, $or: [{'ownerId': ?1}, {'members': ?1}] }} }")
    Optional<Trip> findByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId);

    @Query(value = "{ 'areas': {$elemMatch: {'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'dispatchers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndDispatcherId(ObjectId areaId, ObjectId userId, Date curr);
}
