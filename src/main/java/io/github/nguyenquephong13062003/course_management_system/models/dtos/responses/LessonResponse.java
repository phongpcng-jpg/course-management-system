package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

/**
 * The LessonResponse class represents the response DTO (Data Transfer Object) for a lesson in the course management system.
 * It is used to transfer lesson data from the backend to the frontend, providing relevant information about individual lessons.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {

    /**
     * The unique identifier for the lesson.
     */
    private Long id;

    /**
     * The title of the lesson.
     */
    private String title;

    /**
     * The URL of the lesson content.
     */
    private String contentUrl;

    /**
     * The text content of the lesson.
     */
    private String textContent;

    /**
     * The order index of the lesson within the course.
     */
    private Integer orderIndex;

    /**
     * Indicates whether the lesson is published or not.
     */
    private Boolean isPublished;

    /**
     * The timestamp when the lesson was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the lesson was last updated.
     */
    private LocalDateTime updatedAt;

}
