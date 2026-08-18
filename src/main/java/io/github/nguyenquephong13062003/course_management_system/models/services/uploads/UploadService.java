package io.github.nguyenquephong13062003.course_management_system.models.services.uploads;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.nguyenquephong13062003.course_management_system.exceptions.UploadCloudinaryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service class for handling file uploads to Cloudinary.
 */
@Service
@RequiredArgsConstructor
public class UploadService {

    /**
     * Cloudinary instance for interacting with the Cloudinary API.
     */
    private final Cloudinary cloudinary;

    /**
     * Uploads a file to Cloudinary and returns the URL of the uploaded file.
     *
     * @param file The file to be uploaded.
     * @return The URL of the uploaded file.
     * @throws UploadCloudinaryException If an error occurs during the upload process.
     */
    public String upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();

            if(originalFilename != null && originalFilename.contains(".")) {
                originalFilename = originalFilename.substring(
                        0,
                        originalFilename.lastIndexOf(".")
                );
            }

            Map<?, ?> uploadParams = ObjectUtils.asMap(
                    "public_id",originalFilename
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),uploadParams
            );

            return uploadResult.get("url").toString();

        } catch (IOException e) {
            throw new UploadCloudinaryException(
                    "Error uploading file to Cloudinary"
            );
        }
    }

}
