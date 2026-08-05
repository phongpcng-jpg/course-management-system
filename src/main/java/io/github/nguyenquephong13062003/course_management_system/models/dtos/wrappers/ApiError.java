package io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * ApiError
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /**
     * The field that caused the error.
     */
    private String field;

    /**
     * The error message.
     */
    private String message;

}
