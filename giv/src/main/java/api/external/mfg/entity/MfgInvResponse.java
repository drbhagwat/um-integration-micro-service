package api.external.mfg.entity;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents to display overall response and responseDesc of the mfginventory API (both header and sku fields)
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since		2019-06-28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgInvResponse {
	@JsonProperty("Response")
	private String response;

	@JsonProperty("ResponseDesc")	
	private String responseDesc;

	@Autowired
	private MfgInvRequest mfginv;

	public void save(String productionOrderNumber, String customerOrderNumber, String purchaseOrderNumber, String transactionNumber) {
		this.mfginv.setProductionOrderNumber(productionOrderNumber);
		this.mfginv.setPurchaseOrderNumber(purchaseOrderNumber);
		this.mfginv.setCustomerOrderNumber(customerOrderNumber);
		this.mfginv.setTransactionNumber(transactionNumber);
	}
}