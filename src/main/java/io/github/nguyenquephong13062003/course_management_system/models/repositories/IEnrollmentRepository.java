package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Enrollment entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Enrollment entities.
 */
public interface IEnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Checks if an enrollment exists for a given course ID.
     *
     * @param courseId the ID of the course to check for enrollment existence
     * @return true if an enrollment exists for the specified course ID, false otherwise
     */
    boolean existsByCourseId(Long courseId);

}
