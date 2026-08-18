package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO representing the overall learning progress of a student.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProgressReportResponse {

    /**
     * The unique identifier of the student.
     */
    private Long studentId;

    /**
     * The full name of the student.
     */
    private String studentName;

    /**
     * The username of the student.
     */
    private String studentUsername;

    /**
     * The total number of courses the student has enrolled in.
     */
    private Long totalCourses;

    /**
     * The number of courses completed by the student.
     */
    private Long completedCourses;

    /**
     * The number of courses the student is currently enrolled in.
     */
    private Long enrolledCourses;

    /**
     * The overall learning progress percentage across all published lessons.
     */
    private BigDecimal overallProgressPercentage;

    /**
     * The detailed progress of the student for each enrolled course.
     */
    private List<StudentCourseProgressResponse> courses;
}
