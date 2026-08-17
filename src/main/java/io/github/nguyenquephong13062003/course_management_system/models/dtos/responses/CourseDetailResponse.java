package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The CourseDetailResponse class represents the detailed response for a course in the course management system.
 * It contains information about the course, its teacher, lessons, and other relevant details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {

    /**
     * The unique identifier for the course.
     */
    private Long id;

    /**
     * The title of the course.
     */
    private String title;

    /**
     * The description of the course.
     */
    private String description;

    /**
     * The teacher associated with the course.
     */
    private CourseTeacherResponse teacher;

    /**
     * The price of the course.
     */
    private BigDecimal price;

    /**
     * The duration of the course in hours.
     */
    private Integer durationHours;

    /**
     * The status of the course (e.g., ACTIVE, INACTIVE).
     */
    private CourseStatus status;

    /**
     * The timestamp when the course was created.
     */
    private List<LessonResponse> lessons;

    /**
     * The timestamp when the course was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the course was last updated.
     */
    private LocalDateTime updatedAt;

}
