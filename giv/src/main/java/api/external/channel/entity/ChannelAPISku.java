package api.external.channel.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents all sku fields of the ChannelAPI
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-05-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAPISku implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String company;

	@NotNull
	private String division;

	@NotNull
	private String warehouse;

	private String manufacturingPlantCode;

	@NotNull
	private String skuBarcode;

	@NotNull
	private String season;

	@NotNull
	private String seasonYear;

	@NotNull
	private String style;

	@NotNull
	private String styleSfx;

	@NotNull
	private String color;

	@NotNull
	private String colorSfx;

	@NotNull
	private String secDimension;

	@NotNull
	private String quality;

	@NotNull
	private String sizeRngeCode;

	@NotNull
	private String sizeRelPosnIn;

	@NotNull
	private String inventoryType;

	@NotNull
	private String lotNumber;

	@NotNull
	private String countryOfOrigin;

	@NotNull
	private String productStatus;

	@NotNull
	private String skuAttribute1;

	@NotNull
	private String skuAttribute2;

	@NotNull
	private String skuAttribute3;

	@NotNull
	private String skuAttribute4;

	@NotNull
	private String skuAttribute5;

	private String salesOrderNumber;

	private String orderType;

	private LocalDate shipDate;

	private String inventorySource;

	private int qty;

	private String action;

	private ChannelAPIResponseDetail responseDetail;
}