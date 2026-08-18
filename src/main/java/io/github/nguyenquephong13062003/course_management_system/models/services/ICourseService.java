package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseStatusUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.CourseUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * Retrieves detailed information about a specific course by its ID.
     *
     * @param id The ID of the course to retrieve details for.
     * @return A CourseDetailResponse containing detailed information about the specified course.
     */
    CourseDetailResponse getCourseDetail(Long id);

    /**
     * Creates a new course based on the provided CourseCreateRequest.
     *
     * @param request The CourseCreateRequest containing the details of the course to be created.
     * @return A CourseResponse representing the newly created course.
     */
    CourseResponse createCourse(CourseCreateRequest request);

    /**
     * Updates an existing course identified by its ID based on the provided CourseUpdateRequest.
     *
     * @param courseId The ID of the course to be updated.
     * @param request  The CourseUpdateRequest containing the updated details of the course.
     * @return A CourseResponse representing the updated course.
     */
    CourseResponse updateCourse(Long courseId, CourseUpdateRequest request);

    /**
     * Updates the status of an existing course identified by its ID based on the provided CourseStatusUpdateRequest.
     *
     * @param courseId The ID of the course whose status is to be updated.
     * @param request  The CourseStatusUpdateRequest containing the new status for the course.
     * @return A CourseResponse representing the course with the updated status.
     */
    CourseResponse updateCourseStatus(
            Long courseId,
            CourseStatusUpdateRequest request
    );

    /**
     * Deletes an existing course identified by its ID.
     *
     * @param courseId The ID of the course to be deleted.
     */
    void deleteCourse(Long courseId);

    /**
     * Retrieves a list of published lessons associated with a specific course by its ID.
     *
     * @param id The ID of the course for which to retrieve published lessons.
     * @return A list of LessonResponse objects representing the published lessons of the specified course.
     */
    List<LessonResponse> getAllPublishedLessonByCourseId(Long id);

    /**
     * Creates a new lesson associated with a specific course identified by its ID based on the provided LessonCreateRequest.
     *
     * @param course_id The ID of the course to which the new lesson will be associated.
     * @param request   The LessonCreateRequest containing the details of the lesson to be created.
     * @return A LessonResponse representing the newly created lesson.
     */
    LessonResponse createLesson(Long course_id, LessonCreateRequest request);

}
