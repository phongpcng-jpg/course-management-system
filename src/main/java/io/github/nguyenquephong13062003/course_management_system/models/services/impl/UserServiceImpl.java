package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.UserResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImpl
 * Implementation of IUserService for user-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

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

}
