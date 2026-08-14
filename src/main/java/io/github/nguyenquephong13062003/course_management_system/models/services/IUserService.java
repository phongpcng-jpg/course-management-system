package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;

/**
 * IUserService
 * Interface for user-related services.
 */
public interface IUserService {

    /**
     * Retrieves a paginated list of users based on the provided parameters.
     *
     * @param page      the page number to retrieve (0-based)
     * @param size      the number of users per page
     * @param sortBy    the field to sort by
     * @param direction the direction of sorting (ASC or DESC)
     * @param keyword   a keyword to filter users by username, email, or full name
     * @param active    a filter for active status (true for active, false for inactive, null for all)
     * @param role      a filter for user role (null for all roles)
     * @return a PageResponse containing the paginated list of UserResponse objects
     */
    PageResponse<UserResponse> getAllUsers(
        int page, 
        int size,
        String sortBy,
        String direction,
        String keyword,
        Boolean active,
        UserRole role
    );

}
