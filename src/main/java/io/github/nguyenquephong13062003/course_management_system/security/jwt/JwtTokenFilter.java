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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = getTokenFromRequest(request);
            if(token != null) {
                Claims claims = jwtUtils.extractAllClaims(token);
                String username = jwtUtils.extractUsername(claims);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtUtils.validateToken(claims,userDetails)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException e) {
            log.warn(
                    "JWT token has expired for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

        } catch (JwtException e) {
            log.warn(
                    "Invalid JWT token for request: {} {} - {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    e.getMessage()
            );

        } catch (UsernameNotFoundException e) {
            log.warn(
                    "User from JWT token was not found for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

        } catch (Exception e) {
            log.error(
                    "Unexpected error while processing JWT authentication",
                    e
            );
        }

        filterChain.doFilter(request,response);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        // Token: Bearer édasdsadasdaskfjsdasdl3
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}