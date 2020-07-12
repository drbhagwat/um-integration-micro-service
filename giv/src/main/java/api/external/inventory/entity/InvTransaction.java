package api.external.inventory.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class represents inventory transaction entity
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-22
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"new", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime"})
public class InvTransaction extends BasicLogger<String> {
  @Id
  @GeneratedValue
  public Long id;

  private SKUInventoryKey skuInventoryKey;

  private double allocatedQuantity;

  private double protectedQuantity;

  private String campaignCode;

  private String channel;

  private String reasonCode;

  @Column(name = "usr")
  private String user;

  private LocalDateTime dateTimeStamp;

  private @Transient
  boolean isNew = true;

  @PostPersist
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

}
