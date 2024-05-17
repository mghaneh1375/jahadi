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

    @Query(value = "{$and: [{$or: [{$and: [{'startAt': {$lte: ?0}}, {'endAt': {$gte: ?0}}]}, {'startAt': {$exists: false}}]}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesByGroupId(Date curr, ObjectId groupId);

    @Query(value = "{$and: [{'startAt': {$exists: false}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findNeedActionByGroupId(Date curr, ObjectId groupId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}, {'groupsWithAccess.groupId': ?1}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjectsByGroupId(Date curr, ObjectId groupId);

    @Query(value = "{$and: [{'endAt': {$exists: true}}, {'endAt': {$gte: ?0}}]  }", fields = "{'projectId': false, 'areas.members': false, 'createdAt': false}")
    List<Trip> findActivesOrNotStartedProjects(Date curr);

    @Query(value = "{$and: [{'startAt': {$gte: ?0}}, {'areas.id': ?1}, {'areas.ownerId': ?2}]  }")
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
    Boolean existNotFinishedResponsibleId(Date curr, ObjectId userId);

    @Query(value = "{'areas.id': ?0, 'areas.ownerId': ?1}", fields = "{'areas.members': true, 'areas.id': true}")
    Optional<Trip> getMembersByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    //todo : wrong query
    @Query(value = "{'areas.id': ?0, 'areas.ownerId': ?1}")
    Optional<Trip> findByAreaIdAndOwnerId(ObjectId areaId, ObjectId areaOwnerId);

    @Query(value = "{$and: [{'areas.finished': true}, {'areas.id': ?0}, {'areas.modules.moduleId': ?2}, {$or: [{'areas.ownerId': ?1}, {'areas.members': ?1}]}]  }")
    Optional<Trip> findByAreaIdAndResponsibleIdAndModuleId(ObjectId areaId, ObjectId userId, ObjectId moduleId);

    @Query(value = "{$and: [{'areas.id': ?0}, {$or: [{'areas.ownerId': ?1}, {'areas.members': ?1}]}]  }")
    Optional<Trip> findByAreaIdAndResponsibleId(ObjectId areaId, ObjectId userId);

    @Query(value = "{$and: [{'areas.startAt': {$lte: ?2}}, {'areas.endAt': {$gte: ?2}}, {'areas.id': ?0}, {$or: [{'areas.ownerId': ?1}, {'areas.dispatchers': ?1}]}]  }")
    Optional<Trip> findActiveByAreaIdAndDispatcherId(ObjectId areaId, ObjectId userId, Date curr);
}
