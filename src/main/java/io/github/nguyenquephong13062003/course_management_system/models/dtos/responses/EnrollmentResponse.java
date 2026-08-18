package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO representing a student's enrollment in a course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {

    /**
     * The unique identifier of the enrollment.
     */
    private Long enrollmentId;

    /**
     * The ID of the enrolled course.
     */
    private Long courseId;

    /**
     * The title of the enrolled course.
     */
    private String courseTitle;

    /**
     * The date and time when the student enrolled in the course.
     */
    private LocalDateTime enrollmentDate;

    /**
     * The current status of the enrollment.
     */
    private EnrollmentStatus status;

    /**
     * The current completion percentage of the course.
     */
    private BigDecimal progressPercentage;

    /**
     * The date and time when the course was completed.
     */
    private LocalDateTime completionDate;
}
