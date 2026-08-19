package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO representing a course review.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    /**
     * The unique identifier of the review.
     */
    private Long id;

    /**
     * The ID of the course being reviewed.
     */
    private Long courseId;

    /**
     * The ID of the student who created the review.
     */
    private Long studentId;

    /**
     * The full name of the student who created the review.
     */
    private String studentName;

    /**
     * The rating given to the course.
     */
    private Integer rating;

    /**
     * The review comment.
     */
    private String comment;

    /**
     * The timestamp when the review was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the review was last updated.
     */
    private LocalDateTime updatedAt;
}
