package api.external.mfg.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents all sku fields of the mfginventory API
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-06-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgInvSku implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String company;

	private String division;

	private String manufacturingPlantCode;

	private String skuBarcode;

	private String season;

	private String seasonYear;

	private String style;

	private String styleSfx;

	private String color;

	private String colorSfx;

	private String secDimension;

	private String quality;

	private String sizeRngeCode;

	private String sizeRelPosnIn;

	private String inventoryType;

	private String lotNumber;

	private String countryOfOrigin;

	private String productStatus;

	private String skuAttribute1;

	private String skuAttribute2;

	private String skuAttribute3;

	private String skuAttribute4;

	private String skuAttribute5;

	private long qty;

	private String serialNumber;

	private String action;

	private String inventorySource;

	private String reasonCode;

	private String desc;
}
