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

    private Long id;

    private LessonCourseResponse course;

    private String title;

    private String contentUrl;

    private String textContent;

    private Integer orderIndex;

    private Boolean isPublished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
