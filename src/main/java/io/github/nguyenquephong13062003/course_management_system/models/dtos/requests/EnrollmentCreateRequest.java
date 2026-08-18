package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used to enroll the authenticated student in a course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCreateRequest {

    /**
     * The ID of the course to enroll in.
     */
    @NotNull(message = "Course ID must not be null")
    private Long courseId;
}