package io.github.nguyenquephong13062003.course_management_system.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}