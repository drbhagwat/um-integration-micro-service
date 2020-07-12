package api.external.wms.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents search fields of sku inventory table in the db
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-15
 * <p>
 * * @author Dinesh Bhagwat
 * * @version 2.0
 * * @since 2019-10-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmsInventorySearchCriteria {
  private String style;

  private String styleSfx;

  private String color;

  private String quality;

  private String sizeRngeCode;

  private String skuBarcode;

  private String lotNumber;

  private String productStatus;

  private String skuAttribute1;

  private String countryOfOrigin;

  private String sortBy;
}
