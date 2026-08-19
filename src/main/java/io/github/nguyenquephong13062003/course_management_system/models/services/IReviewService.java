package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.ReviewRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.ReviewResponse;

import java.util.List;

/**
 * Service interface for course review operations.
 */
public interface IReviewService {

    /**
     * Retrieves all reviews belonging to a course.
     *
     * @param courseId the course identifier
     * @return list of review responses
     */
    List<ReviewResponse> getCourseReviews(Long courseId);

    /**
     * Creates a review for a course by the currently authenticated student.
     *
     * @param courseId the course identifier
     * @param request  review creation request
     * @return created review response
     */
    ReviewResponse createReview(
            Long courseId,
            ReviewRequest request
    );

    /**
     * Updates an existing review.
     *
     * @param reviewId the review identifier
     * @param request  review update request
     * @return updated review response
     */
    ReviewResponse updateReview(
            Long reviewId,
            ReviewRequest request
    );

    /**
     * Deletes an existing review.
     *
     * @param reviewId the review identifier
     */
    void deleteReview(Long reviewId);
}
