package io.github.nguyenquephong13062003.course_management_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.nguyenquephong13062003.course_management_system.security.exceptions.AccessDenied;
import io.github.nguyenquephong13062003.course_management_system.security.exceptions.JwtEntryPoint;
import io.github.nguyenquephong13062003.course_management_system.security.jwt.JwtTokenFilter;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * CustomUserDetailsService is a service that loads user-specific data.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * JwtEntryPoint is a class that handles unauthorized access attempts.
     */
    private final JwtEntryPoint entryPoint;

    /**
     * AccessDenied is a class that handles access denied exceptions.
     */
    private final AccessDenied accessDenied;

    /**
     * JwtTokenFilter is a filter that validates JWT tokens in incoming requests.
     */
    private final JwtTokenFilter jwtTokenFilter;

    /**
     * PasswordEncoder bean that uses BCrypt hashing algorithm.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager bean that is used for authentication.
     * @param config The AuthenticationConfiguration.
     * @return An AuthenticationManager instance.
     * @throws Exception If an error occurs while creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * AuthenticationProvider bean that uses DaoAuthenticationProvider with the custom UserDetailsService and PasswordEncoder.
     * @return An AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * SecurityFilterChain bean that configures the security filter chain.
     * @param http The HttpSecurity object.
     * @return A SecurityFilterChain instance.
     * @throws Exception If an error occurs while configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(url -> url
                        // TODO: Add public endpoints here
                        .requestMatchers("/api/users").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDenied)
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
