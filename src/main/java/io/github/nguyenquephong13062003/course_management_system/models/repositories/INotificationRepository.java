package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing Notification entities.
 */
public interface INotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Retrieves notifications belonging to a specific user with a given read status.
     *
     * @param userId  the ID of the notification recipient.
     * @param isRead  the read status used to filter notifications.
     * @param pageable pagination and sorting information.
     * @return a page of notifications matching the specified user and read status.
     */
    Page<Notification> findByUser_IdAndIsRead(
            Long userId,
            Boolean isRead,
            Pageable pageable
    );

    /**
     * Retrieves all notifications belonging to a specific user.
     *
     * @param userId  the ID of the notification recipient.
     * @param pageable pagination and sorting information.
     * @return a page of notifications belonging to the specified user.
     */
    Page<Notification> findByUser_Id(
            Long userId,
            Pageable pageable
    );

    /**
     * Retrieves a notification only when it belongs to the specified user.
     *
     * @param notificationId the ID of the notification.
     * @param userId         the ID of the notification owner.
     * @return the notification if it exists and belongs to the specified user.
     */
    Optional<Notification> findByNotificationIdAndUser_Id(
            Long notificationId,
            Long userId
    );
}
