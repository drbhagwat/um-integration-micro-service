package api.external.item.requesthistory;

import api.external.entity.BasicLogger;
import api.external.item.entity.Item;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class is used to log request of an item api call.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
@JsonIgnoreProperties({"id"})
public class ItemRequestHistory extends BasicLogger<String> {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private String transactionNumber;

  private Item item;
}

