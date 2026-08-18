package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.*;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.internals.CloudinaryUploadResult;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseStatusUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseTeacherResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.*;
import io.github.nguyenquephong13062003.course_management_system.models.services.ICourseService;
import io.github.nguyenquephong13062003.course_management_system.models.services.uploads.UploadService;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
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

    /**
     * The IUserRepository instance used for accessing user data from the database.
     */
    private final IUserRepository userRepository;

    /**
     * The IEnrollmentRepository instance used for accessing enrollment data from the database.
     */
    private final IEnrollmentRepository enrollmentRepository;

    /**
     * The IReviewRepository instance used for accessing review data from the database.
     */
    private final IReviewRepository reviewRepository;

    /**
     * The UploadService instance used for handling file uploads.
     */
    private final UploadService uploadService;

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

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthException(
                    "JWT verification failed: invalid authentication principal"
            );
        }

        if (userDetails.getUser().getRole() != UserRole.ADMIN) {
            if (status == null) {
                status = CourseStatus.PUBLISHED;
            } else if (status != CourseStatus.PUBLISHED) {
                throw new InvalidStateTransitionException(
                        "ADMIN sees all but others see only `PUBLISHED`"
                );
            }
        }

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 20;
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
                keyword, status, null, teacherId, priceMin, priceMax, durationHoursMin, durationHoursMax, pageable
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
                .orElseThrow(() -> new NotFoundException("Course with id " + id + " not found"));

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

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new NotFoundException("User with id " + request.getTeacherId() + " not found"));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new InvalidInputDataException("The specified user is not a teacher");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .price(request.getPrice())
                .durationHours(request.getDurationHours())
                .build();

        Course savedCourse = courseRepository.save(course);

        return toCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long courseId, CourseUpdateRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id " + courseId + " not found"));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new NotFoundException("User with id " + request.getTeacherId() + " not found"));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new InvalidInputDataException("The specified user is not a teacher");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);
        course.setPrice(request.getPrice());
        course.setDurationHours(request.getDurationHours());

        Course savedCourse = courseRepository.save(course);

        return toCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourseStatus(Long courseId, CourseStatusUpdateRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id " + courseId + " not found"));

        course.setStatus(request.getStatus());

        Course savedCourse = courseRepository.save(course);

        return toCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id " + courseId + " not found"));



        if (course.getStatus() != CourseStatus.DRAFT) {

            throw new InvalidStateTransitionException(
                    "Only DRAFT courses can be deleted"
            );
        }

        boolean hasLessons = lessonRepository.existsByCourseId(courseId);
        boolean hasEnrollments = enrollmentRepository.existsByCourseId(courseId);
        boolean hasReviews = reviewRepository.existsByCourseId(courseId);

        if (hasLessons || hasEnrollments || hasReviews) {
            log.warn(
                    "Cannot delete course id={} because it contains business data: " +
                            "lessons={}, enrollments={}, reviews={}",
                    courseId,
                    hasLessons,
                    hasEnrollments,
                    hasReviews
            );

            throw new CourseHasDependentDataException(
                    "Course cannot be deleted because it contains business data"
            );
        }

        courseRepository.deleteById(courseId);

    }

    @Override
    public List<LessonResponse> getAllPublishedLessonByCourseId(Long id) {

        courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course with id " + id + " not found"));

        List<Lesson> lessons = lessonRepository.findByCourse_IdAndIsPublishedTrueOrderByOrderIndexAsc(id);

        return lessons.stream()
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
                ).toList();
        
    }

    @Override
    @Transactional
    public LessonResponse createLesson(Long course_id, LessonCreateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new NotFoundException("Course with id " + course_id + " not found"));

        if (!authentication.getName().equals(course.getTeacher().getUsername()) && authentication.getAuthorities().stream()
                .noneMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"))) {

            throw new AccessDeniedException("Only Admin and Teacher who is in charge of the course can create lesson of course");

        }

        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new InvalidStateTransitionException("Cannot create lesson for a course that is archived");
        }

        if (lessonRepository.existsByCourse_IdAndOrderIndex(course_id, request.getOrderIndex())) {

            throw new DuplicateResourceException(
                    "Lesson with course_id '" + course_id + "' and orderIndex '" + request.getOrderIndex() + "' already exists"
            );

        }

        CloudinaryUploadResult cloudinaryUploadResult = (request.getContent() == null || request.getContent().isEmpty())
                ? CloudinaryUploadResult.builder().build() : uploadService.upload(
                request.getContent()
        );

        Lesson lesson = Lesson.builder()
                .course(course)
                .title(request.getTitle())
                .contentUrl(cloudinaryUploadResult.getUrl())
                .contentPublicId(cloudinaryUploadResult.getPublicId())
                .textContent(request.getTextContent())
                .orderIndex(request.getOrderIndex())
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);

        return LessonResponse.builder()
                .id(savedLesson.getLessonId())
                .title(savedLesson.getTitle())
                .contentUrl(savedLesson.getContentUrl())
                .textContent(savedLesson.getTextContent())
                .orderIndex(savedLesson.getOrderIndex())
                .isPublished(savedLesson.getIsPublished())
                .createdAt(savedLesson.getCreatedAt())
                .updatedAt(savedLesson.getUpdatedAt())
                .build();
    }

    /**
     * Converts a Course entity to a CourseResponse DTO.
     *
     * @param course the Course entity to convert
     * @return the corresponding CourseResponse DTO
     */
    private CourseResponse toCourseResponse(Course course) {

        return CourseResponse.builder()
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
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();

    }

}
