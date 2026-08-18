package io.github.nguyenquephong13062003.course_management_system.exceptions;

/**
 * Exception thrown when an error occurs during file upload to Cloudinary.
 */
public class UploadCloudinaryException extends RuntimeException {

    /**
     * Constructs a new UploadCloudinaryException with the specified detail message.
     *
     * @param message the detail message
     */
    public UploadCloudinaryException(String message) {
        super(message);
    }
    
}
