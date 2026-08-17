package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseTeacherResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ICourseRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    /**
     * The ILessonRepository instance used for accessing lesson data from the database.
     */
    private final ILessonRepository lessonRepository;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        CourseStatus exceptStatus = null;

        if (authentication.getAuthorities().stream()
                .noneMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"))) {
            if (status == null) {
                exceptStatus = CourseStatus.DRAFT;
            } else if (status == CourseStatus.DRAFT) {
                throw new AccessDeniedException("Student and Teacher cannot see Draft courses");
            }
        }

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
                keyword, status, exceptStatus, teacherId, priceMin, priceMax, durationHoursMin, durationHoursMax, pageable
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

    @Override
    public CourseDetailResponse getCourseDetail(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("Course with id " + id + " not found");
                });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        if (course.getStatus() == CourseStatus.DRAFT && authentication.getAuthorities().stream()
                .noneMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"))) {
            throw new AccessDeniedException("Student and Teacher cannot see Draft course");
        }

        List<Lesson> lessons = lessonRepository.findByCourse_IdAndIsPublishedTrueOrderByOrderIndexAsc(id);

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacher(
                        CourseTeacherResponse.builder()
                                .id(course.getTeacher().getId())
                                .username(course.getTeacher().getUsername())
                                .fullName(course.getTeacher().getFullName())
                                .build()
                ).price(course.getPrice())
                .durationHours(course.getDurationHours())
                .status(course.getStatus())
                .lessons(
                        lessons.stream()
                                .map(
                                        lesson -> LessonResponse.builder()
                                                .id(lesson.getLessonId())
                                                .title(lesson.getTitle())
                                                .contentUrl(lesson.getContentUrl())
                                                .textContent(lesson.getTextContent())
                                                .orderIndex(lesson.getOrderIndex())
                                                .isPublished(lesson.getIsPublished())
                                                .createdAt(lesson.getCreatedAt())
                                                .updatedAt(lesson.getUpdatedAt())
                                                .build()
                                ).toList()
                ).createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

}
