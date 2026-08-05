package io.github.nguyenquephong13062003.course_management_system.security.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Un Authentication {}", authException.getMessage());
        response.setHeader("error","UNAUTHORIZED");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);
        Map<String, Object> errors = new HashMap<>();
        errors.put("code",401);
        errors.put("error",authException.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),errors);
    }
}
