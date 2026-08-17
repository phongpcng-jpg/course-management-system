package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when an operation is attempted on a course that has dependent data, such as enrollments or reviews.
 * This exception indicates that the course cannot be modified or deleted due to existing dependencies.
 */
public class CourseHasDependentDataException extends RuntimeException {

    /**
     * Constructs a new CourseHasDependentDataException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CourseHasDependentDataException(String message) {
        super(message);
    }
    
}
