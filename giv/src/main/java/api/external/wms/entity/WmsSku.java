package api.external.wms.entity;

import api.external.inventory.entity.SKUInventoryKey;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmsSku implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@JsonUnwrapped
	private SKUInventoryKey id;

	private String serialNumber;

	private long qty;

	private String action;

	private String inventorySource;

	private String reasonCode;

	private String desc;
}