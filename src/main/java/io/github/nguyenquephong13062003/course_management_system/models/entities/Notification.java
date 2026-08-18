package io.github.nguyenquephong13062003.course_management_system.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a notification sent to a user in the course management system.
 * A notification can be created automatically by the system or manually by an administrator.
 */
@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notifications_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_notifications_user_read",
                        columnList = "user_id, is_read"
                ),
                @Index(
                        name = "idx_notifications_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    /**
     * The unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    /**
     * The user who receives this notification.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_notifications_user"
            )
    )
    private User user;

    /**
     * The detailed message of the notification.
     */
    @Column(
            name = "message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    /**
     * The type of event that generated the notification.
     * <p>
     * Examples:
     * NEW_COURSE,
     * LESSON_UPDATED,
     * ENROLLMENT_CONFIRMED.
     */
    @Column(
            name = "type",
            length = 50
    )
    private String type;

    /**
     * The URL to navigate to when the user interacts with the notification.
     */
    @Column(
            name = "target_url",
            length = 1000
    )
    private String targetUrl;

    /**
     * Indicates whether the user has read the notification.
     */
    @Column(
            name = "is_read",
            nullable = false
    )
    @Builder.Default
    private Boolean isRead = false;

    /**
     * The timestamp when the notification was created.
     */
    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;
}
