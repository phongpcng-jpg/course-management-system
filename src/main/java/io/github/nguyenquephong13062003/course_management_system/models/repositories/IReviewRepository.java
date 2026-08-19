package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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

    /**
     * Checks if a review exists for a given student ID.
     *
     * @param studentId the ID of the student to check for review existence
     * @return true if a review exists for the specified student ID, false otherwise
     */
    boolean existsByStudent_Id(Long studentId);

    /**
     * Retrieves all reviews of a specific course ordered by creation time descending.
     *
     * @param courseId the course identifier
     * @return a list of reviews belonging to the course
     */
    List<Review> findAllByCourseIdOrderByCreatedAtDesc(Long courseId);

    /**
     * Checks whether a review already exists for a specific student and course.
     *
     * @param courseId  the course identifier
     * @param studentId the student identifier
     * @return true if the student has already reviewed the course
     */
    boolean existsByCourseIdAndStudentId(
            Long courseId,
            Long studentId
    );
    
}
