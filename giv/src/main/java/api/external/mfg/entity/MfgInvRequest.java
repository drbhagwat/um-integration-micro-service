package api.external.mfg.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents all fields of mfginventory API
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-06-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgInvRequest {
	@NotNull
	private String transactionNumber;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;

	@NotNull
	private String user;

	@NotNull
	private String productionOrderNumber;

	@NotNull
	private String customerOrderNumber;

	@NotNull
	private String purchaseOrderNumber;

	@NotNull
	private List<MfgInvSku> multipleSkus = new ArrayList<>();
}
