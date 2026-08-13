package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A custom implementation of the AuthenticationEntryPoint interface that handles authentication failures.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtEntryPoint implements AuthenticationEntryPoint {

    /**
     * The SecurityErrorResponseWriter used for writing error responses.
     */
    private final SecurityErrorResponseWriter responseWriter;

    /**
     * Handles authentication failures by logging the event and writing an error response.
     *
     * @param request       The HttpServletRequest that resulted in an AuthenticationException.
     * @param response      The HttpServletResponse to write to.
     * @param authException The AuthenticationException that caused the failure.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn(
            "Authentication failed for request {} {}: {}",
            request.getMethod(),
            request.getRequestURI(),
            authException.getMessage()
        );
        
        responseWriter.write(
            response,
            "UNAUTHORIZED",
            HttpServletResponse.SC_UNAUTHORIZED,
            ApiResponse.<Void>error(
                HttpServletResponse.SC_UNAUTHORIZED,
                "AUTHENTICATION_FAILED",
                authException.getMessage(),
                null
            )
        );
    }
}
