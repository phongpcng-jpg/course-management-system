package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserPasswordRequest {

    @NotBlank(message = "Current password must not be blank")
    @Size(min = 6, max = 100, message = "Current password must be between 6 and 100 characters")
    private String currentPassword;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;

}
