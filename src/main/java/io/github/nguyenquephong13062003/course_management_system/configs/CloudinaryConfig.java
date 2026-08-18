package io.github.nguyenquephong13062003.course_management_system.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Configuration class for Cloudinary integration.
 */
@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

    /**
     * Creates a Cloudinary bean using the provided CloudinaryProperties.
     *
     * @param properties the CloudinaryProperties containing configuration values
     * @return a configured Cloudinary instance
     */
    @Bean
    public Cloudinary cloudinary(CloudinaryProperties properties) {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", properties.getCloudName(),
                "api_key", properties.getApiKey(),
                "api_secret", properties.getApiSecret()
        ));
    }
    
}
