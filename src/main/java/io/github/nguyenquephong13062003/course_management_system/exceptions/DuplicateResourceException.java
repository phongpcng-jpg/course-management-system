package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * DuplicateResourceException is a custom exception that is thrown when an attempt is made to create a resource that already exists.
 * This exception extends RuntimeException and can be used to indicate that a duplicate resource was encountered during an operation.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

}
