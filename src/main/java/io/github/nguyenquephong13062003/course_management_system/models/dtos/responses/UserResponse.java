package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserResponse
 * Represents the public profile of a user, safe to expose via the API
 * (never includes the password hash).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The role of the user (ADMIN, TEACHER, STUDENT).
     */
    private String role;

    /**
     * Whether the user's account is active.
     */
    private Boolean active;

    /**
     * Timestamp indicating when the user was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating when the user was last updated.
     */
    private LocalDateTime updatedAt;

}
