package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequests;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IAuthService;
import io.github.nguyenquephong13062003.course_management_system.security.jwt.JWTUtils;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager manager;
    private final JWTUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequests req) {
        Authentication authentication;
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new AuthException("Username or password is incorrect");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return LoginResponse.builder()
                .accessToken(jwtUtils.generateToken(userDetails.getUsername()))
                .roles(
                        Set.of(
                            userDetails.getUser().getRole().name()
                        )
                )
                .build();
    }
}
