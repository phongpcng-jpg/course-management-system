package io.github.nguyenquephong13062003.course_management_system.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * GlobalExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all exceptions that are not explicitly handled by other exception handlers.
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(
            ApiResponse.<Void>error(
                500,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later.",
                null
            )
        );
    }

    /**
     * Handles NotFoundException thrown by the application.
     * @param ex The NotFoundException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(404).body(
            ApiResponse.<Void>error(
                404,
                "NOT_FOUND",
                ex.getMessage(),
                null
            )
        );
    }

    /**
     * Handles UsernameNotFoundException thrown by the CustomUserDetailsService.
     * @param ex The UsernameNotFoundException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(401).body(
            ApiResponse.<Void>error(
                401,
                "BAD_CREDENTIALS",
                ex.getMessage(),
                null
            )
        );
    }

    /**
     * Handles AuthException thrown by the application.
     * @param ex The AuthException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(AuthException ex) {
        return ResponseEntity.status(400).body(
            ApiResponse.<Void>error(
                400,
                "BAD_REQUEST",
                ex.getMessage(),
                null
            )
        );
    }

}
