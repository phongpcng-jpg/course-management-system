package io.github.nguyenquephong13062003.course_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.VerifyResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling authentication-related requests.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * The authentication service used for handling login requests.
     */
    private final IAuthService authService;

    /**
     * Handles user login requests.
     *
     * @param request The login request containing username and password.
     * @return A ResponseEntity containing the login response wrapped in an ApiResponse.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(200).body(ApiResponse.<LoginResponse>success(
            200, 
            "Login successful",
            authService.login(request)
        ));
    }

    /**
     * Verifies the JWT access token carried by the current request.
     * Reaching this method already implies the token passed JwtTokenFilter validation.
     *
     * @param authentication The authenticated principal resolved from the JWT by Spring Security.
     * @return A ResponseEntity containing the verification result wrapped in an ApiResponse.
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyResponse>> verify() {
        
        return ResponseEntity.status(200).body(ApiResponse.<VerifyResponse>success(
            200,
            "Token is valid",
            authService.verifyToken()
        ));
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return A ResponseEntity containing the current user's profile wrapped in an ApiResponse.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        return ResponseEntity.status(200).body(ApiResponse.<UserResponse>success(
            200,
            "User profile retrieved successfully",
            authService.getCurrentUser()
        ));
    }

}
