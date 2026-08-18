package io.github.nguyenquephong13062003.course_management_system.models.services.deletes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.nguyenquephong13062003.course_management_system.exceptions.DeleteCloudinaryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service responsible for managing file resources stored on Cloudinary.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryFileService {

    /**
     * Cloudinary instance for interacting with Cloudinary API.
     */
    private final Cloudinary cloudinary;

    /**
     * Deletes a video resource from Cloudinary using its public ID.
     *
     * @param publicId the public ID of the Cloudinary resource
     * @throws DeleteCloudinaryException if the resource cannot be deleted
     */
    public void delete(String publicId) {

        if (publicId == null || publicId.isBlank()) {
            log.warn(
                    "Skip deleting Cloudinary resource because publicId is null or blank"
            );
            return;
        }

        try {
            Map<?, ?> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type", "video"
                    )
            );

            String deletionResult = String.valueOf(result.get("result"));

            if (!"ok".equalsIgnoreCase(deletionResult)) {
                log.warn(
                        "Failed to delete Cloudinary resource. publicId='{}', result='{}'",
                        publicId,
                        deletionResult
                );

                throw new DeleteCloudinaryException(
                        "Unable to delete file from Cloudinary"
                );
            }

            log.info(
                    "Cloudinary resource '{}' deleted successfully",
                    publicId
            );

        } catch (DeleteCloudinaryException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error deleting Cloudinary resource '{}'",
                    publicId,
                    e
            );

            throw new DeleteCloudinaryException(
                    "Error deleting file from Cloudinary",
                    e
            );
        }
    }
    
}
