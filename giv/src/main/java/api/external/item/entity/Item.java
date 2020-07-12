package api.external.item.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class describes the item entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"new", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime"})
@Component
public class Item extends BasicLogger<String> implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @JsonUnwrapped
  private ItemKey id;

  private String sizeDesc;

  private String productGroup;

  private String productSubgroup;

  private String productType;

  private String productLine;

  private String salesGroup;

  private int shelfDays;

  private String shipAlone;

  private String unitOfMeasure;

  private String stockingUM;

  private String purchasingUM;

  private String sellingUM;

  private String itemDesc;

  private String styleDesc;

  private String colorDesc;

  private String lotControlUsed;

  private String serialNumberUsed;

  private String price;

  private String retlPrice;

  private String ticketType;

  private String sensorTagType;

  private String countryOfOrigin;

  private String acceptSkuByIdCodes;

  private String acceptSkuByLotNumber;

  private String acceptSkuByProductStatus;

  private String acceptSkuByCountryOfOrigin;

  private LocalDateTime dateTimeStamp;

  @Column(name = "usr")
  private String user;
}