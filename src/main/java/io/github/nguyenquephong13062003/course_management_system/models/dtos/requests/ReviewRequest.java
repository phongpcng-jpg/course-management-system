package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating or updating a course review.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    /**
     * The rating given to the course.
     */
    @NotNull(message = "Rating must not be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    /**
     * The optional comment associated with the review.
     */
    @Size(
            max = 2000,
            message = "Comment must not exceed 2000 characters"
    )
    private String comment;
}