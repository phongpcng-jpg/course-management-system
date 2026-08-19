package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

/**
 * Response DTO containing preview information for a lesson's content.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonContentPreviewResponse {

    /**
     * The unique identifier of the lesson.
     */
    private Long lessonId;

    /**
     * The title of the lesson.
     */
    private String title;

    /**
     * The type of content being previewed.
     */
    private String contentType;

    /**
     * The Cloudinary URL used to preview the lesson video.
     */
    private String previewUrl;

    /**
     * The maximum duration of the preview in seconds.
     */
    private Long maxPreviewDurationSeconds;

    /**
     * Indicates whether preview content is available.
     */
    private Boolean previewAvailable;
}
