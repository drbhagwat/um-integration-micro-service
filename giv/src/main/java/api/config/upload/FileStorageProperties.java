package api.config.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Manages the configuration for file upload. upLoad dir is /tmp for now.
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-09-26
 */
@ConfigurationProperties(prefix = "file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageProperties {
  private String uploadDir;
}