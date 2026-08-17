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

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final ILessonService lessonService;

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
