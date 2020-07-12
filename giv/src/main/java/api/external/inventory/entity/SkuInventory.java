package api.external.inventory.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 
 * @author Dinesh Bhagwat
 * @version 1.0
 * @since 2019-04-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SkuInventory extends BasicLogger<String> implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @JsonUnwrapped
  SKUInventoryKey id;

  private String serialNumber;

  private double onHandQuantity;

  private double protectedQuantity;

  private double lockedQuantity;

  private double allocatedQuantity;

  private double availableQuantity;
}