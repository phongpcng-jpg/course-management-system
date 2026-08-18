package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdatePublishRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;

/**
 * The ILessonService interface defines the contract for lesson-related services in the course management system.
 * It provides methods for retrieving and updating lesson details.
 */
public interface ILessonService {

    /**
     * Retrieves detailed information about a specific published lesson by its ID.
     *
     * @param lessonId The ID of the published lesson to retrieve details for.
     * @return A LessonDetailResponse containing detailed information about the specified published lesson.
     */
    LessonDetailResponse getPublishedLessonById(Long lessonId);

    /**
     * Updates the details of a specific lesson identified by its ID based on the provided LessonUpdateRequest.
     *
     * @param id      The ID of the lesson to be updated.
     * @param request The LessonUpdateRequest containing the updated details of the lesson.
     * @return A LessonDetailResponse representing the updated lesson.
     */
    LessonDetailResponse updateLesson(Long id, LessonUpdateRequest request);

    /**
     * Updates the publish status of a specific lesson identified by its ID based on the provided LessonUpdatePublishRequest.
     *
     * @param id      The ID of the lesson to be updated.
     * @param request The LessonUpdatePublishRequest containing the updated publish status of the lesson.
     * @return A LessonDetailResponse representing the updated lesson with the new publish status.
     */
    LessonDetailResponse updateLessonPublish(Long id, LessonUpdatePublishRequest request);

    /**
     * Deletes a specific lesson identified by its ID.
     *
     * @param lessonId The ID of the lesson to be deleted.
     */
    void deleteLesson(Long lessonId);

}
