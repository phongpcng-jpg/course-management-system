package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A custom implementation of the AccessDeniedHandler interface that handles access denied exceptions.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDenied implements AccessDeniedHandler {

    /**
     * The SecurityErrorResponseWriter used for writing error responses.
     */
    private final SecurityErrorResponseWriter responseWriter;

    /**
     * Handles access denied exceptions by logging the event and writing an error response.
     *
     * @param request               The HttpServletRequest that resulted in an AccessDeniedException.
     * @param response              The HttpServletResponse to write to.
     * @param accessDeniedException The AccessDeniedException that caused the failure.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn(
            "Access Denied for request {} {}: {}",
            request.getMethod(),
            request.getRequestURI(),
            accessDeniedException.getMessage()
        );

        responseWriter.write(
            response,
            "FORBIDDEN",
            HttpServletResponse.SC_FORBIDDEN,
            ApiResponse.<Void>error(
                HttpServletResponse.SC_FORBIDDEN,
                "ACCESS_DENIED",
                accessDeniedException.getMessage(),
                null
            )
        );
    }
}
