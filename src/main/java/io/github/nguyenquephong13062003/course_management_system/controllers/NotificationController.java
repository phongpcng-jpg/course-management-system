package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.NotificationCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.NotificationResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.INotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling notification-related API requests.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    /**
     * Service for handling notification-related business logic.
     */
    private final INotificationService notificationService;

    /**
     * Retrieves paginated notifications belonging to the current user.
     *
     * @param page   the page number, starting from 0.
     * @param size   the number of notifications per page.
     * @param isRead optional filter by notification read status.
     * @return paginated notifications.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>>
    getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, name = "is-read") Boolean isRead
    ) {
        log.info(
                "Received request to get notifications, page={}, size={}, isRead={}",
                page,
                size,
                isRead
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        PageResponse<NotificationResponse> notifications =
                notificationService.getMyNotifications(
                        isRead,
                        pageable
                );

        log.info(
                "Notifications retrieved successfully, page={}, size={}, totalItems={}",
                page,
                size,
                notifications.getTotalItems()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Notifications retrieved successfully",
                        notifications
                )
        );
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification.
     * @return the updated notification.
     */
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>>
    markAsRead(
            @PathVariable Long notificationId
    ) {
        log.info(
                "Received request to mark notification as read, notificationId={}",
                notificationId
        );

        NotificationResponse notification =
                notificationService.markAsRead(notificationId);

        log.info(
                "Notification marked as read successfully, notificationId={}",
                notificationId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Notification marked as read successfully",
                        notification
                )
        );
    }

    /**
     * Creates a notification for a specific user.
     *
     * @param request the notification creation request.
     * @return the created notification.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>>
    createNotification(
            @Valid @RequestBody NotificationCreateRequest request
    ) {
        log.info(
                "Received request to create notification, userId={}, type={}",
                request.getUserId(),
                request.getType()
        );

        NotificationResponse notification =
                notificationService.createNotification(request);

        log.info(
                "Notification created successfully, notificationId={}, userId={}",
                notification.getNotificationId(),
                request.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                HttpStatus.CREATED.value(),
                                "Notification created successfully",
                                notification
                        )
                );
    }

    /**
     * Deletes a notification.
     *
     * @param notificationId the ID of the notification to delete.
     * @return an empty success response.
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
    deleteNotification(
            @PathVariable Long notificationId
    ) {
        log.info(
                "Received request to delete notification, notificationId={}",
                notificationId
        );

        notificationService.deleteNotification(notificationId);

        log.info(
                "Notification deleted successfully, notificationId={}",
                notificationId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Notification deleted successfully",
                        null
                )
        );
    }
}
