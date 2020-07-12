package api.external.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents json fields of item history logs
 * 
 * @author Dinesh Bhagwat
 * @version 1.0
 * @since 2019-10-21
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemLogSearch {
  private LocalDate ldtUserSuppliedStartDate;
  private LocalDate ldtUserSuppliedEndDate;
  private byte finalCompare;
  private String sortBy;
  private String division;
  private String warehouse;
  private String skuBarcode;
}
