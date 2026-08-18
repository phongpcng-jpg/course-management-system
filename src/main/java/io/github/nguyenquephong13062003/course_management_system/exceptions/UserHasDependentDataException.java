package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when a user has dependent data that prevents certain operations.
 */
public class UserHasDependentDataException extends RuntimeException {

    /**
     * Constructs a new UserHasDependentDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserHasDependentDataException(String message) {
        super(message);
    }
    
}
