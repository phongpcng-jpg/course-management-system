package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonCourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.ILessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LessonServiceImpl implements ILessonService {

    private final ILessonRepository lessonRepository;

    @Override
    public LessonDetailResponse getPublishedLessonById(Long lessonId) {

        Lesson lesson = lessonRepository.findBylessonIdAndIsPublishedTrue(lessonId)
                .orElseThrow(
                        () -> new NotFoundException("Published lesson with id " + lessonId + " not found")
                );

        return toLessonDetailResponse(lesson);
    }

    private LessonDetailResponse toLessonDetailResponse(Lesson lesson) {

        return LessonDetailResponse.builder()
                .id(lesson.getLessonId())
                .course(
                        LessonCourseResponse.builder()
                                .id(lesson.getCourse().getId())
                                .title(lesson.getCourse().getTitle())
                                .build()
                ).title(lesson.getTitle())
                .contentUrl(lesson.getContentUrl())
                .textContent(lesson.getTextContent())
                .orderIndex(lesson.getOrderIndex())
                .isPublished(lesson.getIsPublished())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .build();

    }

}
