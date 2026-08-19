package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.security.jwt.JwtAuthenticationError;
import io.github.nguyenquephong13062003.course_management_system.security.jwt.JwtSecurityConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles authentication failures caused by missing or invalid JWT
 * authentication.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final SecurityErrorResponseWriter responseWriter;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        JwtAuthenticationError jwtError =
                getJwtAuthenticationError(request);

        String errorCode = jwtError.name();

        String message = switch (jwtError) {
            case EXPIRED_JWT_TOKEN ->
                    "JWT token has expired";

            case INVALID_JWT_TOKEN ->
                    "JWT token is invalid";
        };

        log.warn(
                "JWT authentication failed: method={}, uri={}, errorCode={}",
                request.getMethod(),
                request.getRequestURI(),
                errorCode
        );

        responseWriter.write(
                response,
                "UNAUTHORIZED",
                HttpServletResponse.SC_UNAUTHORIZED,
                ApiResponse.<Void>error(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        errorCode,
                        message,
                        null
                )
        );
    }

    /**
     * Reads the JWT authentication failure reason stored by JwtTokenFilter.
     *
     * <p>If the request reaches the entry point without an explicitly
     * recorded JWT error, it is treated as an invalid JWT.</p>
     */
    private JwtAuthenticationError getJwtAuthenticationError(
            HttpServletRequest request
    ) {

        Object attribute = request.getAttribute(
                JwtSecurityConstants.JWT_ERROR_ATTRIBUTE
        );

        if (attribute instanceof JwtAuthenticationError jwtError) {
            return jwtError;
        }

        return JwtAuthenticationError.INVALID_JWT_TOKEN;
    }
}