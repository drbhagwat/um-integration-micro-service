package api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Email configuration to send an automatic email to the newly created user.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Component
@ConfigurationProperties
@PropertySource("file:${CONF_DIR}/messages.properties")
@Data
@NoArgsConstructor
public class MessagesConfig {
}