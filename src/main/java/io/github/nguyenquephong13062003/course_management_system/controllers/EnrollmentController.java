package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.EnrollmentCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.EnrollmentResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.IEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling enrollment-related API requests.
 */
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    /**
     * Service for handling enrollment-related business logic.
     */
    private final IEnrollmentService enrollmentService;

    /**
     * API 22:
     * Retrieves all enrollments belonging to the authenticated student.
     *
     * @return a ResponseEntity containing the student's enrollments
     */
    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments() {

        log.info("Received request to get enrollments of authenticated student");

        List<EnrollmentResponse> response =
                enrollmentService.getMyEnrollments();

        log.info(
                "Successfully fetched {} enrollments for authenticated student",
                response.size()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Fetched enrollments successfully",
                        response
                )
        );
    }

    /**
     * API 23:
     * Enrolls the authenticated student in a published course.
     *
     * @param request the enrollment creation request
     * @return a ResponseEntity containing the created enrollment
     */
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollCourse(
            @Valid @RequestBody EnrollmentCreateRequest request
    ) {

        log.info(
                "Received course enrollment request: courseId={}",
                request.getCourseId()
        );

        EnrollmentResponse response =
                enrollmentService.enrollCourse(request);

        log.info(
                "Course enrollment created successfully: enrollmentId={}, courseId={}",
                response.getEnrollmentId(),
                response.getCourseId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                HttpStatus.CREATED.value(),
                                "Enrolled in course successfully",
                                response
                        )
                );
    }

    /**
     * API 24:
     * Retrieves detailed information about an enrollment
     * belonging to the authenticated student.
     *
     * @param enrollmentId the ID of the enrollment
     * @return a ResponseEntity containing detailed enrollment information
     */
    @GetMapping("/{enrollment_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentDetailResponse>> getEnrollmentDetail(
            @PathVariable("enrollment_id") Long enrollmentId
    ) {

        log.info(
                "Received request to get enrollment detail: enrollmentId={}",
                enrollmentId
        );

        EnrollmentDetailResponse response =
                enrollmentService.getEnrollmentDetail(enrollmentId);

        log.info(
                "Successfully fetched enrollment detail: enrollmentId={}",
                enrollmentId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Fetched enrollment details successfully",
                        response
                )
        );
    }

    /**
     * API 25:
     * Marks a published lesson as completed for an enrollment
     * belonging to the authenticated student.
     *
     * @param enrollmentId the ID of the enrollment
     * @param lessonId     the ID of the lesson to complete
     * @return a ResponseEntity containing the updated enrollment detail
     */
    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentDetailResponse>> completeLesson(
            @PathVariable("enrollment_id") Long enrollmentId,
            @PathVariable("lesson_id") Long lessonId
    ) {

        log.info(
                "Received lesson completion request: enrollmentId={}, lessonId={}",
                enrollmentId,
                lessonId
        );

        EnrollmentDetailResponse response =
                enrollmentService.completeLesson(
                        enrollmentId,
                        lessonId
                );

        log.info(
                "Lesson completed successfully: enrollmentId={}, lessonId={}, progress={}",
                enrollmentId,
                lessonId,
                response.getProgressPercentage()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Lesson completed successfully",
                        response
                )
        );
    }
}
