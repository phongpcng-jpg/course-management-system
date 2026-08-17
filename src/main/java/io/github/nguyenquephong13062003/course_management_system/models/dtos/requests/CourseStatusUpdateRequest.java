package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for updating the status of a course.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseStatusUpdateRequest {

    /**
     * The status of the course.
     */
    @NotNull(message = "Status must not be null")
    private CourseStatus status;

}
