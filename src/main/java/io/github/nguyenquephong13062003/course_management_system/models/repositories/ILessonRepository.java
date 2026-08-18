package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Lesson entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Lesson entities.
 */
public interface ILessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Retrieves a list of published lessons for a specific course, ordered by their order index in ascending order.
     *
     * @param courseId The ID of the course for which to retrieve published lessons.
     * @return A list of published Lesson entities associated with the specified course, ordered by order index.
     */
    List<Lesson> findByCourse_IdAndIsPublishedTrueOrderByOrderIndexAsc(
            Long courseId
    );

    /**
     * Checks if any lessons exist for a specific course.
     *
     * @param courseId The ID of the course to check for existing lessons.
     * @return true if there are lessons associated with the specified course, false otherwise.
     */
    boolean existsByCourseId(Long courseId);

    /**
     * Retrieves a published lesson by its ID.
     *
     * @param lessonId The ID of the lesson to retrieve.
     * @return An Optional containing the Lesson entity if found and published, or an empty Optional if not found or not published.
     */
    Optional<Lesson> findByLessonIdAndIsPublishedTrue(Long lessonId);

    /**
     * Checks if a lesson exists for a specific course and order index.
     *
     * @param courseId   The ID of the course to check for existing lessons.
     * @param orderIndex The order index of the lesson to check for existence.
     * @return true if a lesson exists with the specified course ID and order index, false otherwise.
     */
    Boolean existsByCourse_IdAndOrderIndex(Long courseId, Integer orderIndex);

    /**
     * Counts the number of published lessons for a specific course.
     *
     * @param courseId The ID of the course for which to count published lessons.
     * @return The count of published lessons associated with the specified course.
     */
    long countByCourseIdAndIsPublishedTrue(Long courseId);

    /**
     * Retrieves a published lesson by its ID and associated course ID.
     *
     * @param lessonId The ID of the lesson to retrieve.
     * @param courseId The ID of the course associated with the lesson.
     * @return An Optional containing the Lesson entity if found and published, or an empty Optional if not found or not published.
     */
    Optional<Lesson> findByLessonIdAndCourseIdAndIsPublishedTrue(
        Long lessonId,
        Long courseId
    );


    /**
     * Counts the number of published lessons for a specific course.
     *
     * @param courseId The ID of the course for which to count published lessons.
     * @return The count of published lessons associated with the specified course.
     */
    long countByCourse_IdAndIsPublishedTrue(Long courseId);

    /**
     * Retrieves a published lesson by its ID and associated course ID.
     *
     * @param lessonId The ID of the lesson to retrieve.
     * @param courseId The ID of the course associated with the lesson.
     * @return An Optional containing the Lesson entity if found and published, or an empty Optional if not found or not published.
     */
    Optional<Lesson> findByLessonIdAndCourse_IdAndIsPublishedTrue(
        Long lessonId,
        Long courseId
    );
    
}
