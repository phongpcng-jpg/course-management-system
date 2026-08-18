package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Custom exception class for handling "not found" scenarios in the course management system.
 * This exception is thrown when a requested resource (e.g., course, student, etc.) cannot be found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public NotFoundException(String message) {
        super(message);
    }
    
}