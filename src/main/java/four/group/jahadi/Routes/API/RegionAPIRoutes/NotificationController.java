package four.group.jahadi.Routes.API.RegionAPIRoutes;


import four.group.jahadi.DTO.Area.NotificationData;
import four.group.jahadi.DTO.NotificationDto;
import four.group.jahadi.Models.Area.Notification;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.NotificationService;

import four.group.jahadi.Validator.ObjectIdConstraint;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController extends Router {
    private final NotificationService service;

    @GetMapping("/list/{areaId}")
    public ResponseEntity<Page<Notification>> list(
            @PathVariable @NotNull @ObjectIdConstraint ObjectId areaId,
            @RequestParam @NotNull @Min(0) @Max(10000) int page,
            @RequestParam @NotNull @Min(5) @Max(100) int size
    ){
        return service.paginateList(
                page,
                size,
                areaId
        );
    }

    @GetMapping("/user-notifications")
    public ResponseEntity<Page<NotificationDto>> list(
            HttpServletRequest request,
            @RequestParam(value = "page") @NotNull @Min(0) @Max(10000) int page,
            @RequestParam(value = "size") @NotNull @Min(5) @Max(100) int size,
            @RequestParam(required = false, value = "seenStatus") Boolean seenStatus
    ){
        return service.userNotifications(
                getId(request),
                seenStatus,
                page,
                size
        );
    }

    @GetMapping("/user-notifications-count")
    public ResponseEntity<Long> count(
            HttpServletRequest request
    ){
        return service.userNotificationsCount(
                getId(request)
        );
    }

    @PostMapping("/{areaId}")
    public ResponseEntity<Notification> store(
            HttpServletRequest request,
            @PathVariable @NotNull @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid NotificationData data
    ){
        return service.store(
                data, areaId, getId(request)
        );
    }

    @PutMapping("/{id}")
    public void update(
            HttpServletRequest request,
            @PathVariable @NotNull @ObjectIdConstraint ObjectId id,
            @RequestBody @NotNull @Valid NotificationData data
    ) {
        service.update(id, data, getId(request));
    }

    @DeleteMapping("/{id}")
    public String delete(
            HttpServletRequest request,
            @PathVariable ObjectId id
    ) {
        return service.remove(id, getId(request));
    }

    @PostMapping("/{id}/seen")
    public void seen(
            HttpServletRequest request,
            @PathVariable ObjectId id
    ) {
        service.markAsSeen(id, getId(request));
    }
}