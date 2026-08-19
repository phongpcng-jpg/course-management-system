package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.internals.CourseLessonCountResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.internals.EnrollmentCompletedLessonCountResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.*;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.*;
import io.github.nguyenquephong13062003.course_management_system.models.services.IReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of IReportService for administrative reports and analytics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportServiceImpl implements IReportService {

    /**
     * Repository for course-related report queries.
     */
    private final ICourseRepository courseRepository;

    /**
     * Repository for enrollment-related report queries.
     */
    private final IEnrollmentRepository enrollmentRepository;

    /**
     * Repository for lesson-related report queries.
     */
    private final ILessonRepository lessonRepository;

    /**
     * Repository for lesson progress-related report queries.
     */
    private final ILessonProgressRepository lessonProgressRepository;

    /**
     * Repository for user-related queries.
     */
    private final IUserRepository userRepository;

    /**
     * Retrieves the most popular courses based on enrollment count.
     *
     * @param limit maximum number of courses to return
     * @return list of top courses
     */
    @Override
    public List<TopCourseReportResponse> getTopCourses(int limit) {

        log.debug("Generating top courses report with limit={}", limit);

        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero"
            );
        }

        List<TopCourseReportResponse> response =
                courseRepository.findTopCourses(
                        PageRequest.of(0, limit)
                );

        log.debug(
                "Top courses report generated successfully. Result count={}",
                response.size()
        );

        return response;
    }

    /**
     * Retrieves the learning progress report of a specific student.
     *
     * @param studentId the student identifier
     * @return student's progress report
     */
    @Override
    public StudentProgressReportResponse getStudentProgress(Long studentId) {

        log.debug(
                "Generating student progress report for studentId={}",
                studentId
        );

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(
                        "Student with id " + studentId + " not found"
                ));

        if (student.getRole() != UserRole.STUDENT) {
            log.warn(
                    "User id={} is not a student. Actual role={}",
                    studentId,
                    student.getRole()
            );

            throw new NotFoundException(
                    "Student with id " + studentId + " not found"
            );
        }

        List<StudentCourseProgressResponse> courses =
                enrollmentRepository.findStudentCourseProgress(studentId);

        if (courses.isEmpty()) {
            return StudentProgressReportResponse.builder()
                    .studentId(student.getId())
                    .studentName(student.getFullName())
                    .studentUsername(student.getUsername())
                    .totalCourses(0L)
                    .completedCourses(0L)
                    .enrolledCourses(0L)
                    .overallProgressPercentage(BigDecimal.ZERO)
                    .courses(Collections.emptyList())
                    .build();
        }

        List<Long> courseIds = courses.stream()
                .map(StudentCourseProgressResponse::getCourseId)
                .distinct()
                .toList();

        List<Long> enrollmentIds = courses.stream()
                .map(StudentCourseProgressResponse::getEnrollmentId)
                .distinct()
                .toList();

        /*
         * Query 2:
         * Count published lessons for all enrolled courses.
         */
        Map<Long, Long> publishedLessonCountByCourse =
                lessonRepository
                        .countPublishedLessonsByCourseIds(courseIds)
                        .stream()
                        .collect(Collectors.toMap(
                                CourseLessonCountResponse::getCourseId,
                                CourseLessonCountResponse::getPublishedLessonCount
                        ));

        /*
         * Query 3:
         * Count completed published lessons for all enrollments.
         */
        Map<Long, Long> completedLessonCountByEnrollment =
                lessonProgressRepository
                        .countCompletedPublishedLessonsByEnrollmentIds(enrollmentIds)
                        .stream()
                        .collect(Collectors.toMap(
                                EnrollmentCompletedLessonCountResponse::getEnrollmentId,
                                EnrollmentCompletedLessonCountResponse::getCompletedLessonCount
                        ));

        long completedCourses = courses.stream()
                .filter(course ->
                        course.getEnrollmentStatus() == EnrollmentStatus.COMPLETED
                )
                .count();

        long enrolledCourses = courses.stream()
                .filter(course ->
                        course.getEnrollmentStatus() == EnrollmentStatus.ENROLLED
                )
                .count();

        /*
         * Calculate progress for every enrolled course.
         */
        courses.forEach(course -> {

            long totalPublishedLessons =
                    publishedLessonCountByCourse.getOrDefault(
                            course.getCourseId(),
                            0L
                    );

            long completedLessons =
                    completedLessonCountByEnrollment.getOrDefault(
                            course.getEnrollmentId(),
                            0L
                    );

            BigDecimal progressPercentage =
                    calculateProgressPercentage(
                            completedLessons,
                            totalPublishedLessons
                    );

            course.setTotalPublishedLessons(totalPublishedLessons);
            course.setCompletedLessons(completedLessons);
            course.setProgressPercentage(progressPercentage);
        });

        /*
         * Overall progress is calculated using total completed lessons
         * divided by total published lessons, rather than averaging
         * individual course percentages.
         */
        long totalPublishedLessons = courses.stream()
                .mapToLong(StudentCourseProgressResponse::getTotalPublishedLessons)
                .sum();

        long totalCompletedLessons = courses.stream()
                .mapToLong(StudentCourseProgressResponse::getCompletedLessons)
                .sum();

        BigDecimal overallProgressPercentage =
                calculateProgressPercentage(
                        totalCompletedLessons,
                        totalPublishedLessons
                );

        StudentProgressReportResponse response =
                StudentProgressReportResponse.builder()
                        .studentId(student.getId())
                        .studentName(student.getFullName())
                        .studentUsername(student.getUsername())
                        .totalCourses((long) courses.size())
                        .completedCourses(completedCourses)
                        .enrolledCourses(enrolledCourses)
                        .overallProgressPercentage(overallProgressPercentage)
                        .courses(courses)
                        .build();

        log.debug(
                "Student progress report generated successfully. " +
                        "studentId={}, totalCourses={}, completedCourses={}, enrolledCourses={}, overallProgress={}",
                studentId,
                courses.size(),
                completedCourses,
                enrolledCourses,
                overallProgressPercentage
        );

        return response;
    }

    /**
     * Retrieves an overview of all courses managed by a teacher.
     *
     * @param teacherId the teacher identifier
     * @return teacher courses overview report
     */
    @Override
    public TeacherCoursesOverviewResponse getTeacherCoursesOverview(
            Long teacherId
    ) {

        log.debug(
                "Generating teacher courses overview for teacherId={}",
                teacherId
        );

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(
                        "Teacher with id " + teacherId + " not found"
                ));

        if (teacher.getRole() != UserRole.TEACHER) {
            log.warn(
                    "User id={} is not a teacher. Actual role={}",
                    teacherId,
                    teacher.getRole()
            );

            throw new NotFoundException(
                    "Teacher with id " + teacherId + " not found"
            );
        }

        /*
         * Query 1:
         * Retrieve teacher courses and enrollment counts.
         */
        List<TeacherCourseOverviewItem> courses =
                courseRepository.findTeacherCoursesOverview(teacherId);

        if (courses.isEmpty()) {
            return TeacherCoursesOverviewResponse.builder()
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFullName())
                    .teacherUsername(teacher.getUsername())
                    .totalCourses(0L)
                    .draftCourses(0L)
                    .publishedCourses(0L)
                    .archivedCourses(0L)
                    .totalEnrollments(0L)
                    .totalPublishedLessons(0L)
                    .courses(Collections.emptyList())
                    .build();
        }

        List<Long> courseIds = courses.stream()
                .map(TeacherCourseOverviewItem::getCourseId)
                .toList();

        /*
         * Query 2:
         * Retrieve published lesson counts for all teacher courses.
         */
        Map<Long, Long> publishedLessonCountByCourse =
                lessonRepository
                        .countPublishedLessonsByCourseIds(courseIds)
                        .stream()
                        .collect(Collectors.toMap(
                                CourseLessonCountResponse::getCourseId,
                                CourseLessonCountResponse::getPublishedLessonCount
                        ));

        courses.forEach(course ->
                course.setPublishedLessonCount(
                        publishedLessonCountByCourse.getOrDefault(
                                course.getCourseId(),
                                0L
                        )
                )
        );

        long draftCourses = courses.stream()
                .filter(course -> course.getStatus() == CourseStatus.DRAFT)
                .count();

        long publishedCourses = courses.stream()
                .filter(course -> course.getStatus() == CourseStatus.PUBLISHED)
                .count();

        long archivedCourses = courses.stream()
                .filter(course -> course.getStatus() == CourseStatus.ARCHIVED)
                .count();

        long totalEnrollments = courses.stream()
                .mapToLong(TeacherCourseOverviewItem::getEnrollmentCount)
                .sum();

        long totalPublishedLessons = courses.stream()
                .mapToLong(TeacherCourseOverviewItem::getPublishedLessonCount)
                .sum();

        TeacherCoursesOverviewResponse response =
                TeacherCoursesOverviewResponse.builder()
                        .teacherId(teacher.getId())
                        .teacherName(teacher.getFullName())
                        .teacherUsername(teacher.getUsername())
                        .totalCourses((long) courses.size())
                        .draftCourses(draftCourses)
                        .publishedCourses(publishedCourses)
                        .archivedCourses(archivedCourses)
                        .totalEnrollments(totalEnrollments)
                        .totalPublishedLessons(totalPublishedLessons)
                        .courses(courses)
                        .build();

        log.debug(
                "Teacher courses overview generated successfully. " +
                        "teacherId={}, totalCourses={}, totalEnrollments={}",
                teacherId,
                courses.size(),
                totalEnrollments
        );

        return response;
    }

    /**
     * Calculates progress percentage from completed and published lessons.
     *
     * @param completedLessons number of completed lessons
     * @param totalPublishedLessons total number of published lessons
     * @return progress percentage rounded to two decimal places
     */
    private BigDecimal calculateProgressPercentage(
            long completedLessons,
            long totalPublishedLessons
    ) {

        if (totalPublishedLessons <= 0) {
            return BigDecimal.ZERO;
        }

        if (completedLessons <= 0) {
            return BigDecimal.ZERO;
        }

        long normalizedCompletedLessons =
                Math.min(completedLessons, totalPublishedLessons);

        return BigDecimal.valueOf(normalizedCompletedLessons)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(totalPublishedLessons),
                        2,
                        RoundingMode.HALF_UP
                );
    }
}
