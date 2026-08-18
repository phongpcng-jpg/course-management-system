package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when a Cloudinary resource cannot be deleted.
 */
public class DeleteCloudinaryException extends RuntimeException {

    /**
     * Constructs a new DeleteCloudinaryException with the specified detail message.
     *
     * @param message the detail message
     */
    public DeleteCloudinaryException(String message) {
        super(message);
    }

    /**
     * Constructs a new DeleteCloudinaryException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public DeleteCloudinaryException(String message, Throwable cause) {
        super(message, cause);
    }
    
}