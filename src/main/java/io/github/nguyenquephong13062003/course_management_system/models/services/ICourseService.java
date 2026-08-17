package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;

import java.math.BigDecimal;

/**
 * The ICourseService interface defines the contract for course-related services in the course management system.
 * It provides methods for retrieving courses with various filtering and pagination options.
 */
public interface ICourseService {

    /**
     * Retrieves a paginated list of CourseResponse objects based on the provided filtering and sorting criteria.
     *
     * @param page               The page number to retrieve (0-based index).
     * @param size               The number of courses per page.
     * @param sortBy             The field to sort the courses by (e.g., "title", "price").
     * @param direction          The sorting direction ("asc" for ascending, "desc" for descending).
     * @param keyword            The keyword to search for in course titles (optional).
     * @param status             The status of the courses to filter by (optional).
     * @param teacherId          The ID of the teacher to filter courses by (optional).
     * @param priceMin           The minimum price of the courses to filter by (optional).
     * @param priceMax           The maximum price of the courses to filter by (optional).
     * @param durationHoursMin   The minimum duration in hours of the courses to filter by (optional).
     * @param durationHoursMax   The maximum duration in hours of the courses to filter by (optional).
     * @return A PageResponse containing a paginated list of CourseResponse objects matching the specified criteria.
     */
    PageResponse<CourseResponse> getAllCourse(
            int page,
            int size,
            String sortBy,
            String direction,
            String keyword,
            CourseStatus status,
            Long teacherId,
            BigDecimal priceMin,
            BigDecimal priceMax,
            Integer durationHoursMin,
            Integer durationHoursMax
    );
}
