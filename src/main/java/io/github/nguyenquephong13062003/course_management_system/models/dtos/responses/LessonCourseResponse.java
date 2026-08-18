package io.github.nguyenquephong13062003.course_management_system.models.dtos.responses;

import lombok.*;

/**
 * 
 * LessonCourseResponse
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonCourseResponse {

    /**
     * The id of the lesson
     */
    private Long id;

    /**
     * The title of the lesson
     */
    private String title;

}
