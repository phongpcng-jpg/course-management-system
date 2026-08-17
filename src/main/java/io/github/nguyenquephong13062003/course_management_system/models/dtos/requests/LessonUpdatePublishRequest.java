package io.github.nguyenquephong13062003.course_management_system.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LessonUpdatePublishRequest {

    @NotNull(message = "Published status must not be null")
    @Schema(
            description = "Whether the lesson is published and visible to students",
            example = "true"
    )
    private Boolean isPublished;

}
