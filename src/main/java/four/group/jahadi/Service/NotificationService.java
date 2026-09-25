package four.group.jahadi.Service;


import four.group.jahadi.DTO.Area.NotificationData;
import four.group.jahadi.DTO.NotificationDto;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Notification;
import four.group.jahadi.Repository.NotificationRepository;
import four.group.jahadi.Repository.impl.NotificationCustomRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static four.group.jahadi.Utility.StaticValues.JSON_NOT_ACCESS;
import static four.group.jahadi.Utility.StaticValues.JSON_OK;


@Service
public class NotificationService
        extends AbstractService<Notification, NotificationData> {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationCustomRepositoryImpl notificationCustomRepository;

    public ResponseEntity<Page<NotificationDto>> userNotifications(
            ObjectId userId,
            Boolean seenStatus,
            int pageIndex,
            int pageSize
    ) {
        return new ResponseEntity<>(
                notificationCustomRepository.findUserNotifications(
                        userId,
                        seenStatus,
                        Pageable.ofSize(pageSize)
                                .withPage(pageIndex)
                ),
                HttpStatus.OK
        );
    }

    @Cacheable(value = "userNotificationsCount", key = "#userId")
    public ResponseEntity<Long> userNotificationsCount(
            ObjectId userId
    ) {
        return new ResponseEntity<>(
                notificationCustomRepository.countUnreadNotifications(
                        userId
                ),
                HttpStatus.OK
        );
    }


    @Override
    public ResponseEntity<Page<Notification>> paginateList(
            int pageIndex,
            int pageSize,
            Object... filters
    ) {
        return new ResponseEntity<>(
                notificationRepository.findByAreaId(
                        (ObjectId) filters[0],
                        Pageable.ofSize(pageSize)
                                .withPage(pageIndex)
                ),
                HttpStatus.OK
        );
    }


    @Override
    @CacheEvict(value = "userNotificationsCount", allEntries = true)
    public ResponseEntity<Notification> store(
            NotificationData dto,
            Object... params
    ) {
        ObjectId areaId = (ObjectId) params[0];

        Notification notification = new Notification();
        notification.setAreaId(areaId);

        notification.setUserId(
                (ObjectId) params[1]
        );

        notification.setTitle(dto.getTitle());
        notification.setDescription(dto.getDescription());
        notification.setType(dto.getType());

        notification.setUpdatedAt(LocalDateTime.now());

        return new ResponseEntity<>(
                notificationRepository.save(notification),
                HttpStatus.OK
        );

    }


    @Override
    @CacheEvict(value = "userNotificationsCount", allEntries = true)
    public void update(
            ObjectId id,
            NotificationData dto,
            Object... params
    ) {
        Notification notification =
                notificationRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (!notification.getUserId().equals(params[0]))
            throw new NotAccessException();

        notification.setTitle(dto.getTitle());
        notification.setDescription(dto.getDescription());
        notification.setType(dto.getType());
        notification.setUpdatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }


    @Override
    public ResponseEntity<Notification> findById(
            ObjectId id,
            Object... params
    ) {
        Notification notification =
                notificationRepository.findById(id).orElseThrow(InvalidIdException::new);

        return new ResponseEntity<>(
                notification,
                HttpStatus.OK
        );
    }


    @CacheEvict(value = "userNotificationsCount", allEntries = true)
    public String remove(ObjectId id, ObjectId userId) {
        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(
                                InvalidIdException::new
                        );


        if (!notification.getUserId().equals(userId))
            return JSON_NOT_ACCESS;

        notificationRepository.delete(notification);
        return JSON_OK;
    }


    @CacheEvict(value = "userNotificationsCount", key = "#jahadiId")
    public void markAsSeen(ObjectId notificationId, ObjectId jahadiId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(
                                InvalidIdException::new
                        );

        if (!notification.getSeenBy().contains(jahadiId)) {
            notification.getSeenBy().add(jahadiId);
            notificationRepository.save(notification);
        }
    }

}