package io.github.nguyenquephong13062003.course_management_system.security.principal;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Custom implementation of UserDetails to represent authenticated user details.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserDetails implements UserDetails {

    /**
     * The user entity associated with this CustomUserDetails.
     */
    private User user;

    /**
     * The authorities granted to the user.
     */
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.getActive();
    }
}
