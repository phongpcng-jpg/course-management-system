package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.entities.Enrollment;

import java.util.List;
import java.util.Optional;

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

    /**
     * Checks if an enrollment exists for a given student ID and course ID.
     *
     * @param studentId the ID of the student to check for enrollment existence
     * @param courseId  the ID of the course to check for enrollment existence
     * @return true if an enrollment exists for the specified student ID and course ID, false otherwise
     */
    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    /**
     * Retrieves a list of enrollments for a specific student, ordered by enrollment date in descending order.
     *
     * @param studentId the ID of the student for which to retrieve enrollments
     * @return a list of Enrollment entities associated with the specified student, ordered by enrollment date
     */
    List<Enrollment> findAllByStudentIdOrderByEnrollmentDateDesc(
            Long studentId
    );

    /**
     * Retrieves an enrollment by its ID and the associated student ID.
     *
     * @param enrollmentId the ID of the enrollment to retrieve
     * @param studentId    the ID of the student associated with the enrollment
     * @return an Optional containing the Enrollment entity if found, or empty if not found
     */
    Optional<Enrollment> findByIdAndStudentId(
        Long enrollmentId,
        Long studentId
    );

    boolean existsByStudent_Id(Long studentId);

}
