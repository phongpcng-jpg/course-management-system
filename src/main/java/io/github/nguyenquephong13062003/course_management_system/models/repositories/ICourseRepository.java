package io.github.nguyenquephong13062003.course_management_system.models.repositories;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * Repository interface for managing Course entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Course entities.
 */
public interface ICourseRepository extends JpaRepository<Course, Long> {

    /**
     * Retrieves a paginated list of CourseResponse objects based on the provided filtering and sorting criteria.
     *
     * @param keyword            The keyword to search for in course titles (optional).
     * @param status             The status of the courses to filter by (optional).
     * @param exceptStatus       The status of the courses to exclude from the results (optional).
     * @param teacherId          The ID of the teacher to filter courses by (optional).
     * @param priceMin           The minimum price of the courses to filter by (optional).
     * @param priceMax           The maximum price of the courses to filter by (optional).
     * @param durationHoursMin   The minimum duration in hours of the courses to filter by (optional).
     * @param durationHoursMax   The maximum duration in hours of the courses to filter by (optional).
     * @param pageable           The Pageable object containing pagination and sorting information.
     * @return A Page containing CourseResponse objects matching the specified criteria.
     */
    @Query("""
        SELECT new io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseResponse(
            c.id,
            c.title,
            c.description,
            new io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.CourseTeacherResponse(
                t.id,
                t.username,
                t.fullName
            ),
            c.price,
            c.durationHours,
            c.status,
            c.createdAt,
            c.updatedAt
        )
        FROM Course c
        JOIN c.teacher t
        WHERE (
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :status IS NULL
            OR c.status = :status
        )
        AND (
            :exceptStatus IS NULL
            OR c.status != :exceptStatus
        )
        AND (
            :teacherId IS NULL
            OR t.id = :teacherId
        )
        AND (
            :priceMin IS NULL
            OR c.price >= :priceMin
        )
        AND (
            :priceMax IS NULL
            OR c.price <= :priceMax
        )
        AND (
            :durationHoursMin IS NULL
            OR c.durationHours >= :durationHoursMin
        )
        AND (
            :durationHoursMax IS NULL
            OR c.durationHours <= :durationHoursMax
        )
    """)
    Page<CourseResponse> findAllCourseWithKeywordAndFilters(
            @Param("keyword") String keyword,
            @Param("status") CourseStatus status,
            @Param("exceptStatus") CourseStatus exceptStatus,
            @Param("teacherId") Long teacherId,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("durationHoursMin") Integer durationHoursMin,
            @Param("durationHoursMax") Integer durationHoursMax,
            Pageable pageable
    );

    boolean existsByTeacher_Id(Long teacherId);
}
