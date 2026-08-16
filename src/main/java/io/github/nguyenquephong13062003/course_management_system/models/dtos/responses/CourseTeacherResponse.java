package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

/**
 * Represents the response DTO for a course's teacher.
 * This class is used to transfer teacher data in API responses related to courses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseTeacherResponse {

    /**
     * The unique identifier for the teacher.
     */
    private Long id;

    /**
     * The username of the teacher.
     */
    private String username;

    /**
     * The full name of the teacher.
     */
    private String fullName;
}
