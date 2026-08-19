package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO representing the progress of a student in a specific course.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseProgressResponse {

    /**
     * The unique identifier of the course.
     */
    private Long courseId;

    /**
     * The title of the course.
     */
    private String courseTitle;

    /**
     * The unique identifier of the enrollment.
     */
    private Long enrollmentId;

    /**
     * The current enrollment status.
     */
    private EnrollmentStatus enrollmentStatus;

    /**
     * The progress percentage of the student in the course.
     */
    private BigDecimal progressPercentage;

    /**
     * The total number of published lessons in the course.
     */
    private Long totalPublishedLessons;

    /**
     * The total number of published lessons completed by the student.
     */
    private Long completedLessons;

    /**
     * The date and time when the student completed the course.
     */
    private LocalDateTime completionDate;

    public StudentCourseProgressResponse(Long courseId, String courseTitle, Long enrollmentId, EnrollmentStatus enrollmentStatus, BigDecimal progressPercentage, LocalDateTime completionDate) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrollmentId = enrollmentId;
        this.enrollmentStatus = enrollmentStatus;
        this.progressPercentage = progressPercentage;
        this.totalPublishedLessons = 0L;
        this.completedLessons = 0L;
        this.completionDate = completionDate;
    }
}
