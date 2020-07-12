package api.external.channel.entity;

import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents response sku fields of the ChannelAPI
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since	2019-10-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAPIResponseSku implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String company;

	private String division;

	private String warehouse;
	
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
	
	private String salesOrderNumber;
	
	private String orderType;
	
	private LocalDate shipDate; 
	
	private String inventorySource;
	
	@JsonProperty("requestedQty")	
	private int qty;
	
	private double responseQty;
	
	private double onHandQty;
	
	private double protectedQty;
	
	private double allocatedQty;
	
	private double lockedQty;
	
	private double availableQty;

	private String action;
	
	@JsonProperty("ResponseDetail")
	private ChannelAPIResponseDetail channelAPIResponseDetail;
}