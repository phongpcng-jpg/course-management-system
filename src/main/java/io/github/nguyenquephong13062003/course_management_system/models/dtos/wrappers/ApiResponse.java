package io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * ApiResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {

    /**
     * Whether the request was successful.
     */
    private boolean success;

    /**
     * The status code returned in the response.
     */
    @JsonProperty("status_code")
    private Integer statusCode;

    /**
     * The error code returned in the response.
     */
    @JsonProperty("error_code")
    private String errorCode;

    /**
     * The message returned in the response.
     */
    private String message;

    /**
     * The data returned in the response.
     */
    private T data;

    /**
     * The list of errors returned in the response.
     */
    private List<ApiError> errors;

    /**
     * Timestamp indicating when the response was generated.
     *
     * <p>
     * Defaults to the current UTC time.
     * </p>
     */
    @Builder.Default
    private Instant timestamp = Instant.now();

    /**
     * Creates a successful ApiResponse with the given status code, message, and data.
     * @param <T> the type of the data being returned
     * @param statusCode the HTTP status code of the response
     * @param message a message describing the response
     * @param data the data being returned in the response
     * @return an ApiResponse object representing a successful response
     */
    public static <T> ApiResponse<T> success(Integer statusCode, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates an error ApiResponse with the given status code, error code, message, and list of errors.
     * @param <T> the type of the data being returned (null in this case)
     * @param statusCode the HTTP status code of the response
     * @param errorCode a code representing the type of error that occurred
     * @param message a message describing the error
     * @param errors a list of ApiError objects representing the specific errors that occurred
     * @return an ApiResponse object representing an error response
     */
    public static <T> ApiResponse<T> error(Integer statusCode, String errorCode, String message, List<ApiError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .errorCode(errorCode)
                .message(message)
                .errors(errors)
                .build();
    }

}
