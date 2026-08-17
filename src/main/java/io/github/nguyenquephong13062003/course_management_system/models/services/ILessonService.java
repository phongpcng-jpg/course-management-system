package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;

public interface ILessonService {

    LessonDetailResponse getPublishedLessonById(Long lessonId);
}
