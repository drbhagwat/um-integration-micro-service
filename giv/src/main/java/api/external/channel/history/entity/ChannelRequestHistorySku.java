package api.external.channel.history.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-17
 * 
 * This class represents ChannelRequestHistorySku entity
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChannelRequestHistorySku extends BasicLogger<String> {
	
	@Id
	@GeneratedValue
	Long id;
	
	private String transactionNumber;
	
	private String channel;
	
	private String campaignCode;
		
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
	
	private int qty;

	private String action;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;
	
	@Column(name = "usr")
	private String user;
}
