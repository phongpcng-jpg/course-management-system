package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the response DTO for a course.
 * This class is used to transfer course data in API responses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseResponse {

    /**
     * The unique identifier for the course.
     */
    Long id;

    /**
     * The title of the course.
     */
    String title;

    /**
     * The description of the course.
     */
    String description;

    /**
     * The teacher associated with the course.
     */
    CourseTeacherResponse teacher;

    /**
     * The price of the course.
     */
    BigDecimal price;

    /**
     * The duration of the course in hours.
     */
    Integer durationHours;

    /**
     * The status of the course.
     */
    CourseStatus status;

    /**
     * The timestamp when the course was created.
     */
    LocalDateTime createdAt;

    /**
     * The timestamp when the course was last updated.
     */
    LocalDateTime updatedAt;

}
