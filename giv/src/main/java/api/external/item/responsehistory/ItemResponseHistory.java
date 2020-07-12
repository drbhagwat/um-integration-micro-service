package api.external.item.responsehistory;

import api.external.entity.BasicLogger;
import api.external.item.entity.Item;
import api.external.item.entity.ResponseDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * This class is used to log response of an item api call.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Component
@JsonIgnoreProperties({"id"})
public class ItemResponseHistory extends BasicLogger<String> {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private String transactionNumber;

  private Item item;

  private ResponseDetail responseDetail;
}
