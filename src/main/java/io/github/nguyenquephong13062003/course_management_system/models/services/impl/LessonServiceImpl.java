package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdatePublishRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonCourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.ILessonService;
import io.github.nguyenquephong13062003.course_management_system.models.services.uploads.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LessonServiceImpl implements ILessonService {

    private final ILessonRepository lessonRepository;

    private final UploadService uploadService;

    @Override
    public LessonDetailResponse getPublishedLessonById(Long lessonId) {

        Lesson lesson = lessonRepository.findBylessonIdAndIsPublishedTrue(lessonId)
                .orElseThrow(
                        () -> new NotFoundException("Published lesson with id " + lessonId + " not found")
                );

        return toLessonDetailResponse(lesson);
    }

    @Override
    @Transactional
    public LessonDetailResponse updateLesson(Long id, LessonUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("Lesson with id " + id + " not found");
                });

        if (!authentication.getName().equals(lesson.getCourse().getTeacher().getUsername()) && authentication.getAuthorities().stream()
                .noneMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"))) {

            throw new AccessDeniedException("Only Admin and Teacher who is in charge of the course can create lesson of course");

        }

        lesson.setTitle(request.getTitle());
        if (request.getContent() != null) {
            lesson.setContentUrl(
                    uploadService.upload(
                            request.getContent()
                    )
            );
        }
        if (request.getTextContent() != null) {
            lesson.setTextContent(request.getTextContent());
        }
        lesson.setOrderIndex(request.getOrderIndex());

        Lesson savedLesson = lessonRepository.save(lesson);

        return toLessonDetailResponse(savedLesson);

    }

    @Override
    @Transactional
    public LessonDetailResponse updateLessonPublish(Long id, LessonUpdatePublishRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException("Lesson with id " + id + " not found");
                });

        if (!authentication.getName().equals(lesson.getCourse().getTeacher().getUsername()) && authentication.getAuthorities().stream()
                .noneMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"))) {

            throw new AccessDeniedException("Only Admin and Teacher who is in charge of the course can create lesson of course");

        }

        lesson.setIsPublished(request.getIsPublished());

        Lesson savedLesson = lessonRepository.save(lesson);

        return toLessonDetailResponse(savedLesson);

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
