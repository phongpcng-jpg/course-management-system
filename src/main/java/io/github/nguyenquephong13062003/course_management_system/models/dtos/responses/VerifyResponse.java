package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * VerifyResponse
 * Represents the result of verifying the caller's JWT access token.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VerifyResponse {

    /**
     * Whether the token is valid. Always true when this response is returned,
     * since an invalid/expired token never reaches the controller (rejected by JwtTokenFilter/JwtEntryPoint).
     */
    private boolean valid;

    /**
     * The username extracted from the token.
     */
    private String username;

    /**
     * The role of the authenticated user.
     */
    private String role;

}
