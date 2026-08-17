package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdatePublishRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;

public interface ILessonService {

    LessonDetailResponse getPublishedLessonById(Long lessonId);

    LessonDetailResponse updateLesson(Long id, LessonUpdateRequest request);

    LessonDetailResponse updateLessonPublish(Long id, LessonUpdatePublishRequest request);

}
