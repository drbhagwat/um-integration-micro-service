package api.external.campaign.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to log CampaignRequestDetail of a campaign api call.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"id", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime", "responseDetail"})
public class CampaignRequestDetail extends BasicLogger<String> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String transactionNumber;
	
    private String company;

    private String division;

    private String warehouse;

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

	private int minQty;

	private int maxQty;

	private int protectQty;

	private String autoReplenish;
	
	@Transient
	private ResponseDetail responseDetail;
	
	private String channel;

	private String campaignCode;

	private LocalDate campaignStartDate;

	private LocalDate campaignEndDate;

	private String campaignActive;

	private String action;

	@Column(name = "usr")
	private String user = "";

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;
	
	public void save(char responseCode, String responseId) {
		this.responseDetail.setResponseCode(responseCode);
		this.responseDetail.setResponseId(responseId);
	}
}