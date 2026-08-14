package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.VerifyResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.services.IAuthService;
import io.github.nguyenquephong13062003.course_management_system.security.jwt.JWTUtils;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthServiceImpl
 * Implementation of the IAuthService interface for handling authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    /**
     * The authentication manager used for authenticating users.
     */
    private final AuthenticationManager manager;

    /**
     * The JWT utility used for generating and validating JWT tokens.
     */
    private final JWTUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws AuthException {
        Authentication authentication;
        
        log.info("Attempting login for user: {}", loginRequest.getUsername());
        
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new AuthException("Username or password is incorrect");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return LoginResponse.builder()
                .accessToken(jwtUtils.generateToken(userDetails.getUsername()))
                .role(userDetails.getUser().getRole().name())
                .build();
    }

    @Override
    public VerifyResponse verifyToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Verifying token for user: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("JWT verification failed: authentication is missing or unauthenticated");
            throw new IllegalStateException("Authentication is invalid");
        }

        String username = authentication.getName();

        log.debug(
                "JWT verification successful for user: {}",
                username
        );

        return VerifyResponse.builder()
                .valid(true)
                .username(username)
                .role(
                    authentication.getAuthorities()
                    .stream()
                    .map(authority -> authority.getAuthority())
                    .map(authority -> authority.startsWith("ROLE_")
                            ? authority.substring(5)
                            : authority)
                    .findFirst()
                    .orElse(null)
                )
                .build();
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Fetching current user profile for: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("JWT verification failed: authentication is missing or unauthenticated");
            throw new IllegalStateException("Authentication is invalid");
        }
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        log.debug("Fetching profile for current user: {}", user.getUsername());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
