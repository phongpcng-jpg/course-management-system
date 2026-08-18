package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.DuplicateResourceException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.InvalidStateTransitionException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
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

/**
 * Implementation of the ILessonService interface for managing Lesson entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LessonServiceImpl implements ILessonService {

    /**
     * The repository for managing Lesson entities.
     */
    private final ILessonRepository lessonRepository;

    /**
     * The service for handling file uploads.
     */
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

        if (lesson.getCourse().getStatus() == CourseStatus.ARCHIVED) {
            throw new InvalidStateTransitionException("Cannot update lesson for a course that is archived");
        }

        if (
                !Objects.equals(request.getOrderIndex(), lesson.getOrderIndex()) &&
                lessonRepository.existsByCourse_IdAndOrderIndex(lesson.getCourse().getId(), request.getOrderIndex())
        ) {

            throw new DuplicateResourceException(
                    "Lesson with course_id '" + lesson.getCourse().getId() + "' and orderIndex '" + request.getOrderIndex() + "' already exists"
            );

        }

        lesson.setTitle(request.getTitle());
        if (request.getContent() != null && !request.getContent().isEmpty()) {
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

        if (lesson.getCourse().getStatus() == CourseStatus.ARCHIVED) {
            throw new InvalidStateTransitionException("Cannot update lesson for a course that is archived");
        }

        lesson.setIsPublished(request.getIsPublished());

        Lesson savedLesson = lessonRepository.save(lesson);

        return toLessonDetailResponse(savedLesson);

    }

    /**
     * Converts a Lesson entity to a LessonDetailResponse DTO.
     *
     * @param lesson the Lesson entity to convert
     * @return the corresponding LessonDetailResponse DTO
     */
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
