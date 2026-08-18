package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.DuplicateResourceException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.InvalidStateTransitionException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.EnrollmentCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonProgressResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.*;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ICourseRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IEnrollmentRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonProgressRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.IEnrollmentService;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing enrollments.
 *
 * <p>
 * This service provides methods to handle enrollment-related operations,
 * including retrieving enrollments, enrolling in courses, fetching enrollment
 * details, and marking lessons as completed. It ensures that only active
 * students can enroll in published courses and maintains the progress of
 * lessons within each enrollment.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements IEnrollmentService {

    /**
     * Repository for managing enrollment entities.
     */
    private final IEnrollmentRepository enrollmentRepository;

    /**
     * Repository for managing lesson progress entities.
     */
    private final ILessonProgressRepository lessonProgressRepository;

    /**
     * Repository for managing lesson entities.
     */
    private final ILessonRepository lessonRepository;

    /**
     * Repository for managing course entities.
     */
    private final ICourseRepository courseRepository;

    /**
     * Repository for managing user entities.
     */
    private final IUserRepository userRepository;

    /**
     * API 22:
     * Retrieves all enrollments belonging to the authenticated student.
     *
     * @return the student's enrollments
     */
    @Override
    public List<EnrollmentResponse> getMyEnrollments(
    ) {

        Long studentId = getAuthenticatedStudentId();

        log.debug(
                "Fetching enrollments for studentId={}",
                studentId
        );

        List<Enrollment> enrollments =
                enrollmentRepository
                        .findAllByStudentIdOrderByEnrollmentDateDesc(
                                studentId
                        );

        log.debug(
                "Fetched {} enrollments for studentId={}",
                enrollments.size(),
                studentId
        );

        return enrollments.stream()
                .map(this::toEnrollmentResponse)
                .toList();
    }

    /**
     * API 23:
     * Enrolls the authenticated student in a published course.
     *
     * @param request   the enrollment request
     * @return the created enrollment
     */
    @Override
    @Transactional
    public EnrollmentResponse enrollCourse(
            EnrollmentCreateRequest request
    ) {

        Long studentId = getAuthenticatedStudentId();

        Long courseId = request.getCourseId();

        log.debug(
                "Processing course enrollment: studentId={}, courseId={}",
                studentId,
                courseId
        );

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(
                        "Student not found with ID: " + studentId
                ));

        validateStudent(student);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(
                        "Course not found with ID: " + courseId
                ));

        validateCourseAvailableForEnrollment(course);

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                studentId,
                courseId
        )) {

            log.warn(
                    "Student is already enrolled: studentId={}, courseId={}",
                    studentId,
                    courseId
            );

            throw new DuplicateResourceException(
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .progressPercentage(BigDecimal.ZERO)
                .build();

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        log.debug(
                "Enrollment created successfully: enrollmentId={}, studentId={}, courseId={}",
                savedEnrollment.getId(),
                studentId,
                courseId
        );

        return toEnrollmentResponse(savedEnrollment);
    }

    /**
     * API 24:
     * Retrieves detailed enrollment information together with
     * the progress of all published lessons.
     *
     * @param enrollmentId the ID of the enrollment
     * @return detailed enrollment information
     */
    @Override
    public EnrollmentDetailResponse getEnrollmentDetail(
            Long enrollmentId
    ) {

        Long studentId = getAuthenticatedStudentId();

        log.debug(
                "Fetching enrollment detail: studentId={}, enrollmentId={}",
                studentId,
                enrollmentId
        );

        Enrollment enrollment =
                enrollmentRepository.findByIdAndStudentId(
                                enrollmentId,
                                studentId
                        )
                        .orElseThrow(() -> new NotFoundException(
                                "Enrollment not found with ID: " + enrollmentId
                        ));

        return toEnrollmentDetailResponse(enrollment);
    }

    /**
     * API 25:
     * Marks a published lesson as completed and recalculates
     * the enrollment progress.
     *
     * @param enrollmentId the ID of the enrollment
     * @param lessonId     the ID of the lesson
     * @return updated enrollment detail
     */
    @Override
    @Transactional
    public EnrollmentDetailResponse completeLesson(
            Long enrollmentId,
            Long lessonId
    ) {

        Long studentId = getAuthenticatedStudentId();

        log.debug(
                "Processing lesson completion: studentId={}, enrollmentId={}, lessonId={}",
                studentId,
                enrollmentId,
                lessonId
        );

        Enrollment enrollment =
                enrollmentRepository.findByIdAndStudentId(
                                enrollmentId,
                                studentId
                        )
                        .orElseThrow(() -> new NotFoundException(
                                "Enrollment not found with ID: " + enrollmentId
                        ));

        validateEnrollmentCanUpdateProgress(enrollment);

        Long courseId = enrollment.getCourse().getId();

        Lesson lesson =
                lessonRepository
                        .findByLessonIdAndCourse_IdAndIsPublishedTrue(
                                lessonId,
                                courseId
                        )
                        .orElseThrow(() -> new NotFoundException(
                                "Published lesson not found with ID: "
                                        + lessonId
                        ));

        LessonProgress lessonProgress =
                lessonProgressRepository
                        .findByEnrollmentIdAndLessonLessonId(
                                enrollmentId,
                                lessonId
                        )
                        .orElseGet(() -> LessonProgress.builder()
                                .enrollment(enrollment)
                                .lesson(lesson)
                                .completed(false)
                                .build()
                        );

        if (!lessonProgress.isCompleted()) {

            lessonProgress.setCompleted(true);
            lessonProgress.setCompletedAt(LocalDateTime.now());

            log.debug(
                    "Lesson marked as completed: enrollmentId={}, lessonId={}",
                    enrollmentId,
                    lessonId
            );
        } else {

            log.debug(
                    "Lesson was already completed: enrollmentId={}, lessonId={}",
                    enrollmentId,
                    lessonId
            );
        }

        lessonProgress.setLastAccessedAt(LocalDateTime.now());

        lessonProgressRepository.save(lessonProgress);

        recalculateEnrollmentProgress(enrollment);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        log.debug(
                "Enrollment progress recalculated: enrollmentId={}, progress={}, status={}",
                savedEnrollment.getId(),
                savedEnrollment.getProgressPercentage(),
                savedEnrollment.getStatus()
        );

        return toEnrollmentDetailResponse(savedEnrollment);
    }

    /**
     * Validates that the user is an active student.
     */
    private void validateStudent(User student) {

        if (student.getRole() != UserRole.STUDENT) {

            log.warn(
                    "User is not a student: userId={}, role={}",
                    student.getId(),
                    student.getRole()
            );

            throw new InvalidStateTransitionException(
                    "Only STUDENT users can enroll in courses"
            );
        }

        if (!Boolean.TRUE.equals(student.getActive())) {

            log.warn(
                    "Inactive student attempted to enroll: studentId={}",
                    student.getId()
            );

            throw new InvalidStateTransitionException(
                    "Inactive student cannot enroll in courses"
            );
        }
    }

    /**
     * Validates that a course can currently accept enrollments.
     */
    private void validateCourseAvailableForEnrollment(
            Course course
    ) {

        if (course.getStatus() != CourseStatus.PUBLISHED) {

            log.warn(
                    "Course is not available for enrollment: courseId={}, status={}",
                    course.getId(),
                    course.getStatus()
            );

            throw new InvalidStateTransitionException(
                    "Only PUBLISHED courses can be enrolled"
            );
        }
    }

    /**
     * Validates that an enrollment can still receive lesson progress.
     */
    private void validateEnrollmentCanUpdateProgress(
            Enrollment enrollment
    ) {

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {

            log.warn(
                    "Enrollment cannot be updated: enrollmentId={}, status={}",
                    enrollment.getId(),
                    enrollment.getStatus()
            );

            throw new InvalidStateTransitionException(
                    "Lesson progress can only be updated for an ENROLLED enrollment"
            );
        }
    }

    /**
     * Recalculates enrollment progress using:
     * <p>
     * completed published lessons / total published lessons * 100
     */
    private void recalculateEnrollmentProgress(
            Enrollment enrollment
    ) {

        Long enrollmentId = enrollment.getId();
        Long courseId = enrollment.getCourse().getId();

        long totalPublishedLessons =
                lessonRepository
                        .countByCourse_IdAndIsPublishedTrue(courseId);

        if (totalPublishedLessons == 0) {

            enrollment.setProgressPercentage(BigDecimal.ZERO);
            enrollment.setStatus(EnrollmentStatus.ENROLLED);
            enrollment.setCompletionDate(null);

            log.debug(
                    "Course has no published lessons: courseId={}",
                    courseId
            );

            return;
        }

        long completedPublishedLessons =
                lessonProgressRepository
                        .countByEnrollmentIdAndCompletedTrueAndLessonIsPublishedTrue(
                                enrollmentId
                        );

        BigDecimal progressPercentage =
                BigDecimal.valueOf(completedPublishedLessons)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                BigDecimal.valueOf(totalPublishedLessons),
                                2,
                                RoundingMode.HALF_UP
                        );

        enrollment.setProgressPercentage(progressPercentage);

        if (completedPublishedLessons == totalPublishedLessons) {

            enrollment.setStatus(EnrollmentStatus.COMPLETED);

            if (enrollment.getCompletionDate() == null) {
                enrollment.setCompletionDate(LocalDateTime.now());
            }

            log.debug(
                    "Enrollment completed: enrollmentId={}, courseId={}",
                    enrollmentId,
                    courseId
            );

        } else {

            enrollment.setStatus(EnrollmentStatus.ENROLLED);
            enrollment.setCompletionDate(null);
        }
    }

    /**
     * Converts Enrollment entity to EnrollmentResponse.
     */
    private EnrollmentResponse toEnrollmentResponse(
            Enrollment enrollment
    ) {

        Course course = enrollment.getCourse();

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .progressPercentage(enrollment.getProgressPercentage())
                .completionDate(enrollment.getCompletionDate())
                .build();
    }

    /**
     * Converts Enrollment entity to EnrollmentDetailResponse.
     *
     * <p>
     * Under Approach B, LessonProgress only exists after a lesson
     * has been completed. Therefore, this method first retrieves
     * all published lessons and then merges existing progress records.
     * </p>
     */
    private EnrollmentDetailResponse toEnrollmentDetailResponse(
            Enrollment enrollment
    ) {

        Long enrollmentId = enrollment.getId();
        Long courseId = enrollment.getCourse().getId();

        List<Lesson> publishedLessons =
                lessonRepository
                        .findByCourse_IdAndIsPublishedTrueOrderByOrderIndexAsc(
                                courseId
                        );

        Map<Long, LessonProgress> progressByLessonId =
                enrollment.getLessonProgresses()
                        .stream()
                        .collect(Collectors.toMap(
                                progress ->
                                        progress.getLesson().getLessonId(),
                                Function.identity(),
                                (existing, replacement) -> existing
                        ));

        List<LessonProgressResponse> lessonProgresses =
                publishedLessons.stream()
                        .map(lesson -> {

                            LessonProgress progress =
                                    progressByLessonId.get(
                                            lesson.getLessonId()
                                    );

                            return LessonProgressResponse.builder()
                                    .lessonId(lesson.getLessonId())
                                    .lessonTitle(lesson.getTitle())
                                    .orderIndex(lesson.getOrderIndex())
                                    .completed(
                                            progress != null
                                                    && progress.isCompleted()
                                    )
                                    .completedAt(
                                            progress != null
                                                    ? progress.getCompletedAt()
                                                    : null
                                    )
                                    .build();
                        })
                        .toList();

        Course course = enrollment.getCourse();

        return EnrollmentDetailResponse.builder()
                .enrollmentId(enrollmentId)
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .progressPercentage(enrollment.getProgressPercentage())
                .completionDate(enrollment.getCompletionDate())
                .lessons(lessonProgresses)
                .build();
    }

    private Long getAuthenticatedStudentId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new AuthException("JWT verification failed: User details is missing or unauthenticated");
        }

        return userDetails.getUser().getId();

    }
    
}
