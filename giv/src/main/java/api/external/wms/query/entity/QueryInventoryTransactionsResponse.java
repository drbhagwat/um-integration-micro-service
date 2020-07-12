package api.external.wms.query.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sachin Kulkarni
 * @date : 29-10-2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryInventoryTransactionsResponse {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fromTimeStamp;

    private String action;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String channel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestFor;

    private String inventorySource;

    private List<QueryInventoryTransactionsSkuResponse> transactions;
}
