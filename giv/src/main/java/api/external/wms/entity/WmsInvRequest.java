package api.external.wms.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmsInvRequest {
	@NotNull(message = "{TRANSACTION_NUMBER_MANDATORY}")
	@NotBlank(message = "{TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
	private String transactionNumber;

	@NotNull(message = "{DATETIME_IS_NOT_SPECIFIED_OR_EMPTY}")
	private LocalDateTime dateTimeStamp;

	@NotNull(message = "{USER_MANDATORY}")
	@NotBlank(message = "{USER_CANNOT_BE_BLANK}")
	private String user;

	@NotNull(message = "{EMPTY_SKU_LIST}")
	@NotEmpty(message = "{EMPTY_SKU_LIST}")
	private List<WmsSku> multipleSkus;
}
