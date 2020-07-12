package api.core.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents Division's primary Key
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DivisionKey implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @NotNull(message = "{COMPANY_CODE_MANDATORY}")
  @JsonProperty("companyCode")
  private String compCode;

  @NotNull(message = "{CODE_MANDATORY}")
  @JsonProperty("code")
  private String code;
}
