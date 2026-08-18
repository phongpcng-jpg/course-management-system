package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

/**
 * Response DTO representing a course in the top courses report.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCourseReportResponse {

    /**
     * The unique identifier of the course.
     */
    private Long courseId;

    /**
     * The title of the course.
     */
    private String courseTitle;

    /**
     * The unique identifier of the teacher responsible for the course.
     */
    private Long teacherId;

    /**
     * The name of the teacher responsible for the course.
     */
    private String teacherName;

    /**
     * The total number of enrollments for the course.
     */
    private Long enrollmentCount;
}
