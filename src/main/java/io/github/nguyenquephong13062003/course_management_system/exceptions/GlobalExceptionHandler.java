package io.github.nguyenquephong13062003.course_management_system.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
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

    /**
     * Handles DuplicateResourceException thrown by the application.
     * @param ex The DuplicateResourceException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(
            HttpServletRequest request,
            HttpServletResponse response,
            DuplicateResourceException ex
    ) {

        log.warn(
                "Resource duplicate in request {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return  ResponseEntity.status(400).body(
                ApiResponse.<Void>error(
                        400,
                        "DUPLICATE_RESOURCE",
                        ex.getMessage(),
                        null
                )
        );

    }

    /**
     * Handles InvalidInputDataException thrown by the application.
     * @param ex The InvalidInputDataException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidInputDataException(
            HttpServletRequest request,
            HttpServletResponse response,
            InvalidInputDataException ex
    ) {

        log.warn(
                "Invalid input data in request {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return  ResponseEntity.status(400).body(
                ApiResponse.<Void>error(
                        400,
                        "INVALID_INPUT_DATA",
                        ex.getMessage(),
                        null
                )
        );

    }

    /**
     * Handles InvalidStateTransitionException thrown by the application.
     * @param ex The InvalidStateTransitionException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidStateTransitionException(
            HttpServletRequest request,
            HttpServletResponse response,
            InvalidStateTransitionException ex
    ) {

        log.warn(
                "Invalid state transition in request {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.<Void>error(
                        HttpStatus.CONFLICT.value(),
                        "INVALID_STATE_TRANSITION",
                        ex.getMessage(),
                        null
                )
        );

    }

    /**
     * Handles IllegalArgumentException thrown by the application.
     * @param ex The IllegalArgumentException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            HttpServletRequest request,
            HttpServletResponse response,
            IllegalArgumentException ex
    ) {

        log.warn(
                "Illegal argument in request {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return  ResponseEntity.status(400).body(
                ApiResponse.<Void>error(
                        400,
                        "ILLEGAL_ARGUMENT",
                        ex.getMessage(),
                        null
                )
        );

    }

    /**
     * Handles CourseHasDependentDataException thrown by the application.
     * @param ex The CourseHasDependentDataException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(CourseHasDependentDataException.class)
    public ResponseEntity<ApiResponse<Void>> handleCourseHasDependentDataException(
            HttpServletRequest request,
            HttpServletResponse response,
            CourseHasDependentDataException ex
    ) {

        log.warn(
                "Course has dependent data in request {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.<Void>error(
                        HttpStatus.CONFLICT.value(),
                        "COURSE_HAS_DEPENDENT_DATA",
                        ex.getMessage(),
                        null
                )
        );

    }

    /**
     * Handles UploadCloudinaryException thrown by the application.
     * @param ex The UploadCloudinaryException that was thrown.
     * @return A ResponseEntity containing an ApiResponse with error details.
     */
    @ExceptionHandler(UploadCloudinaryException.class)
    public ResponseEntity<ApiResponse<Void>> handleUploadCloudinaryFail(
            UploadCloudinaryException ex
    ) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(
                        ApiResponse.<Void>error(
                                HttpStatus.BAD_GATEWAY.value(),
                                "BAD_GATEWAY",
                                ex.getMessage(),
                                null
                        )
                );

    }

}