package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.NotificationCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.NotificationResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for notification-related business operations.
 */
public interface INotificationService {

    /**
     * Retrieves notifications belonging to the current user.
     *
     * @param isRead   optional filter for notification read status.
     * @param pageable pagination and sorting information.
     * @return paginated notifications.
     */
    PageResponse<NotificationResponse> getMyNotifications(
            Boolean isRead,
            Pageable pageable
    );

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification.
     * @return the updated notification.
     */
    NotificationResponse markAsRead(
            Long notificationId
    );

    /**
     * Creates a notification for a specific user.
     *
     * @param request the notification creation request.
     * @return the created notification.
     */
    NotificationResponse createNotification(
            NotificationCreateRequest request
    );

    /**
     * Deletes a notification.
     *
     * @param notificationId the ID of the notification to delete.
     */
    void deleteNotification(Long notificationId);
}
