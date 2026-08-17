package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when an invalid state transition is attempted in the system.
 * This exception indicates that the requested operation cannot be performed due to the current state of the entity.
 */
public class InvalidStateTransitionException extends RuntimeException {

    /**
     * Constructs a new InvalidStateTransitionException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidStateTransitionException(String message) {
        super(message);
    }
    
}
