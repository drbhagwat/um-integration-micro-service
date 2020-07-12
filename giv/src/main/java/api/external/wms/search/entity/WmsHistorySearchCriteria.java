package api.external.wms.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmsHistorySearchCriteria {
	private String transactionNumber;

	private String division;

	private String warehouse;

	private String skuBarcode;

	// start date to search for - as given by the user
	private String userSuppliedStartDate;
	
	// end date to search for - as given by the user
	private String userSuppliedEndDate;
	
	// sortBy parameter - default is date_time_stamp
	private String sortBy;
}