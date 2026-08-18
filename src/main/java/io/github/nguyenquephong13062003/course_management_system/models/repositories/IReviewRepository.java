package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Review entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Review entities.
 */
public interface IReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Checks if a review exists for a given course ID.
     *
     * @param courseId the ID of the course to check for review existence
     * @return true if a review exists for the specified course ID, false otherwise
     */
    boolean existsByCourseId(Long courseId);

    boolean existsByStudent_Id(Long studentId);
    
}
