package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.StudentProgressReportResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.TeacherCoursesOverviewResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.TopCourseReportResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IReportService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling administrative report and analytics requests.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ReportController {

    /**
     * Service for report-related business operations.
     */
    private final IReportService reportService;

    /**
     * Retrieves the most popular courses based on enrollment count.
     *
     * @param limit maximum number of courses to return
     * @return top courses report
     */
    @GetMapping("/top_courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TopCourseReportResponse>>> getTopCourses(
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Limit must be at least 1")
            @Max(value = 100, message = "Limit must not exceed 100")
            int limit
    ) {

        log.info(
                "Received request to retrieve top courses report. limit={}",
                limit
        );

        List<TopCourseReportResponse> data =
                reportService.getTopCourses(limit);

        log.info(
                "Successfully retrieved top courses report. count={}",
                data.size()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Top courses retrieved successfully",
                        data
                )
        );
    }

    /**
     * Retrieves the learning progress report of a specific student.
     *
     * @param studentId the student identifier
     * @return student progress report
     */
    @GetMapping("/student_progress/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProgressReportResponse>> getStudentProgress(
            @PathVariable Long studentId
    ) {

        log.info(
                "Received request to retrieve student progress report. studentId={}",
                studentId
        );

        StudentProgressReportResponse data =
                reportService.getStudentProgress(studentId);

        log.info(
                "Successfully retrieved student progress report. studentId={}",
                studentId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Student progress report retrieved successfully",
                        data
                )
        );
    }

    /**
     * Retrieves an overview of all courses managed by a specific teacher.
     *
     * @param teacherId the teacher identifier
     * @return teacher courses overview report
     */
    @GetMapping("/teacher_courses_overview/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherCoursesOverviewResponse>> getTeacherCoursesOverview(
            @PathVariable Long teacherId
    ) {

        log.info(
                "Received request to retrieve teacher courses overview. teacherId={}",
                teacherId
        );

        TeacherCoursesOverviewResponse data =
                reportService.getTeacherCoursesOverview(teacherId);

        log.info(
                "Successfully retrieved teacher courses overview. teacherId={}",
                teacherId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Teacher courses overview retrieved successfully",
                        data
                )
        );
    }
}
