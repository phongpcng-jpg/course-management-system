package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Custom exception class for authentication-related errors.
 */
public class AuthException extends RuntimeException {

    /**
     * Constructs a new AuthException with the specified detail message.
     *
     * @param message the detail message
     */
    public AuthException(String message) {
        super(message);
    }
    
}
