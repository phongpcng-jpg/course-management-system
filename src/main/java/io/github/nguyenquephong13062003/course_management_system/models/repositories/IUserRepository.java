package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import java.util.List;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;


/**
 * User Repository
 */
public interface IUserRepository extends JpaRepository<User,Long> {

    /**
     * Find a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found User, or empty if not found
     */
    Optional<User> findByUsername(String username);

    List<User> findByRole(UserRole role);
}
