package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.LessonProgress;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing LessonProgress entities.
 */
public interface ILessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    /**
     * Deletes all LessonProgress records associated with a specific lesson ID.
     *
     * @param lessonId the ID of the lesson
     * @return the number of records deleted
     */
    long deleteAllByLesson_LessonId(Long lessonId);

    /**
     * Retrieves a LessonProgress record based on the enrollment ID and lesson ID.
     *
     * @param enrollmentId the ID of the enrollment
     * @param lessonId     the ID of the lesson
     * @return an Optional containing the LessonProgress entity if found, or empty if not found
     */
    Optional<LessonProgress> findByEnrollmentIdAndLessonLessonId(
        Long enrollmentId,
        Long lessonId
    );

    /**
     * Counts the number of completed lessons for a specific enrollment ID.
     *
     * @param enrollmentId the ID of the enrollment
     * @return the count of completed lessons
     */
    long countByEnrollmentIdAndCompletedTrue(
        Long enrollmentId
    );

    /**
     * Counts the number of completed and published lessons for a specific enrollment ID.
     *
     * @param enrollmentId the ID of the enrollment
     * @return the count of completed and published lessons
     */
    long countByEnrollmentIdAndCompletedTrueAndLessonIsPublishedTrue(
        Long enrollmentId
    );

    /**
     * Counts the number of published lessons for a specific course.
     *
     * @param courseId The ID of the course for which to count published lessons.
     * @return The count of published lessons associated with the specified course.
     */
    @Query("""
            SELECT COUNT(lp)
            FROM LessonProgress lp
            JOIN lp.lesson l
            WHERE lp.enrollment.id = :enrollmentId
              AND lp.completed = true
              AND l.isPublished = true
            """)
    long countCompletedPublishedLessons(
            @Param("enrollmentId") Long enrollmentId
    );

}
