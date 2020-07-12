package api.external.wms.query.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sachin Kulkarni
 * @date : 28-10-2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryInventoryDetailsResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fromTimeStamp;

    @NotNull
    @NotBlank
    private String action;

    @NotNull
    @NotBlank
    private String inventorySource;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String channel;

    @NotNull
    @NotBlank
    private List<QueryInventoryDetailsSkuResponse> skus;
}
