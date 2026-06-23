package four.group.jahadi.Repository;

import four.group.jahadi.DTO.dashboard.PerProvinceData;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
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

    @Query(value = "{'groupsWithAccess.groupId': ?0}", fields = "{'name': 1, 'areas.name': 1, 'areas.id': 1, 'projectId': 1, 'groupsWithAccess': 1}")
    List<Trip> findByGroupId(ObjectId groupId);

    @Query(value = "{'groupsWithAccess.groupId': ?0}", fields = "{'name': 1, 'areas.name': 1, 'areas.id': 1, 'projectId': 1, 'groupsWithAccess': 1}", sort = "{'_id': 1}")
    Page<Trip> findByGroupIdWithPagination(ObjectId groupId, Pageable pageable);

    @Query(value = "{'groupsWithAccess.groupId': ?0, 'areas.id': ?1}")
    Optional<Trip> findByGroupIdAndAreaId(ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{'groupsWithAccess': { $elemMatch: {'groupId': ?0, 'writeAccess': true} }}, {'areas.id': ?1}]}", exists = true)
    boolean existByGroupIdAndAreaId(ObjectId groupId, ObjectId areaId);

    @Query(value = "{'projectId': ?0}")
    List<Trip> findTripByProjectId(ObjectId projectId);

    @Query(value = "{'projectId': ?0}", fields = "{'areas': 0}")
    List<Trip> findTripExcludeAreaByProjectId(ObjectId projectId);

    @Query(value = "{'projectId': {$in: ?0}}", fields = "{_id: 1, projectId: 1}")
    List<Trip> findTripByProjectIds(List<ObjectId> projectIds);

    @Query(value = "{'projectId': ?0, '_id': ?1}")
    Optional<Trip> findTripByProjectIdAndId(ObjectId projectId, ObjectId id);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}]  }")
    List<Trip> findActiveTrips(LocalDateTime curr);

    @Query(value = "{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesProjectIdsByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", fields = "{projectId: true}")
    List<Trip> findActivesOrNotStartedProjectIdsByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{$or: [{'startAt': {$exists: false}}, {'areas.ownerId': {$exists: false}}]}, {'projectId': ?2}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findNeedActionByGroupId(LocalDateTime curr, ObjectId groupId, ObjectId projectId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}]  }", fields = "{'areas.id': 1, 'areas.name': 1}")
    List<Trip> findDigestInfoProjectsByGroupId(ObjectId groupId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}, {'_id': ?1}]  }", fields = "{'areas.id': 1, 'areas.name': 1}")
    List<Trip> findDigestInfoProjectsByGroupIdAndTripId(ObjectId groupId, ObjectId tripId);

    @Query(value = "{$and: [{'groupsWithAccess.groupId': ?0}]  }", fields = "{'_id': 1, 'name': 1, 'startAt': 1, 'endAt': 1}")
    List<Trip> findDigestTripInfoProjectsByGroupId(ObjectId groupId);

    @Query(value = "{$and: [{'areas': {$elemMatch: { 'id': ?2, $or: [{'endAt': {$exists: false}}, {'endAt': {$gte: ?0}}] } } }, {'groupsWithAccess': { $elemMatch: {'groupId': ?1, 'writeAccess': true} }} ] }", fields = "{'name': 1, 'areas.name': 1, 'areas.id': 1}")
    Optional<Trip> findActiveAreaByGroupIdAndAreaIdAndWriteAccess(LocalDateTime curr, ObjectId groupId, ObjectId areaId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjects(LocalDateTime curr);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}]  }", fields = "{'name': 1, 'groups_with_access': 1, 'areas.ownerId': 1, 'areas.city': 1, 'areas.name': 1}")
    List<Trip> findActivesOrNotStartedProjects2(LocalDateTime curr);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'name': 1, 'groups_with_access': 1, 'areas.ownerId': 1, 'areas.city': 1, 'areas.name': 1}")
    List<Trip> findActivesOrNotStartedProjectsByGroupId(LocalDateTime curr, ObjectId groupId);
    @Aggregation(pipeline = {
            "{ $project: { areaCount: { $size: { $ifNull: ['$areas', []] } } } }",
            "{ $group: { _id: null, totalAreas: { $sum: '$areaCount' } } }",
            "{ $project: { _id: 0, totalAreas: 1 } }"
    })
    Long getTotalAreasCount();

    @Aggregation(pipeline = {
            "{ $match: { $or: [ { start_at: { $gte: ?0 } }, { end_at: { $gte: ?0 } } ] } }",
            "{ $project: { areaCount: { $size: { $ifNull: ['$areas', []] } } } }",
            "{ $group: { _id: null, totalAreas: { $sum: '$areaCount' } } }",
            "{ $project: { _id: 0, totalAreas: 1 } }"
    })
    Long getTotalAreasCountInLastMonth(LocalDateTime oneMonthAgo);

    @Query(value = "{ $or: [ { start_at: { $gte: ?0 } }, { end_at: { $gte: ?0 } } ] }", count = true)
    Long getTotalTripsCountInLastMonth(LocalDateTime oneMonthAgo);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}] }",
            fields = "{'groupsWithAccess': false, 'projectId':  false, " +
                    "'createdAt':  false, 'areas.members': false, " +
                    "'areas.country': false, 'areas.city': false, " +
                    "'areas.state': false, 'areas.lat': false, 'areas.dates': false, " +
                    "'areas.lng': false} "
    )
    List<Trip> findNotFinishedByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.members': ?1}] }",
            fields = "{'groupsWithAccess': false, 'projectId':  false, " +
                    "'createdAt':  false, " +
                    "'areas.cityId': false, 'areas.dates': false, " +
                    "'areas.stateId': false } "
    )
    List<Trip> findNotFinishedByMemberId(LocalDateTime curr, ObjectId memberId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.ownerId': ?1}]  }", exists = true)
    Boolean existNotFinishedByAreaOwnerId(LocalDateTime curr, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'endAt': {$gte: ?0}}, {'areas.members': ?1}]  }", exists = true)
    Boolean existNotFinishedByResponsibleId(LocalDateTime curr, ObjectId userId);

    @Query(
            value = "{'areas': {$elemMatch: {'id': ?0, 'ownerId': ?1}} }",
            fields = "{'areas.members': true, 'areas.id': true, 'areas.ownerId': 1, 'areas.modules': 1, 'areas.trainers': 1, 'areas.insurancers': 1, 'areas.dispatchers': 1, 'areas.pharmacyManagers': 1, 'areas.equipmentManagers': 1, 'areas.laboratoryManager': 1}"
    )
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

    @Query(value = "{ 'areas': {$elemMatch: { $and: [{'finished': true}, {'startAt': {$lte: ?2}}, {'endAt': {$gte: ?2}}, {'id': ?0}, {$or: [{'ownerId': ?1}, {'members': ?1}]}] } } }")
    Optional<Trip> findActiveByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId, LocalDateTime curr);

    @Query(value = "{ 'areas': {$elemMatch: { 'id': ?0, $or: [{'ownerId': ?1}, {'members': ?1}] }} }")
    Optional<Trip> findByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId);

    @Query(value = "{ 'areas': {$elemMatch: {'startAt': {$lte: ?2}, 'endAt': {$gte: ?2}, 'id': ?0, $or: [{'ownerId': ?1}, {'dispatchers': ?1}] } } }")
    Optional<Trip> findActiveByAreaIdAndDispatcherId(ObjectId areaId, ObjectId userId, LocalDateTime curr);


    @Aggregation(pipeline = {
            "{ $match: { $or: [ " +
                    "  { start_at: { $lte: ?1 }, end_at: { $gte: ?0 } }, " +
                    "  { start_at: { $gte: ?0, $lte: ?1 } }, " +
                    "  { end_at: { $gte: ?0, $lte: ?1 } } " +
                    "] } }",
            "{ $unwind: '$areas' }",
            "{ $match: { $or: [ " +
                    "  { 'areas.start_at': { $lte: ?1 }, 'areas.end_at': { $gte: ?0 } }, " +
                    "  { 'areas.start_at': { $gte: ?0, $lte: ?1 } }, " +
                    "  { 'areas.end_at': { $gte: ?0, $lte: ?1 } } " +
                    "] } }",
            "{ $group: { " +
                    "  _id: '$areas.state', " +
                    "  tripCount: { $addToSet: '$_id' } " +
                    "} }",
            "{ $project: { " +
                    "  _id: 0, " +
                    "  province: '$_id', " +
                    "  count: { $size: '$tripCount' } " +
                    "} }",
            "{ $sort: { count: -1 } }"
    })
    List<PerProvinceData> countTripsByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(pipeline = {
            "{ $match: { $or: [ " +
                    "  { start_at: { $lte: ?1 }, end_at: { $gte: ?0 } }, " +
                    "  { start_at: { $gte: ?0, $lte: ?1 } }, " +
                    "  { end_at: { $gte: ?0, $lte: ?1 } } " +
                    "] } }",
            "{ $unwind: '$areas' }",
            "{ $match: { $or: [ " +
                    "  { 'areas.start_at': { $lte: ?1 }, 'areas.end_at': { $gte: ?0 } }, " +
                    "  { 'areas.start_at': { $gte: ?0, $lte: ?1 } }, " +
                    "  { 'areas.end_at': { $gte: ?0, $lte: ?1 } } " +
                    "] } }",
            "{ $unwind: '$areas.members' }",
            "{ $group: { " +
                    "  _id: { " +
                    "    state: '$areas.state', " +
                    "    member: '$areas.members' " +
                    "  } " +
                    "} }",
            "{ $group: { " +
                    "  _id: '$_id.state', " +
                    "  memberCount: { $sum: 1 } " +
                    "} }",
            "{ $project: { " +
                    "  _id: 0, " +
                    "  province: '$_id', " +
                    "  count: '$memberCount' " +
                    "} }",
            "{ $sort: { count: -1 } }"
    })
    List<PerProvinceData> countMembersByStateInDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
