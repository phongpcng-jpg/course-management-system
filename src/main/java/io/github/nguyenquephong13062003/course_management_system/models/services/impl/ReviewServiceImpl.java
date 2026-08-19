package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.ReviewRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.ReviewResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Review;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ICourseRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IEnrollmentRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IReviewRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.IReviewService;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of review-related business operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewServiceImpl implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IEnrollmentRepository enrollmentRepository;
    private final ICourseRepository courseRepository;

    @Override
    public List<ReviewResponse> getCourseReviews(Long courseId) {

        log.debug(
                "Fetching reviews for courseId={}",
                courseId
        );

        Course course = getCourseById(courseId);

        return reviewRepository
                .findAllByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(this::toReviewResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse createReview(
            Long courseId,
            ReviewRequest request
    ) {

        User currentUser = getCurrentUser();

        log.debug(
                "Creating review. courseId={}, studentId={}",
                courseId,
                currentUser.getId()
        );

        boolean canReview =
                enrollmentRepository.existsByStudentIdAndCourseIdAndStatusIn(
                        currentUser.getId(),
                        courseId,
                        List.of(
                                EnrollmentStatus.ENROLLED,
                                EnrollmentStatus.COMPLETED
                        )
                );

        if (!canReview) {
            log.warn(
                    "Student is not eligible to review course. studentId={}, courseId={}",
                    currentUser.getId(),
                    courseId
            );

            throw new AccessDeniedException(
                    "Student has not enrolled in this course"
            );
        }

        boolean alreadyReviewed =
                reviewRepository.existsByCourseIdAndStudentId(
                        courseId,
                        currentUser.getId()
                );

        if (alreadyReviewed) {
            log.warn(
                    "Duplicate review attempt. studentId={}, courseId={}",
                    currentUser.getId(),
                    courseId
            );

            throw new IllegalStateException(
                    "Student has already reviewed this course"
            );
        }

        Course course = getCourseById(courseId);

        Review review = Review.builder()
                .course(course)
                .student(currentUser)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        log.debug(
                "Review created successfully. reviewId={}, courseId={}, studentId={}",
                savedReview.getId(),
                courseId,
                currentUser.getId()
        );

        return toReviewResponse(savedReview);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(
            Long reviewId,
            ReviewRequest request
    ) {

        User currentUser = getCurrentUser();

        log.debug(
                "Updating review. reviewId={}, userId={}",
                reviewId,
                currentUser.getId()
        );

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Review not found"
                        )
                );

        validateReviewOwnership(review, currentUser);

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        log.debug(
                "Review updated successfully. reviewId={}",
                reviewId
        );

        return toReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {

        User currentUser = getCurrentUser();

        log.debug(
                "Deleting review. reviewId={}, userId={}",
                reviewId,
                currentUser.getId()
        );

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Review not found"
                        )
                );

        validateReviewOwnership(review, currentUser);

        reviewRepository.delete(review);

        log.debug(
                "Review deleted successfully. reviewId={}",
                reviewId
        );
    }

    private void validateReviewOwnership(
            Review review,
            User currentUser
    ) {

        boolean isAdmin =
                currentUser.getRole().name().equals("ADMIN");

        boolean isOwner =
                review.getStudent().getId()
                        .equals(currentUser.getId());

        if (!isAdmin && !isOwner) {

            log.warn(
                    "Unauthorized review modification attempt. reviewId={}, userId={}",
                    review.getId(),
                    currentUser.getId()
            );

            throw new AccessDeniedException(
                    "You do not have permission to modify this review"
            );
        }
    }

    private ReviewResponse toReviewResponse(Review review) {

        return ReviewResponse.builder()
                .id(review.getId())
                .courseId(review.getCourse().getId())
                .studentId(review.getStudent().getId())
                .studentName(review.getStudent().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthException(
                    "JWT verification failed: invalid authentication principal"
            );
        }

        return  userDetails.getUser();
    }

    private Course getCourseById(Long courseId) {

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id " + courseId + " not found"));

    }
}
