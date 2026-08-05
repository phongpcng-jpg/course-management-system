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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .user(user)
                .authorities(
                        List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                        )
                )
                .build();
        return customUserDetails;
    }
}
