package io.github.nguyenquephong13062003.course_management_system.models.services.uploads;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.nguyenquephong13062003.course_management_system.exceptions.UploadCloudinaryException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.internals.CloudinaryUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service class for handling file uploads to Cloudinary.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    /**
     * Cloudinary instance for interacting with the Cloudinary API.
     */
    private final Cloudinary cloudinary;


    /**
     * Uploads a file to Cloudinary and returns the upload result.
     *
     * @param file the file to be uploaded
     * @return a CloudinaryUploadResult containing the URL and public ID of the uploaded file
     * @throws UploadCloudinaryException if an error occurs during the upload process
     */
    public CloudinaryUploadResult upload(MultipartFile file) {
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

            return CloudinaryUploadResult.builder()
                    .url(uploadResult.get("url").toString())
                    .publicId(uploadResult.get("publicId").toString())
                    .build();

        } catch (IOException e) {
            throw new UploadCloudinaryException(
                    "Error uploading file to Cloudinary"
            );
        }
    }

    public CloudinaryUploadResult uploadVideo(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();

            String publicId = originalFilename;

            if (publicId != null && publicId.contains(".")) {
                publicId = publicId.substring(
                        0,
                        publicId.lastIndexOf(".")
                );
            }

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "video",
                    "public_id", publicId
            );

            log.info(
                    "Uploading video to Cloudinary: filename={}, size={}",
                    originalFilename,
                    file.getSize()
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadParams
            );

            String url = uploadResult.get("secure_url") != null
                    ? uploadResult.get("secure_url").toString()
                    : uploadResult.get("url").toString();

            String uploadedPublicId = uploadResult.get("public_id").toString();

            log.info(
                    "Video uploaded successfully: publicId={}",
                    uploadedPublicId
            );

            return CloudinaryUploadResult.builder()
                    .url(url)
                    .publicId(uploadedPublicId)
                    .build();

        } catch (IOException e) {
            log.error("Error uploading video to Cloudinary", e);
            throw new UploadCloudinaryException(
                    "Error uploading video to Cloudinary"
            );
        }
    }

}
