package api.external.wms.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @author : Sachin Kulkarni
 * @date : 16-10-2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WmsRequestHeader extends BasicLogger<String> {
    @Id
    @Column(name = "transactionNumber", updatable = false, nullable = false)
    private String transactionNumber;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTimeStamp;

    @Column(name = "usr")
    private String user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="transactionNumber")
    private List<WmsRequestDetail> wmsRequestDetails = new ArrayList<>();
}
