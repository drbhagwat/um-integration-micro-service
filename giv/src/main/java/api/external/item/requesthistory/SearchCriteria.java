package api.external.item.requesthistory;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents json search parameters of item history logs 
 * 
 * @author Dinesh Bhagwat
 * @version 1.0
 * @since 2019-06-03
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
  private String transactionNumber;

  // name of the division
  private String division;

  // name of the warehouse
  private String warehouse;

  // sku barcode
  private String skuBarcode;

  // start date to search for - as given by the user
  private String userSuppliedStartDate;

  // end date to search for - as given by the user
  private String userSuppliedEndDate;
}