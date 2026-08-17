package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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

            @RequestParam(name = "keyword", required = false)
            String keyword,

            // Note: Status is All/null if hasRole('ADMIN')
            // Else PUBLISHED and DRAFT/ARCHIVED is locked
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
                page, size, sortBy, direction, keyword, status, teacherId, priceMin, priceMax, durationHoursMin, durationHoursMax);

        PageResponse<CourseResponse> response = courseService.getAllCourse(
                page, size, sortBy, direction, keyword, status, teacherId,
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

}
