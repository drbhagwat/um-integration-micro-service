package api.external.item.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents Item search criteria fields.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchCriteria {
  private String skuBarcode;

  private String style;

  private String styleSfx;

  private String color;

  private String colorSfx;

  private String sizeRngeCode;
}
