package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 
 * LessonDetailResponse
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetailResponse {

    /**
     * The id of the lesson
     */
    private Long id;

    /**
     * The course to which the lesson belongs
     */
    private LessonCourseResponse course;

    /**
     * The title of the lesson
     */
    private String title;

    /**
     * The URL of the lesson content
     */
    private String contentUrl;

    /**
     * The text content of the lesson
     */
    private String textContent;

    /**
     * The display order of the lesson
     */
    private Integer orderIndex;

    /**
     * Indicates whether the lesson is published and visible to students
     */
    private Boolean isPublished;

    /**
     * The date and time when the lesson was created
     */
    private LocalDateTime createdAt;

    /**
     * The date and time when the lesson was last updated
     */
    private LocalDateTime updatedAt;

}
