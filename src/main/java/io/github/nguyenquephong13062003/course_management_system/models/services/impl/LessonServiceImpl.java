package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.DuplicateResourceException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.InvalidStateTransitionException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.internals.CloudinaryUploadResult;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdatePublishRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonContentPreviewResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonCourseResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonProgressRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.ILessonRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.ILessonService;
import io.github.nguyenquephong13062003.course_management_system.models.services.deletes.CloudinaryFileService;
import io.github.nguyenquephong13062003.course_management_system.models.services.previews.CloudinaryPreviewService;
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

    /**
     * The service for handling file deletions from Cloudinary.
     */
    private final CloudinaryFileService cloudinaryFileService;

    /**
     * The repository for managing LessonProgress entities.
     */
    private final ILessonProgressRepository lessonProgressRepository;

    /**
     * Service responsible for generating Cloudinary content preview URLs.
     */
    private final CloudinaryPreviewService cloudinaryPreviewService;

    @Override
    public LessonDetailResponse getPublishedLessonById(Long lessonId) {

        Lesson lesson = lessonRepository.findByLessonIdAndIsPublishedTrue(lessonId)
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
                .orElseThrow(() -> new NotFoundException("Lesson with id " + id + " not found"));

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

        String contentPublicId = null;

        lesson.setTitle(request.getTitle());
        if (request.getContent() != null && !request.getContent().isEmpty()) {

            CloudinaryUploadResult cloudinaryUploadResult =
                    uploadService.upload(request.getContent());

            if (lesson.getContentPublicId() != null) {
                contentPublicId = lesson.getContentPublicId();
            }

            lesson.setContentUrl(cloudinaryUploadResult.getUrl());
            lesson.setContentPublicId(cloudinaryUploadResult.getPublicId());
        }
        if (request.getTextContent() != null) {
            lesson.setTextContent(request.getTextContent());
        }
        lesson.setOrderIndex(request.getOrderIndex());

        Lesson savedLesson = lessonRepository.save(lesson);

        if (contentPublicId != null) {
            cloudinaryFileService.delete(contentPublicId);
        }

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
                .orElseThrow(() -> new NotFoundException("Lesson with id " + id + " not found"));

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

    @Override
    @Transactional
    public void deleteLesson(Long lessonId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException(
                    "JWT verification failed: authentication is missing or unauthenticated"
            );
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Lesson with id " + lessonId + " not found"
                        )
                );

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        Objects.equals(authority.getAuthority(), "ROLE_ADMIN")
                );

        boolean isCourseTeacher = authentication.getName()
                .equals(lesson.getCourse().getTeacher().getUsername());

        if (!isAdmin && !isCourseTeacher) {

            throw new AccessDeniedException(
                    "Only Admin and Teacher who is in charge of the course can delete this lesson"
            );

        }

        if (Boolean.TRUE.equals(lesson.getIsPublished())) {
            throw new InvalidStateTransitionException(
                    "Cannot delete a published lesson"
            );
        }

        CourseStatus courseStatus = lesson.getCourse().getStatus();

        if (courseStatus == CourseStatus.PUBLISHED) {
            throw new InvalidStateTransitionException(
                    "Cannot delete a lesson from a published course"
            );
        }

        if (courseStatus == CourseStatus.ARCHIVED) {
            throw new InvalidStateTransitionException(
                    "Cannot delete a lesson from an archived course"
            );
        }

        String contentPublicId = lesson.getContentPublicId();

        long deletedProgressCount =
                lessonProgressRepository.deleteAllByLesson_LessonId(lessonId);

        log.debug(
                "Deleted {} lesson progress records for lesson '{}'",
                deletedProgressCount,
                lessonId
        );

        lessonRepository.delete(lesson);

        if (contentPublicId != null) {
            cloudinaryFileService.delete(contentPublicId);
        }

    }

    @Override
    public LessonContentPreviewResponse getContentPreview(Long lessonId) {

        log.debug(
                "Processing lesson content preview request: lessonId={}",
                lessonId
        );

        Lesson lesson = lessonRepository
                .findByLessonIdAndIsPublishedTrue(lessonId)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Published lesson with id " + lessonId + " not found"
                        )
                );

        if (lesson.getCourse().getStatus() != CourseStatus.PUBLISHED) {

            log.debug(
                    "Lesson preview rejected because course is not published: " +
                            "lessonId={}, courseId={}, courseStatus={}",
                    lessonId,
                    lesson.getCourse().getId(),
                    lesson.getCourse().getStatus()
            );

            throw new NotFoundException(
                    "Published lesson with id " + lessonId + " not found"
            );
        }

        String contentPublicId = lesson.getContentPublicId();

        if (contentPublicId == null || contentPublicId.isBlank()) {

            log.warn(
                    "Lesson has no Cloudinary content available for preview: lessonId={}",
                    lessonId
            );

            throw new NotFoundException(
                    "No preview content found for lesson with id " + lessonId
            );
        }

        String previewUrl =
                cloudinaryPreviewService.generateVideoPreviewUrl(
                        contentPublicId
                );

        log.debug(
                "Cloudinary preview URL generated successfully: lessonId={}",
                lessonId
        );

        return LessonContentPreviewResponse.builder()
                .lessonId(lesson.getLessonId())
                .title(lesson.getTitle())
                .contentType("VIDEO")
                .previewUrl(previewUrl)
                .maxPreviewDurationSeconds(
                        (long) cloudinaryPreviewService.getPreviewMaxPreviewDurationSeconds()
                )
                .previewAvailable(true)
                .build();
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
