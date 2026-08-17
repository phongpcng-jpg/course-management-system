package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Lesson entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Lesson entities.
 */
public interface ILessonRepository extends JpaRepository<Lesson, Long> {
}
