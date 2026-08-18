package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

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
    long deleteAllByLesson_lessonId(Long lessonId);

}
