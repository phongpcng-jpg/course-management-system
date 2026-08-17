package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import io.github.nguyenquephong13062003.course_management_system.utils.validations.annotations.FileExtension;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LessonCreateRequest {

    @NotBlank(message = "Lesson title must not be blank")
    @Size(max = 255, message = "Lesson title must not exceed 255 characters")
    @Schema(
            description = "Title of the lesson",
            example = "Introduction to Spring Boot"
    )
    private String title;

    @NotNull(message = "Lesson video must not be null")
    @FileExtension(
            allowedExtensions = {
                    ".mp4",
                    ".mov",
                    ".avi",
                    ".mkv",
                    ".webm"
            },
            message = "Lesson content must be a valid video file"
    )
    @Schema(
            description = "Lesson video file",
            type = "string",
            format = "binary"
    )
    private MultipartFile contentUrl;

    @Size(
            max = 100000,
            message = "Text content must not exceed 100000 characters"
    )
    @Schema(
            description = "Text content of the lesson",
            example = "In this lesson, we will learn the fundamentals of Spring Boot."
    )
    private String textContent;

    @NotNull(message = "Order index must not be null")
    @Positive(message = "Order index must be greater than 0")
    @Schema(
            description = "Display order of the lesson within the course",
            example = "1"
    )
    private Integer orderIndex;

}
