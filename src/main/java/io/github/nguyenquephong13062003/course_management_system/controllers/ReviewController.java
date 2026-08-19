package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.ReviewRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.ReviewResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling review-related API requests.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    /**
     * Service for handling review-related business logic.
     */
    private final IReviewService reviewService;

    /**
     * Updates an existing review.
     *
     * @param reviewId the ID of the review
     * @param request the review update request
     * @return the updated review
     */
    @PutMapping("/{review_id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable("review_id") Long reviewId,
            @Valid @RequestBody ReviewRequest request
    ) {

        log.info(
                "Received update review request. reviewId={}, rating={}",
                reviewId,
                request.getRating()
        );

        ReviewResponse response =
                reviewService.updateReview(reviewId, request);

        log.info(
                "Review updated successfully. reviewId={}",
                reviewId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Review updated successfully",
                        response
                )
        );
    }

    /**
     * Deletes an existing review.
     *
     * @param reviewId the ID of the review
     * @return an empty successful response
     */
    @DeleteMapping("/{review_id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable("review_id") Long reviewId
    ) {

        log.info(
                "Received delete review request. reviewId={}",
                reviewId
        );

        reviewService.deleteReview(reviewId);

        log.info(
                "Review deleted successfully. reviewId={}",
                reviewId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Review deleted successfully",
                        null
                )
        );
    }
}