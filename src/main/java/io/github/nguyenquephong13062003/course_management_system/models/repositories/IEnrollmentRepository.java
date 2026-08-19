package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.StudentCourseProgressResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Enrollment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Retrieves all course enrollments belonging to a student.
     *
     * @param studentId the student identifier
     * @return a list of student course progress responses
     */
    @Query("""
            SELECT new io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.StudentCourseProgressResponse(
                c.id,
                c.title,
                e.id,
                e.status,
                e.progressPercentage,
                e.completionDate
            )
            FROM Enrollment e
            JOIN e.course c
            WHERE e.student.id = :studentId
            ORDER BY e.enrollmentDate DESC, e.id DESC
            """)
    List<StudentCourseProgressResponse> findStudentCourseProgress(
            @Param("studentId") Long studentId
    );

    /**
     * Checks whether a student has an active or completed enrollment
     * for a specific course.
     *
     * @param studentId the student identifier
     * @param courseId  the course identifier
     * @return true if the student is enrolled or has completed the course
     */
    boolean existsByStudentIdAndCourseIdAndStatusIn(
            Long studentId,
            Long courseId,
            Collection<EnrollmentStatus> statuses
    );

    @Query("""
        SELECT DISTINCT e.student
        FROM Enrollment e
        WHERE e.course.id = :courseId
    """)
    List<User> findStudentsByCourseId(@Param("courseId") Long courseId);
}
