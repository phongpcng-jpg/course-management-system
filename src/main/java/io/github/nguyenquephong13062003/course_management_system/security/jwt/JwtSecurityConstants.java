package io.github.nguyenquephong13062003.course_management_system.security.jwt;

/**
 * Constants used by the JWT security filter and authentication entry point.
 */
public final class JwtSecurityConstants {

    /**
     * Request attribute used to transfer the JWT authentication failure
     * reason from JwtTokenFilter to JwtEntryPoint.
     */
    public static final String JWT_ERROR_ATTRIBUTE =
            "JWT_AUTHENTICATION_ERROR";

    private JwtSecurityConstants() {
        // Utility class.
    }
}