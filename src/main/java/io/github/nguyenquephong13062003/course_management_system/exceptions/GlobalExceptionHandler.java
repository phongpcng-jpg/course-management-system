package io.github.nguyenquephong13062003.course_management_system.exceptions;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiError;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * GlobalExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions thrown by the application.
     * @param ex The Exception that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
        HttpServletRequest request,
        HttpServletResponse response,
        Exception ex
    ) {

        log.error(
            "An error occurred for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            ex.getMessage(), 
            ex
        );

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
     * Handles MethodArgumentNotValidException thrown by the application.
     * @param ex The MethodArgumentNotValidException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
        HttpServletRequest request,
        HttpServletResponse response,
        MethodArgumentNotValidException ex
    ) {

        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ApiError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        log.warn(
            "Validation failed for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            errors
        );

        return ResponseEntity.status(422).body(
            ApiResponse.<Void>error(
                422,
                "VALIDATION_FAILED",
                "Validation failed",
                errors
            )
        );

    }

    /**
     * Handles NotFoundException thrown by the application.
     * @param ex The NotFoundException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(
        HttpServletRequest request,
        HttpServletResponse response,
        NotFoundException ex
    ) {

        log.warn(
            "Resource not found for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            ex.getMessage()
        );

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
     * Handles UsernameNotFoundException thrown by the application.
     * @param ex The UsernameNotFoundException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(
        HttpServletRequest request,
        HttpServletResponse response,
        UsernameNotFoundException ex
    ) {

        log.warn(
            "User not found for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            ex.getMessage()
        );

        return ResponseEntity.status(404).body(
            ApiResponse.<Void>error(
                404,
                "USER_NOT_FOUND",
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
    public ResponseEntity<ApiResponse<Void>> handleAuthException(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthException ex
    ) {

        log.warn(
            "Authentication failed for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            ex.getMessage()
        );

        return ResponseEntity.status(400).body(
            ApiResponse.<Void>error(
                400,
                "BAD_REQUEST",
                ex.getMessage(),
                null
            )
        );

    }

    /**
     * Handles AccessDeniedException thrown by the application.
     * @param ex The AccessDeniedException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) {

        log.warn(
            "Access denied for request {} {}: {}", 
            request.getMethod(), 
            request.getRequestURI(), 
            ex.getMessage()
        );

        return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(
            ApiResponse.<Void>error(
                HttpServletResponse.SC_FORBIDDEN,
                "ACCESS_DENIED",
                ex.getMessage(),
                null
            )
        );
    }

}
