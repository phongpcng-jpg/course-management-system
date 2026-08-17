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

    Optional<Lesson> findBylessonIdAndIsPublishedTrue(Long lessonId);
    
}
