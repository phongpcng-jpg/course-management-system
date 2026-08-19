package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import java.util.List;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;


/**
 * User Repository
 */
public interface IUserRepository extends JpaRepository<User,Long> {

    /**
     * Check if a user exists with the given username.
     *
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Check if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    Boolean existsByEmail(String email);

    /**
     * Find a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found User, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their Role.
     *
     * @param role the role of the user to find
     * @return a list of users with the specified role
     */
    List<User> findByRole(UserRole role);

    /**
     * Find all users that are not active.
     *
     * @return a list of inactive users
     */
    List<User> findByActive(Boolean active);

    /**
     * Find all users with a keyword in their username, email, or full name,
     * and optionally filter by active status and role.
     *
     * @param keyword the keyword to search for in username, email, or full name
     * @param active  the active status to filter by (true for active, false for inactive, null for all)
     * @param role    the role to filter by (null for all roles)
     * @param pageable the pagination information
     * @return a page of UserResponse objects matching the criteria
     */
    @Query("""
        SELECT new io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse(
            u.id,
            u.username,
            u.email,
            u.fullName,
            u.role,
            u.active,
            u.createdAt,
            u.updatedAt
        )
        FROM User u
        WHERE (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ) AND (
            :active IS NULL OR u.active = :active
        ) AND (
            :role IS NULL OR u.role = :role
        )
    """)
    Page<UserResponse> findAllUsersWithKeywordAndFilters(
        @Param("keyword") String keyword, 
        @Param("active") Boolean active,
        @Param("role") UserRole role,
        Pageable pageable
    );

    List<User> findAllByRoleAndActiveTrue(UserRole userRole);
}
