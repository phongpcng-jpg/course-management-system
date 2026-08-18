package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO representing detailed enrollment information,
 * including the progress of published lessons.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDetailResponse {

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

    /**
     * The progress information of published lessons in the course.
     */
    @Builder.Default
    private List<LessonProgressResponse> lessons = new ArrayList<>();
}
