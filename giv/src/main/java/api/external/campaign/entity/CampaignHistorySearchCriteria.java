package api.external.campaign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents CampaignHistorySearchCriteria fields for logs.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignHistorySearchCriteria {
	private String transactionNumber;

	// name of the channel
	private String channel;
	
	// campaign Code 
	private String campaignCode;

	// name of the warehouse
	private String warehouse;

	// sku barcode
	private String skuBarcode;

	// start date to search for - as given by the user
	private String userSuppliedStartDate;
	
	// end date to search for - as given by the user
	private String userSuppliedEndDate;
	
	// sortBy parameter - default is date_time_stamp
	private String sortBy;
}