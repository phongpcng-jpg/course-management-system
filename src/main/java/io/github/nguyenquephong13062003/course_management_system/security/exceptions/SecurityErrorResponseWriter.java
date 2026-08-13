package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

/**
 * A utility class for writing security error responses to the HTTP response.
 */
@Component
@RequiredArgsConstructor
public class SecurityErrorResponseWriter {

    /**
     * The ObjectMapper used for serializing the error response to JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Writes a security error response to the HTTP response.
     *
     * @param response      The HttpServletResponse to write to.
     * @param header        The value for the "error" header.
     * @param status        The HTTP status code to set.
     * @param errorResponse The ApiResponse containing the error details.
     * @throws IOException If an input or output exception occurs.
     */
    public void write(
            HttpServletResponse response,
            String header,
            int status,
            ApiResponse<Void> errorResponse
    ) throws IOException {

        response.setHeader("error", header);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse
        );
    }
}