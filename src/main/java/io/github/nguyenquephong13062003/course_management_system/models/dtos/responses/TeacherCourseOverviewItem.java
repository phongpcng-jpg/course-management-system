package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import lombok.*;

/**
 * Response DTO representing an individual course in a teacher courses overview report.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseOverviewItem {

    /**
     * The unique identifier of the course.
     */
    private Long courseId;

    /**
     * The title of the course.
     */
    private String title;

    /**
     * The current status of the course.
     */
    private CourseStatus status;

    /**
     * The total number of enrollments for the course.
     */
    private Long enrollmentCount;

    /**
     * The total number of published lessons in the course.
     */
    private Long publishedLessonCount;

    /**
     * Constructor used by the teacher courses overview query.
     */
    public TeacherCourseOverviewItem(
            Long courseId,
            String title,
            CourseStatus status,
            Long enrollmentCount
    ) {
        this.courseId = courseId;
        this.title = title;
        this.status = status;
        this.enrollmentCount = enrollmentCount;
        this.publishedLessonCount = 0L;
    }
}
