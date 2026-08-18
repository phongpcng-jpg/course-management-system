package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.StudentProgressReportResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.TeacherCoursesOverviewResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.TopCourseReportResponse;

import java.util.List;

/**
 * Service interface for generating administrative reports and analytics.
 */
public interface IReportService {

    /**
     * Retrieves the most popular courses based on the total number of enrollments.
     *
     * @param limit the maximum number of courses to return
     * @return a list of top courses ordered by enrollment count descending
     */
    List<TopCourseReportResponse> getTopCourses(int limit);

    /**
     * Retrieves the learning progress report of a specific student.
     *
     * @param studentId the unique identifier of the student
     * @return the student's overall progress report
     */
    StudentProgressReportResponse getStudentProgress(Long studentId);

    /**
     * Retrieves an overview of all courses managed by a specific teacher.
     *
     * @param teacherId the unique identifier of the teacher
     * @return the teacher's courses overview report
     */
    TeacherCoursesOverviewResponse getTeacherCoursesOverview(Long teacherId);
}