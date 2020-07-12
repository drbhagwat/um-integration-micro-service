package api.external.mfg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents search fields of manufacturing inventory table in the
 * db
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgInventorySearchCriteria {

	private String productionOrderNumber;

	private String manufacturingPlantCode;

	private String style;

	private String styleSfx;

	private String color;

	private String quality;

	private String sizeRngeCode;

	private String skuBarcode;

	private String lotNumber;

	private String productStatus;

	private String skuAttribute1;

	private String countryOfOrigin;

	private String sortBy;

}
