package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for updating user password.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserPasswordRequest {

    /**
     * The current password of the user.
     */
    @NotBlank(message = "Current password must not be blank")
    @Size(min = 6, max = 100, message = "Current password must be between 6 and 100 characters")
    private String currentPassword;

    /**
     * The new password for the user.
     */
    @NotBlank(message = "New password must not be blank")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;

}
