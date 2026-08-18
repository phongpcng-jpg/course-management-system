package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.DuplicateResourceException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.InvalidStateTransitionException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.UserHasDependentDataException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UpdateUserRoleRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UpdateUserStatusRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.UserRequest;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ICourseRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IEnrollmentRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.IUserService;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * UserServiceImpl
 * Implementation of IUserService for user-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    /**
     * The user repository used for accessing user data.
     */
    private final IUserRepository userRepository;

    private final ICourseRepository courseRepository;

    private final IEnrollmentRepository enrollmentRepository;

    private final IReviewRepository reviewRepository;

    /**
     * The password encoder used for encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<UserResponse> getAllUsers(
        int page, 
        int size, 
        String sortBy, 
        String direction, 
        String keyword,
        Boolean active,
        UserRole role
    ) {
        
        if (page < 0) {
            page = 0;
        }

        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isBlank()
                && direction != null && !direction.isBlank()) {

            sort = direction.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserResponse> userPage = userRepository.findAllUsersWithKeywordAndFilters(keyword, active, role, pageable);

        return PageResponse.<UserResponse>builder()
                .items(userPage.getContent())
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalItems(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .isLast(userPage.isLast())
                .build();

    }

    @Override
    public UserResponse getUserById(Long id) {
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("User with id " + id + " not found");
                });

        return toUserResponse(user);

    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {

            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' already exists"
            );

        }

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email '" + request.getEmail() + "' already exists"
            );

        }

        User user = userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .passwordHash(passwordEncoder.encode(
                                request.getPassword()
                        )).email(request.getEmail())
                        .fullName(request.getFullName())
                        .role(
                                request.getRole() != null
                                ? request.getRole() : UserRole.STUDENT
                        )
                        .build()
        );

        return toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserRole(Long id, UpdateUserRoleRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("JWT verification failed: authentication is missing or unauthenticated");
            throw new IllegalStateException("Authentication is invalid");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("User with id " + id + " not found");
                });

        if (user.getRole() == UserRole.ADMIN && !user.getUsername().equals(authentication.getName())) {
            throw new AccessDeniedException("Admin cannot modify the role of another admin.");
        }

        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);

    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("User with id " + id + " not found");
                });

        user.setActive(request.getIsActive());

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);

    }

    @Override
    @Transactional
    public UserResponse deleteUser(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("JWT verification failed: authentication is missing or unauthenticated");
            throw new IllegalStateException("Authentication is invalid");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("User with id " + id + " not found");
                });

        if (Objects.equals(user.getUsername(), authentication.getName())) {
            throw new InvalidStateTransitionException(
                    "Admin cannot delete their own account"
            );
        }

        if (user.getRole() == UserRole.ADMIN) {
            throw new InvalidStateTransitionException(
                    "Cannot delete Other admin"
            );
        }

        if (user.getRole() == UserRole.TEACHER) {
            if (courseRepository.existsByTeacher_Id(user.getId())) {
                log.warn(
                        "Cannot delete teacher id={} because it contains business data: " +
                                "course={}",
                        user.getId(),
                        true
                );

                throw new UserHasDependentDataException(
                        "User cannot be deleted because it contains business data"
                );
            }
        } else {
            boolean hasEnrollment = enrollmentRepository.existsByStudent_Id(user.getId());
            boolean hasReview = reviewRepository.existsByStudent_Id(user.getId());

            if (hasEnrollment || hasReview) {
                log.warn(
                        "Cannot delete student id={} because it contains business data: " +
                                "enrollment={}, review={}",
                        user.getId(),
                        hasEnrollment,
                        hasReview
                );
                
                throw new UserHasDependentDataException(
                        "User cannot be deleted because it contains business data"
                );
            }
        }

        userRepository.deleteById(id);

        return toUserResponse(user);
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user the User entity to convert
     * @return the corresponding UserResponse DTO
     */
    private UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .active(user.getActive())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

    }

}
