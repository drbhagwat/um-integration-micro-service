package api.external.mfg.history.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class represents search parameters of the mfg history tables.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgHistorySearchCriteria {

	private String transactionNumber;

	// code of the manufacturingPlant
	private String manufacturingPlantCode;

	// sku barcode
	private String skuBarcode;

	// start date to search for - as given by the user
	private String userSuppliedStartDate;
	
	// end date to search for - as given by the user
	private String userSuppliedEndDate;
	
	// sortBy parameter - default is date_time_stamp
	private String sortBy;
}