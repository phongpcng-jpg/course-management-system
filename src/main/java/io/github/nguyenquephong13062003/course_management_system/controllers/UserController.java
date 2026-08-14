package io.github.nguyenquephong13062003.course_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IUserService;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * The user service used for handling user-related operations.
     */
    private final IUserService userService;

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
     * @return a ResponseEntity containing the paginated list of UserResponse objects wrapped in an ApiResponse
     */
    @GetMapping
    public ResponseEntity<?> getPagedUsers(

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "10")
            int size,

            @RequestParam(name = "sortBy", required = false)
            String sortBy,

            @RequestParam(name = "direction", required = false)
            String direction,

            @RequestParam(name = "keyword", required = false)
            String keyword,

            @RequestParam(name = "active", required = false)
            Boolean active,

            @RequestParam(name = "role", required = false)
            UserRole role

    ) {
        PageResponse<UserResponse> response = userService.getAllUsers(
            page, size, sortBy, direction, keyword, active, role
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                    200,
                    "Fetched paginated users successfully",
                    response
                )
        );
        
    }

}
