package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Course entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Course entities.
 */
public interface ICourseRepository extends JpaRepository<Course, Long> {
}
