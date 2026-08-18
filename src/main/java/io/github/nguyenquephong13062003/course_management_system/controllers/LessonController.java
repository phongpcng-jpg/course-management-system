package io.github.nguyenquephong13062003.course_management_system.controllers;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LessonDetailResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.ApiResponse;
import io.github.nguyenquephong13062003.course_management_system.models.services.ILessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
