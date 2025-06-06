package four.group.jahadi.Repository;

import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId>, FilterableRepository<Trip> {

    @Query(value = "{ $and: [{'groupsWithAccess.groupId': ?0}, {'startAt': {$lte: ?1}}, {'endAt': {$gte: ?1}}] }", fields = "{'name': 1, 'areas.name': 1, 'areas._id': 1}")
    List<Trip> findActivesByGroupId(ObjectId groupId, LocalDateTime curr);

    @Query(value = "{ 'groupsWithAccess.groupId': ?0 }", count = true)
    Integer countByGroupId(ObjectId groupId);

    @Query(value = "{'groupsWithAccess.groupId': ?0}", fields = "{'name': 1, 'areas.name': 1, 'projectId': 1, 'groupsWithAccess': 1}")
    List<Trip> findByGroupId(ObjectId groupId);

    @Query(value = "{'groupsWithAccess.groupId': ?0, 'areas.id': ?1}")
    Optional<Trip> findByGroupIdAndAreaId(ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{'groupsWithAccess': { $elemMatch: {'groupId': ?0, 'writeAccess': true} }}, {'areas.id': ?1}]}", exists = true)
    boolean existByGroupIdAndAreaId(ObjectId groupId, ObjectId areaId);

    @Query(value = "{'projectId': ?0}")
    List<Trip> findTripByProjectId(ObjectId projectId);

    @Query(value = "{'projectId': ?0, '_id': ?1}")
    Optional<Trip> findTripByProjectIdAndId(ObjectId projectId, ObjectId id);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}]  }")
    List<Trip> findActives(LocalDateTime curr);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesProjectIdsByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesOrNotStartedProjectIdsByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'startAt': {$exists: false}, 'projectId': ?2}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findNeedActionByGroupId(LocalDateTime curr, ObjectId groupId, ObjectId projectId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjectsByGroupId(LocalDateTime curr, ObjectId groupId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}]  }", fields = "{'areas.id': 1, 'areas.name': 1}")
    List<Trip> findDigestInfoProjectsByGroupId(ObjectId groupId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}, {'_id': ?1}]  }", fields = "{'areas.id': 1, 'areas.name': 1}")
    List<Trip> findDigestInfoProjectsByGroupIdAndTripId(ObjectId groupId, ObjectId tripId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}]  }", fields = "{'_id': 1, 'name': 1}")
    List<Trip> findDigestTripInfoProjectsByGroupId(ObjectId groupId);

    @Query(value = "{$and: [{'areas': {$elemMatch: { 'id': ?2, $or: [{'endAt': {$exists: false}}, {'endAt': {$gte: ?0}}] } } }, {'groupsWithAccess': { $elemMatch: {'groupId': ?1, 'writeAccess': true} }} ] }", fields = "{'name': 1, 'areas.name': 1, 'areas.id': 1}")
    Optional<Trip> findActiveAreaByGroupIdAndAreaIdAndWriteAccess(LocalDateTime curr, ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjects(LocalDateTime curr);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}] }",
            fields = "{'groupsWithAccess': false, 'projectId':  false, " +
                    "'createdAt':  false, 'areas.members': false, " +
                    "'areas.country': false, 'areas.city': false, " +
                    "'areas.state': false, 'areas.lat': false, 'areas.dates': false, " +
                    "'areas.lng': false, 'areas.dispatchers': false, 'areas.modules': false, 'areas.experiments': false, " +
                    "'areas.trainers': false, 'areas.insurancers': false, 'areas.pharmacyManagers': false, " +
                    "'areas.equipmentManagers': false, 'areas.laboratoryManager': false } "
    )
    List<Trip> findNotFinishedByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.members': ?1}] }",
            fields = "{'groupsWithAccess': false, 'projectId':  false, " +
                    "'createdAt':  false, " +
                    "'areas.cityId': false, 'areas.dates': false, " +
                    "'areas.stateId': false, 'areas.experiments': false, " +
                    "'areas.dispatchers': false, 'areas.modules': false, " +
                    "'areas.trainers': false, 'areas.insurancers': false, 'areas.pharmacyManagers': false, " +
                    "'areas.equipmentManagers': false, 'areas.laboratoryManager': false } "
    )
    List<Trip> findNotFinishedByMemberId(LocalDateTime curr, ObjectId memberId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", exists = true)
    Boolean existNotFinishedByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.members': ?1}]  }", exists = true)
    Boolean existNotFinishedByResponsibleId(LocalDateTime curr, ObjectId userId);

    @Query(value = "{'areas': {$elemMatch: {'id': ?0, 'ownerId': ?1}} }", fields = "{'areas.members': true, 'areas.id': true, 'areas.ownerId': 1}")
    Optional<Trip> getMembersByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{'areas': {$elemMatch: {'id': ?0} } }")
    Optional<Trip> findByAreaId(ObjectId areaId);

    @Query(value = "{'areas': {$elemMatch: {'id': ?0, 'ownerId': ?1}} }")
    Optional<Trip> findByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{'areas': {$elemMatch: {'finished': true, 'id': ?0, 'modules.moduleId': ?2, $or: [{'ownerId': ?1}, {'members': ?1}] }} }")
    Optional<Trip> findByAreaIdAndResponsibleIdAndModuleId(ObjectId areaId, ObjectId userId, ObjectId moduleId);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'insurancers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndInsurancerId(ObjectId areaId, ObjectId userId, LocalDateTime curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'pharmacyManagers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndPharmacyManager(ObjectId areaId, ObjectId userId, LocalDateTime curr);
    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'equipmentManagers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndEquipmentManager(ObjectId areaId, ObjectId userId, LocalDateTime curr);
    @Query(value = "{ 'areas': {$elemMatch: { $and: [{'finished': true}, {'startAt': {$lte: ?2}}, {'endAt': {$gte: ?2}}, {'id': ?0}, {$or: [{'ownerId': ?1}, {'trainers': ?1}]} ] } } }")
    Optional<Trip> findActiveByAreaIdAndTrainerId(ObjectId areaId, ObjectId userId, LocalDateTime curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'finished': true, 'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'members': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId, LocalDateTime curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'id': ?0, $or: [{'ownerId': ?1}, {'members': ?1}] }} }")
    Optional<Trip> findByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId);

    @Query(value = "{ 'areas': {$elemMatch: {'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'dispatchers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndDispatcherId(ObjectId areaId, ObjectId userId, LocalDateTime curr);
}
