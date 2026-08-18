package io.github.nguyenquephong13062003.course_management_system.utils.validations.annotations;

import io.github.nguyenquephong13062003.course_management_system.utils.validations.handle.FileExtensionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation for validating file extensions.
 * This annotation can be applied to fields, parameters, and methods to ensure that the file extension is valid.
 */
@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileExtension {

    /**
     * The default error message to be used when the validation fails.
     */
    String message() default "Invalid file extension";

    /**
     * Groups can be used to restrict the set of constraints applied during validation.
     */
    Class<?>[] groups() default {};

    /**
     * The payload can be used to carry additional information about the constraint violation.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies the allowed file extensions for validation.
     * By default, it allows ".jpg", ".png", and ".jpeg" extensions.
     *
     * @return an array of allowed file extensions
     */
    String[] allowedExtensions() default {".jpg",".png",".jpeg"};

}
