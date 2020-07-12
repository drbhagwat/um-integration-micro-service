package api.external.channel.history.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-21
 * 
 * This class represents the SearchCriteria for ChannelRequest and ChannelResponse Histories.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorySearchCriteria {

	private String transactionNumber;

	// name of the division
	private String division;

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