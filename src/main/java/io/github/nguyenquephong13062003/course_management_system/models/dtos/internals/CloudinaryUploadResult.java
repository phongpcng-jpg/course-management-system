package io.github.nguyenquephong13062003.course_management_system.models.dtos.internals;

import lombok.*;

/**
 * Represents the result of a file upload to Cloudinary.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CloudinaryUploadResult {

    /**
     * The URL of the uploaded file on Cloudinary.
     */
    private String url;

    /**
     * The public ID of the uploaded file on Cloudinary.
     */
    private String publicId;
}
