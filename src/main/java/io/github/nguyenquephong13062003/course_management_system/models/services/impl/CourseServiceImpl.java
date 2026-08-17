package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ICourseRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of the ICourseService interface, providing course-related services in the course management system.
 * This class handles the retrieval of courses with various filtering and pagination options.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CourseServiceImpl implements ICourseService {

    /**
     * The ICourseRepository instance used for accessing course data from the database.
     */
    private final ICourseRepository courseRepository;

    @Override
    public PageResponse<CourseResponse> getAllCourse(
            int page,
            int size,
            String sortBy,
            String direction,
            String keyword,
            CourseStatus status,
            Long teacherId,
            BigDecimal priceMin,
            BigDecimal priceMax,
            Integer durationHoursMin,
            Integer durationHoursMax
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

        Page<CourseResponse> coursePage = courseRepository.findAllCourseWithKeywordAndFilters(
                keyword, status, teacherId, priceMin, priceMax, durationHoursMin, durationHoursMax, pageable
        );

        return PageResponse.<CourseResponse>builder()
                .items(coursePage.getContent())
                .page(coursePage.getNumber())
                .size(coursePage.getSize())
                .totalItems(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .isLast(coursePage.isLast())
                .build();

    }

}
