package io.github.nguyenquephong13062003.course_management_system.security.jwt;

/**
 * Represents the authentication failure reason detected while processing JWT.
 */
public enum JwtAuthenticationError {

    /**
     * The JWT has expired.
     */
    EXPIRED_JWT_TOKEN,

    /**
     * The JWT is malformed, has an invalid signature,
     * is unsupported, or otherwise cannot be trusted.
     */
    INVALID_JWT_TOKEN
}