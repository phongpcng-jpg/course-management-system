package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for updating a user's role.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserRoleRequest {

    /**
     * The new role to be assigned to the user.
     */
    @NotNull(message = "Role must not be null")
    UserRole role;

}
