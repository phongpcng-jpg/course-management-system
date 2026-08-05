package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Un Authentication {}", accessDeniedException.getMessage());
        response.setHeader("error","FORBIDDEN");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        Map<String, Object> errors = new HashMap<>();
        errors.put("code",403);
        errors.put("error",accessDeniedException.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),errors);
    }
}
