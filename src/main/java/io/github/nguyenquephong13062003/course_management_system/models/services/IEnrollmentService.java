package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.EnrollmentCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentResponse;

import java.util.List;

/**
 * Service interface for enrollment-related business operations.
 */
public interface IEnrollmentService {

    /**
     * Retrieves all enrollments belonging to the authenticated student.
     *
     * @param studentId the ID of the authenticated student
     * @return a list of enrollment responses
     */
    List<EnrollmentResponse> getMyEnrollments(Long studentId);

    /**
     * Enrolls the authenticated student in a published course.
     *
     * @param studentId the ID of the authenticated student
     * @param request   the enrollment request
     * @return the newly created enrollment response
     */
    EnrollmentResponse enrollCourse(
            Long studentId,
            EnrollmentCreateRequest request
    );

    /**
     * Retrieves detailed information about an enrollment owned by the authenticated student.
     *
     * @param studentId    the ID of the authenticated student
     * @param enrollmentId the ID of the enrollment
     * @return detailed enrollment information
     */
    EnrollmentDetailResponse getEnrollmentDetail(
            Long studentId,
            Long enrollmentId
    );

    /**
     * Marks a published lesson as completed for an enrollment.
     *
     * @param studentId    the ID of the authenticated student
     * @param enrollmentId the ID of the enrollment
     * @param lessonId     the ID of the lesson
     * @return the updated enrollment detail
     */
    EnrollmentDetailResponse completeLesson(
            Long studentId,
            Long enrollmentId,
            Long lessonId
    );
}
