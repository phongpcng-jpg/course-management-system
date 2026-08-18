package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO representing a notification.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    /**
     * The unique identifier of the notification.
     */
    private Long notificationId;

    /**
     * The notification message.
     */
    private String message;

    /**
     * The type of event that generated the notification.
     */
    private String type;

    /**
     * The URL associated with the notification.
     */
    private String targetUrl;

    /**
     * Indicates whether the notification has been read.
     */
    private Boolean isRead;

    /**
     * The timestamp when the notification was created.
     */
    private LocalDateTime createdAt;
}