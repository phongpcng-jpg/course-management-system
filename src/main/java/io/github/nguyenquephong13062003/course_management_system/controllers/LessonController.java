package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdatePublishRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LessonUpdateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.ILessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling lesson-related API requests.
 */
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    /**
     * Service for handling lesson-related business logic.
     */
    private final ILessonService lessonService;

    /**
     * Endpoint to fetch the details of a published lesson by its ID.
     *
     * @param lessonId The ID of the lesson to fetch.
     * @return A ResponseEntity containing the lesson details wrapped in an ApiResponse.
     */
    @GetMapping("/{lesson_id}")
    public ResponseEntity<ApiResponse<LessonDetailResponse>> getPublishedLessonById(
            @PathVariable("lesson_id") Long lessonId
    ) {

        log.info(
                "Received request to get published lesson detail: lessonId={}",
                lessonId
        );

        LessonDetailResponse response =
                lessonService.getPublishedLessonById(lessonId);

        log.info(
                "Successfully fetched published lesson detail: lessonId={}",
                lessonId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Fetched published lesson details successfully",
                        response
                )
        );
    }

    /**
     * Endpoint to update the details of a lesson by its ID.
     *
     * @param lessonId The ID of the lesson to update.
     * @param request  The request body containing the updated lesson details.
     * @return A ResponseEntity containing the updated lesson details wrapped in an ApiResponse.
     */
    @PutMapping(
            value = "/{lesson_id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LessonDetailResponse>> updateLesson(
            @PathVariable("lesson_id") Long lessonId,
            @Valid @ModelAttribute LessonUpdateRequest request
    ) {
        log.info("Received request to update lesson. lessonId={}", lessonId);

        LessonDetailResponse response =
                lessonService.updateLesson(lessonId, request);

        log.info("Lesson updated successfully. lessonId={}", lessonId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Lesson updated successfully",
                        response
                ));
    }

    /**
     * Endpoint to update the publish status of a lesson by its ID.
     *
     * @param lessonId The ID of the lesson to update.
     * @param request  The request body containing the updated publish status.
     * @return A ResponseEntity containing the updated lesson details wrapped in an ApiResponse.
     */
    @PutMapping(
            value = "/{lesson_id}/publish"
    )
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LessonDetailResponse>> updateLessonPublish(
            @PathVariable("lesson_id") Long lessonId,
            @Valid @RequestBody LessonUpdatePublishRequest request
    ) {
        log.info("Received request to update lesson publish. lessonId={}", lessonId);

        LessonDetailResponse response =
                lessonService.updateLessonPublish(lessonId, request);

        log.info("Lesson publish updated successfully. lessonId={}", lessonId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Lesson publish updated successfully",
                        response
                ));
    }

}
