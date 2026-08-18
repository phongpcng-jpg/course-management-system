package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating a new notification.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {

    /**
     * The ID of the user who will receive the notification.
     */
    @NotNull(message = "User ID must not be null")
    private Long userId;

    /**
     * The content of the notification.
     */
    @NotBlank(message = "Message must not be blank")
    @Size(
            max = 2000,
            message = "Message must not exceed 2000 characters"
    )
    private String message;

    /**
     * The type of event that generated the notification.
     */
    @Size(
            max = 50,
            message = "Notification type must not exceed 50 characters"
    )
    private String type;

    /**
     * The URL associated with the notification.
     */
    @Size(
            max = 1000,
            message = "Target URL must not exceed 1000 characters"
    )
    private String targetUrl;
}
