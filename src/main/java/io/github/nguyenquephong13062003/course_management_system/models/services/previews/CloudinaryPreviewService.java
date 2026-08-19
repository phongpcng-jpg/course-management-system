package io.github.nguyenquephong13062003.course_management_system.models.services.previews;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating preview URLs for video resources
 * stored on Cloudinary.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryPreviewService {

    /**
     * Maximum duration of a lesson video preview in seconds.
     */
    private static final int PREVIEW_DURATION_SECONDS = 30;

    /**
     * Cloudinary instance for generating delivery URLs.
     */
    private final Cloudinary cloudinary;

    /**
     * Generates a 30-second preview URL for a video stored on Cloudinary.
     *
     * @param publicId the Cloudinary public ID of the video
     * @return the transformed Cloudinary video delivery URL
     */
    public String generateVideoPreviewUrl(String publicId) {

        if (publicId == null || publicId.isBlank()) {
            throw new IllegalArgumentException(
                    "Cloudinary public ID must not be null or blank"
            );
        }

        log.debug(
                "Generating Cloudinary video preview URL for publicId='{}'",
                publicId
        );

        return cloudinary.url()
                .resourceType("video")
                .transformation(
                        new Transformation()
                                .startOffset(0)
                                .duration(PREVIEW_DURATION_SECONDS)
                )
                .secure(true)
                .generate(publicId);
    }

    /**
     * Returns the configured preview duration.
     *
     * @return preview duration in seconds
     */
    public int getPreviewMaxPreviewDurationSeconds() {
        return PREVIEW_DURATION_SECONDS;
    }
}
