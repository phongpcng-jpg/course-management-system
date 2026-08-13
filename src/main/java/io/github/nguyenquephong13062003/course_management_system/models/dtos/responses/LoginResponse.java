package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginResponse
 * Represents the response payload for a successful login.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {

    /**
     * The access token issued upon successful authentication.
     */
    private String accessToken;

    /**
     * The type of the token, typically "Bearer".
     */
    @Builder.Default
    private String type = "Bearer";
   
    /**
     * The username of the authenticated user.
     */
    private Set<String> roles;

}
