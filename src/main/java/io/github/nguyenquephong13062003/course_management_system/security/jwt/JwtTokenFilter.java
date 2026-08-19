package io.github.nguyenquephong13062003.course_management_system.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT authentication filter.
 *
 * <p>This filter extracts and validates the JWT from the Authorization header.
 * When authentication fails, the failure reason is stored in the request so
 * that JwtEntryPoint can return the correct error code.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);

            if (token == null) {
                if (hasInvalidAuthorizationHeader(request)) {
                    markJwtError(
                            request,
                            JwtAuthenticationError.INVALID_JWT_TOKEN
                    );
                }

                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtUtils.extractAllClaims(token);

            String username = jwtUtils.extractUsername(claims);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(claims, userDetails)) {

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                log.debug(
                        "JWT authentication successful for request: {} {}",
                        request.getMethod(),
                        request.getRequestURI()
                );
            } else {
                markJwtError(
                        request,
                        JwtAuthenticationError.INVALID_JWT_TOKEN
                );

                log.warn(
                        "JWT validation failed for request: {} {}",
                        request.getMethod(),
                        request.getRequestURI()
                );
            }

        } catch (ExpiredJwtException e) {

            markJwtError(
                    request,
                    JwtAuthenticationError.EXPIRED_JWT_TOKEN
            );

            log.warn(
                    "Expired JWT token for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

        } catch (JwtException e) {

            markJwtError(
                    request,
                    JwtAuthenticationError.INVALID_JWT_TOKEN
            );

            log.warn(
                    "Invalid JWT token for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

        } catch (UsernameNotFoundException e) {

            markJwtError(
                    request,
                    JwtAuthenticationError.INVALID_JWT_TOKEN
            );

            log.warn(
                    "User from JWT token was not found for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

        } catch (Exception e) {

            markJwtError(
                    request,
                    JwtAuthenticationError.INVALID_JWT_TOKEN
            );

            log.error(
                    "Unexpected error while processing JWT authentication",
                    e
            );
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT from Authorization header.
     */
    private String getTokenFromRequest(HttpServletRequest request) {

        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || header.isBlank()) {
            return null;
        }

        if (!header.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        return token.isEmpty() ? null : token;
    }

    /**
     * Checks whether an Authorization header exists but is not a valid
     * Bearer token representation.
     */
    private boolean hasInvalidAuthorizationHeader(
            HttpServletRequest request
    ) {

        String header = request.getHeader(AUTHORIZATION_HEADER);

        return header != null
                && !header.isBlank()
                && !header.startsWith(BEARER_PREFIX);
    }

    /**
     * Stores the JWT authentication failure reason in the current request.
     */
    private void markJwtError(
            HttpServletRequest request,
            JwtAuthenticationError error
    ) {

        request.setAttribute(
                JwtSecurityConstants.JWT_ERROR_ATTRIBUTE,
                error
        );

        SecurityContextHolder.clearContext();
    }
}