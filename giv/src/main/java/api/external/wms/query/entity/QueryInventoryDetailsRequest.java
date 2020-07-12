package api.external.wms.query.entity;

import api.external.entity.BasicLogger;
import api.external.inventory.entity.SKUInventoryKey;
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
public class QueryInventoryDetailsRequest extends BasicLogger<String> {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fromTimeStamp;

    @NotNull
    @NotBlank
    private String channel;

    @NotNull
    @NotBlank
    private String action;

    @NotNull
    @NotBlank
    private String inventorySource;

    @NotNull
    @NotBlank
    private List<SKUInventoryKey> skus;
}
