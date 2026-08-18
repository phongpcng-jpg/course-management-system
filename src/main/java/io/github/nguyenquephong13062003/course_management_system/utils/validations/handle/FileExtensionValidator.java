package io.github.nguyenquephong13062003.course_management_system.utils.validations.handle;

import io.github.nguyenquephong13062003.course_management_system.utils.validations.annotations.FileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * FileExtensionValidator is a custom validator that checks if the uploaded file has an allowed extension.
 * It implements the ConstraintValidator interface for the FileExtension annotation and MultipartFile type.
 */
public class FileExtensionValidator
        implements ConstraintValidator<FileExtension, MultipartFile> {

    /**
     * An array of allowed file extensions specified in the FileExtension annotation.
     */
    private String[]  allowedExtensions;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        allowedExtensions = constraintAnnotation.allowedExtensions();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        if(value == null || value.isEmpty()) {
            return true;
        }

        String fileName = value.getOriginalFilename();
        if(fileName == null){
            return false;
        }

        String fileNameLowerCase = fileName.toLowerCase();
        return Arrays.stream(allowedExtensions)
                .anyMatch(fileNameLowerCase::endsWith);

    }

}

