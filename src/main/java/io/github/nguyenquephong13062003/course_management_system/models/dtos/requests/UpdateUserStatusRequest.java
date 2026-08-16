package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for updating a user's status.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserStatusRequest {

    /**
     * The new status to be assigned to the user.
     */
    @NotNull(message = "is_active must not be null")
    Boolean isActive;

}
