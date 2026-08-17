package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO for updating a course.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseUpdateRequest {

    /**
     * The title of the course.
     */
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    /**
     * The description of the course.
     */
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    /**
     * The ID of the teacher responsible for the course.
     */
    @NotNull(message = "Teacher ID must not be null")
    @Min(value = 1, message = "Teacher ID must be greater than 0")
    private Long teacherId;

    /**
     * The price of the course.
     */
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must not be negative")
    private BigDecimal price;

    /**
     * The duration of the course in hours.
     */
    @Min(value = 0, message = "Duration hours must not be negative")
    private Integer durationHours;

}
