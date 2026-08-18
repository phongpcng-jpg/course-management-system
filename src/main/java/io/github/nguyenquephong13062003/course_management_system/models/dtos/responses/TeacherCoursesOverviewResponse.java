package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.util.List;

/**
 * Response DTO representing the overall courses statistics of a teacher.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCoursesOverviewResponse {

    /**
     * The unique identifier of the teacher.
     */
    private Long teacherId;

    /**
     * The full name of the teacher.
     */
    private String teacherName;

    /**
     * The username of the teacher.
     */
    private String teacherUsername;

    /**
     * The total number of courses managed by the teacher.
     */
    private Long totalCourses;

    /**
     * The number of draft courses.
     */
    private Long draftCourses;

    /**
     * The number of published courses.
     */
    private Long publishedCourses;

    /**
     * The number of archived courses.
     */
    private Long archivedCourses;

    /**
     * The total number of enrollments across all courses.
     */
    private Long totalEnrollments;

    /**
     * The total number of published lessons across all courses.
     */
    private Long totalPublishedLessons;

    /**
     * Detailed overview of each course managed by the teacher.
     */
    private List<TeacherCourseOverviewItem> courses;
}
