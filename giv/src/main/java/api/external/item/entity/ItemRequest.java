package api.external.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class is used to send the json response of an item api call.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ItemRequest extends Item {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @NotNull(message = "{TRANSACTION_NUMBER_MANDATORY}")
  @NotBlank(message = "{TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
  @Column(unique = true)
  private String transactionNumber;

  @NotNull(message = "{ACTION_MANDATORY}")
  @NotBlank(message = "{ACTION_CANNOT_BE_BLANK}")
  private String action;
}