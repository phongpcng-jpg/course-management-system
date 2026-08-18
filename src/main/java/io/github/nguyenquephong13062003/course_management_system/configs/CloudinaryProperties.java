package io.github.nguyenquephong13062003.course_management_system.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CloudinaryProperties class holds the configuration properties for Cloudinary.
 * It is annotated with @ConfigurationProperties to bind the properties from the application configuration file.
 * The prefix "cloudinary" indicates that the properties will be prefixed with "cloudinary" in the configuration file.
 */
@ConfigurationProperties(prefix = "cloudinary")
@Getter
@Setter
public class CloudinaryProperties {

    /**
     * The cloud name for the Cloudinary account.
     */
    private String cloudName;

    /**
     * The API key for the Cloudinary account.
     */
    private String apiKey;

    /**
     * The API secret for the Cloudinary account.
     */
    private String apiSecret;
    
}
