package io.github.nguyenquephong13062003.course_management_system.models.dtos.internals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO containing the number of completed published lessons for an enrollment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCompletedLessonCountResponse {

    /**
     * The unique identifier of the enrollment.
     */
    private Long enrollmentId;

    /**
     * The number of completed published lessons.
     */
    private Long completedLessonCount;
}