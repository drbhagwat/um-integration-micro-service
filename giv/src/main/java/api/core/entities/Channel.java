package api.core.entities;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class represents Channel entity
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Channel extends BasicLogger<String> {
  @Id
  @NotNull(message = "{CHANNEL_CODE_MANDATORY}")
  @NotBlank(message = "{CHANNEL_CODE_CANNOT_BE_BLANK}")
  @JsonProperty("code")
  String id;

  @NotNull(message = "{CHANNEL_NAME_MANDATORY}")
  @NotBlank(message = "{CHANNEL_NAME_CANNOT_BE_BLANK}")
  private String name;
}