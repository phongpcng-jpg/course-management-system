package io.github.nguyenquephong13062003.course_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());
        return ResponseEntity.status(200).body(ApiResponse.<LoginResponse>success(
            200, 
            "Login successful",
            authService.login(request)
        ));
    }

}
