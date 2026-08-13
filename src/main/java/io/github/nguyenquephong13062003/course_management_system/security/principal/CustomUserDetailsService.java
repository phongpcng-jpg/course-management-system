package io.github.nguyenquephong13062003.course_management_system.security.principal;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomUserDetailsService is a service that implements UserDetailsService to load user-specific data.
 * It retrieves user information from the database and constructs a CustomUserDetails object for authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * IUserRepository is a repository interface for accessing user data from the database.
     */
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Loading user details for username={}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("User not found with username: {}", username);
            return new UsernameNotFoundException("Username or password is incorrect");
        });

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .user(user)
                .authorities(
                        List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                        )
                )
                .build();

        log.debug(
                "User loaded successfully: username={}, role={}",
                user.getUsername(),
                user.getRole()
        );

        return customUserDetails;
    }
}
