package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginRequests
 * Represents the request payload for user login.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequests {

    /**
     * The username of the user attempting to log in.
     */
    @NotBlank(message = "username must be not empty")
    private String username;

    /**
     * The password of the user attempting to log in.
     */
    @NotBlank(message = "password must be not empty")
    private String password;

}
