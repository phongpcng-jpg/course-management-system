package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when invalid input data is provided.
 */
public class InvalidInputDataException extends RuntimeException {

    /**
     * Constructs a new InvalidInputDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidInputDataException(String message) {
        super(message);
    }
    
}
