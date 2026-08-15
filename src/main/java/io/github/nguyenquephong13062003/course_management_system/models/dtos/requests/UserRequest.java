package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * UserRequest is a Data Transfer Object (DTO) that represents the request payload for creating or updating a user.
 * It includes validation annotations to ensure that the input data meets the required constraints.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {

    /**
     * The username of the user. It must not be blank and must be between 3 and 50 characters.
     */
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * The password of the user. It must not be blank and must be between 6 and 100 characters.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    /**
     * The email of the user. It must not be blank, must be a valid email format, and must not exceed 255 characters.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /**
     * The full name of the user. It must not be blank and must be between 2 and 100 characters.
     */
    @NotBlank(message = "Full name must not be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    /**
     * The role of the user. It must not be null and should be one of the predefined roles in UserRole.
     * If not provided, it defaults to UserRole.STUDENT.
     */
    @Builder.Default
    private UserRole role = UserRole.STUDENT;

}
