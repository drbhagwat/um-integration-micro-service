package api.core.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents ManufacturingPlant primary key.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-06-13
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturingPlantKey implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @NotNull(message = "{COMPANY_CODE_MANDATORY}")
  @JsonProperty("companyCode")
  private String compCode;

  @NotNull(message = "{CODE_MANDATORY}")
  @NotBlank(message = "{CODE_CANNOT_BE_BLANK}")
  @JsonProperty("code")
  private String code;
}
