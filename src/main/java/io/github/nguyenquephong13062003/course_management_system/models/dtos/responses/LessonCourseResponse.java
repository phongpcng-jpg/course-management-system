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

    private Long id;

    private String title;

}
