package api.external.mfg.history.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents entity of the MfgRequestHistoryHeader.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MfgRequestHistoryHeader extends BasicLogger<String> {
   

    @Id
  	@Column(name = "transactionNumber", updatable = false, nullable = false)
  	private String transactionNumber;
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTimeStamp;

    @Column(name = "usr")
    private String user;
    
    private String productionOrderNumber;
  	
  	private String customerOrderNumber;
  	
  	private String purchaseOrderNumber;

    @OneToMany(cascade = CascadeType.ALL)
  	@JoinColumn(name = "transactionNumber")
    private List<MfgRequestHistorySku> multipleSkus = new ArrayList<>();
}
