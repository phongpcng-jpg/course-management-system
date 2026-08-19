package io.github.nguyenquephong13062003.course_management_system.models.dtos.internals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO containing the number of published lessons for a course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseLessonCountResponse {

    /**
     * The unique identifier of the course.
     */
    private Long courseId;

    /**
     * The number of published lessons in the course.
     */
    private Long publishedLessonCount;
}
