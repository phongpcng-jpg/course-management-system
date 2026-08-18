package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO representing a student's progress for a lesson.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgressResponse {

    /**
     * The unique identifier of the lesson.
     */
    private Long lessonId;

    /**
     * The title of the lesson.
     */
    private String lessonTitle;

    /**
     * The order of the lesson within the course.
     */
    private Integer orderIndex;

    /**
     * Indicates whether the lesson has been completed.
     */
    private boolean completed;

    /**
     * The date and time when the lesson was completed.
     */
    private LocalDateTime completedAt;
}
