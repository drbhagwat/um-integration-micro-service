package com.s3groupinc.gateway.api.config.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Manages the configuration for file upload. upLoad dir is /tmp for now.
 */
@ConfigurationProperties(prefix = "file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageProperties {
    private String uploadDir;
}