package four.group.jahadi.Repository;


import four.group.jahadi.Models.Area.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, ObjectId>, FilterableRepository<Notification> {


    @Query(
            value = "{area_id:?0}",
            sort = "{created_at:-1}"
    )
    Page<Notification> findByAreaId(
            ObjectId areaId,
            Pageable pageable
    );


    @Query(
            value = "{user_id:?0}",
            sort = "{created_at:-1}"
    )
    Page<Notification> findByUserId(
            ObjectId userId,
            Pageable pageable
    );


    @Query(
            value = "{area_id:?0, seen_by: {$ne:?1}}"
    )
    List<Notification> findUnread(
            ObjectId areaId,
            ObjectId userId
    );


    @Query(
            value = "{_id:{$in:?0}}",
            delete = true
    )
    void deleteByIdsIn(
            List<ObjectId> ids
    );

}