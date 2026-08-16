package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UpdateUserRoleRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UpdateUserStatusRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UserRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class UserController {

    /**
     * The user service used for handling user-related operations.
     */
    private final IUserService userService;

    /**
     * Retrieves a paginated list of users based on the provided query parameters.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param page      The page number to retrieve (default is 0).
     * @param size      The number of users per page (default is 10).
     * @param sortBy    The field to sort by (optional).
     * @param direction The direction of sorting, either 'asc' or 'desc' (optional).
     * @param keyword   A keyword to filter users by name or email (optional).
     * @param active    A flag to filter users by their active status (optional).
     * @param role      A specific user role to filter by (optional).
     * @return A ResponseEntity containing the paginated list of users wrapped in an ApiResponse.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getPagedUsers(

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

            // Note: The parameter name is changed to "status" for matching 31 endpoint
            @RequestParam(name = "status", required = false) 
            Boolean active,

            @RequestParam(name = "role", required = false)
            UserRole role

    ) {

        log.info(
                "Fetching paginated users: page={}, size={}, sortBy={}, direction={}, keyword={}, active={}, role={}",
                page, size, sortBy, direction, keyword, active, role
        );

        PageResponse<UserResponse> response = userService.getAllUsers(
            page, size, sortBy, direction, keyword, active, role
        );

        log.info(
                "Fetched paginated users: page={}, size={}, totalItems={}, totalPages={}, isLast={}",
                response.getPage(), response.getSize(), response.getTotalItems(),
                response.getTotalPages(), response.getIsLast()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                    200,
                    "Fetched paginated users successfully",
                    response
                )
        );
        
    }

    /**
     * Retrieves a user by their unique identifier.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A ResponseEntity containing the user details wrapped in an ApiResponse.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("user_id") Long id) {

        log.info("Fetching user by ID: {}", id);

        UserResponse response = userService.getUserById(id);

        log.info("Fetched user by ID: {}", id);

        return ResponseEntity.ok(
                ApiResponse.success(
                    200,
                    "Fetched user successfully",
                    response
                )
        );

    }

    /**
     * Creates a new user based on the provided request data.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param request The request body containing user details for creation.
     * @return A ResponseEntity containing the created user details wrapped in an ApiResponse.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request
    ) {

        log.info("Creating new user: username={}, email={}, role={} , fullName={}",
                request.getUsername(),
                request.getEmail(),
                request.getRole(),
                request.getFullName()
        );

        UserResponse response = userService.createUser(request);

        log.info("User created successfully: userId={}, username={}, role={}",
                response.getId(),
                response.getUsername(),
                response.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "User created successfully",
                        response
                ));

    }

    /**
     * Updates the role of an existing user based on the provided request data.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param id      The unique identifier of the user to update.
     * @param request The request body containing the new role information.
     * @return A ResponseEntity containing the updated user details wrapped in an ApiResponse.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{user_id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable("user_id") Long id,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {

        log.info("Updating user role: userId={}, role={}", id, request.getRole());

        UserResponse response = userService.updateUserRole(id, request);

        log.info("User role updated successfully: userId={}, role={}", id, response.getRole());

        return  ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "User role updated successfully",
                        response
                )
        );

    }

    /**
     * Updates the status (active/inactive) of an existing user based on the provided request data.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param id      The unique identifier of the user to update.
     * @param request The request body containing the new status information.
     * @return A ResponseEntity containing the updated user details wrapped in an ApiResponse.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{user_id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable("user_id") Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {

        log.info("Updating user status: userId={}, isActive={}", id, request.getIsActive());

        UserResponse response = userService.updateUserStatus(id, request);

        log.info("User status updated successfully: userId={}, isActive={}", id, response.getActive());

        return  ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "User status updated successfully",
                        response
                )
        );

    }

}
