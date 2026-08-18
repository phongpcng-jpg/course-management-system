package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseStatusUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * The CourseController class handles HTTP requests related to courses in the course management system.
 * It provides endpoints for retrieving paginated lists of courses with various filtering and sorting options.
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    /**
     * The ICourseService instance used for course-related operations.
     */
    private final ICourseService courseService;

    /**
     * Handles GET requests to retrieve a paginated list of courses based on the provided filtering and sorting criteria.
     *
     * @param page               The page number to retrieve (0-based index).
     * @param size               The number of courses per page.
     * @param sortBy             The field to sort the courses by (e.g., "title", "price").
     * @param direction          The sorting direction ("asc" for ascending, "desc" for descending).
     * @param search            The keyword to search for in course titles (optional).
     * @param status             The status of the courses to filter by (optional).
     * @param teacherId          The ID of the teacher to filter courses by (optional).
     * @param priceMin           The minimum price of the courses to filter by (optional).
     * @param priceMax           The maximum price of the courses to filter by (optional).
     * @param durationHoursMin   The minimum duration in hours of the courses to filter by (optional).
     * @param durationHoursMax   The maximum duration in hours of the courses to filter by (optional).
     * @return A ResponseEntity containing an ApiResponse with a PageResponse of CourseResponse objects matching the specified criteria.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getPagedCourse(

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "10")
            int size,

            @RequestParam(name = "sortBy", required = false)
            String sortBy,

            @RequestParam(name = "direction", required = false)
            String direction,

            @RequestParam(name = "search", required = false)
            String search,

            // Note: Status is All/null if hasRole('ADMIN')
            // Else PUBLISHED+ARCHIVED and DRAFT is locked
            @RequestParam(name = "status", required = false)
            CourseStatus status,

            @RequestParam(name = "teacherId", required = false)
            Long teacherId,

            @RequestParam(name = "priceMin", required = false)
            BigDecimal priceMin,

            @RequestParam(name = "priceMax", required = false)
            BigDecimal priceMax,

            @RequestParam(name = "durationHoursMin", required = false)
            Integer durationHoursMin,

            @RequestParam(name = "durationHoursMax", required = false)
            Integer durationHoursMax

    ) {

        log.info("Received request to get paginated courses with parameters: page={}, size={}, sortBy={}, direction={}, keyword={}, status={}, teacherId={}, priceMin={}, priceMax={}, durationHoursMin={}, durationHoursMax={}",
                page, size, sortBy, direction, search, status, teacherId, priceMin, priceMax, durationHoursMin, durationHoursMax);

        PageResponse<CourseResponse> response = courseService.getAllCourse(
                page, size, sortBy, direction, search, status, teacherId,
                priceMin, priceMax, durationHoursMin, durationHoursMax
        );

        log.info("Successfully fetched paginated courses. Total courses found: {}", response.getTotalItems());

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Fetched paginated courses successfully",
                        response
                )
        );

    }

    /**
     * Handles GET requests to retrieve detailed information about a specific course by its ID.
     *
     * @param id The ID of the course to retrieve details for.
     * @return A ResponseEntity containing an ApiResponse with a CourseDetailResponse object representing the course details.
     */
    @GetMapping("/{course_id}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseDetail(
            @PathVariable("course_id") Long id
    ) {

        log.info("Received request to get course detail for course ID: {}", id);

        CourseDetailResponse response = courseService.getCourseDetail(id);

        log.info("Successfully fetched course detail for course ID: {}", id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Fetched course details successfully",
                        response
                )
        );
    }

    /**
     * Handles POST requests to create a new course.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param request The CourseCreateRequest object containing the details of the course to be created.
     * @return A ResponseEntity containing an ApiResponse with a CourseResponse object representing the newly created course.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseCreateRequest request
    ) {
        log.info(
                "Received create course request: title={}, teacherId={}",
                request.getTitle(),
                request.getTeacherId()
        );

        CourseResponse response = courseService.createCourse(request);

        log.info(
                "Course created successfully: courseId={}",
                response.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Course created successfully",
                        response
                )
        );
    }

    /**
     * Handles PUT requests to update an existing course.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param courseId The ID of the course to be updated.
     * @param request  The CourseUpdateRequest object containing the updated details of the course.
     * @return A ResponseEntity containing an ApiResponse with a CourseResponse object representing the updated course.
     */
    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        log.info(
                "Received update course request: courseId={}, teacherId={}",
                courseId,
                request.getTeacherId()
        );

        CourseResponse response = courseService.updateCourse(
                courseId,
                request
        );

        log.info(
                "Course updated successfully: courseId={}",
                courseId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Course updated successfully",
                        response
                )
        );

    }

    /**
     * Handles PUT requests to update the status of an existing course.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param courseId The ID of the course to be updated.
     * @param request  The CourseStatusUpdateRequest object containing the new status of the course.
     * @return A ResponseEntity containing an ApiResponse with a CourseResponse object representing the updated course status.
     */
    @PutMapping("/{courseId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourseStatus(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseStatusUpdateRequest request
    ) {
        log.info(
                "Received update course status request: courseId={}, status={}",
                courseId,
                request.getStatus()
        );

        CourseResponse response = courseService.updateCourseStatus(
                courseId,
                request
        );

        log.info(
                "Course status updated successfully: courseId={}, status={}",
                courseId,
                response.getStatus()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Course status updated successfully",
                        response
                )
        );

    }

    /**
     * Handles DELETE requests to remove an existing course.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param courseId The ID of the course to be deleted.
     * @return A ResponseEntity containing an ApiResponse indicating the success of the deletion operation.
     */
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long courseId
    ) {
        log.info(
                "Received delete course request: courseId={}",
                courseId
        );

        courseService.deleteCourse(courseId);

        log.info(
                "Course deleted successfully: courseId={}",
                courseId
        );

        return  ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Course deleted successfully",
                        null
                )
        );

    }

    /**
     * Handles GET requests to retrieve all published lessons for a specific course.
     *
     * @param courseId The ID of the course for which to retrieve published lessons.
     * @return A ResponseEntity containing an ApiResponse with a list of LessonResponse objects representing the published lessons of the specified course.
     */
    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getAllPublishedLessons(
            @PathVariable Long courseId
    ) {
        log.info("Received request to get published lessons for courseId={}", courseId);

        List<LessonResponse> lessons =
                courseService.getAllPublishedLessonByCourseId(courseId);

        log.info(
                "Successfully retrieved {} published lessons for courseId={}",
                lessons.size(),
                courseId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Successfully retrieved published lessons",
                        lessons
                )
        );
    }

    /**
     * Handles POST requests to create a new lesson for a specific course.
     * This endpoint is restricted to users with the 'ADMIN' role.
     *
     * @param courseId The ID of the course for which to create the lesson.
     * @param request  The LessonCreateRequest object containing the details of the lesson to be created.
     * @return A ResponseEntity containing an ApiResponse with a LessonResponse object representing the newly created lesson.
     */
    @PostMapping(
            value = "/{course_id}/lessons",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @PathVariable("course_id") Long courseId,
            @Valid @ModelAttribute LessonCreateRequest request
    ) {
        log.info("Received request to create lesson. courseId={}", courseId);

        LessonResponse response =
                courseService.createLesson(courseId, request);

        log.info("Lesson created successfully. courseId={}", courseId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Lesson created successfully",
                        response
                ));
    }

}
