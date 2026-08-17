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
public class LessonUpdateRequest {
    @NotBlank(message = "Lesson title must not be blank")
    @Size(max = 255, message = "Lesson title must not exceed 255 characters")
    @Schema(
            description = "Updated title of the lesson",
            example = "Introduction to Spring Boot - Updated"
    )
    private String title;

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
            description = "New lesson video file. Leave empty to keep the current video.",
            type = "string",
            format = "binary"
    )
    private MultipartFile content;

    @Size(
            max = 100000,
            message = "Text content must not exceed 100000 characters"
    )
    @Schema(
            description = "Updated text content of the lesson",
            example = "Updated lesson content."
    )
    private String textContent;

    @NotNull(message = "Order index must not be null")
    @Positive(message = "Order index must be greater than 0")
    @Schema(
            description = "Updated display order of the lesson",
            example = "2"
    )
    private Integer orderIndex;

}
