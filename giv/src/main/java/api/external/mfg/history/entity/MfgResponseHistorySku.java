package api.external.mfg.history.entity;

import api.external.entity.BasicLogger;
import api.external.mfg.entity.MfgResponseDetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents entity of the MfgResponseHistorySku.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"id", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime"})
public class MfgResponseHistorySku extends BasicLogger<String> {
    @Id
    @GeneratedValue
    Long id;
    
    private String transactionNumber;
    
    private String productionOrderNumber;
  	
  	private String customerOrderNumber;
  	
  	private String purchaseOrderNumber;

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

    private String serialNumber;

    private long requestedQty;

    private long responseQty;

    private long onhandQty;

    private long allocatedQty;

    private long protectedQty;

    private long lockedQty;

    private long availableQty;
    
    private String reasonCode;

    private String action;

    private String inventorySource;
    
    @Column(name = "description")
    private String desc;
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  	@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonIgnore
  	private LocalDateTime dateTimeStamp;
  	
  	@Column(name = "usr")
  	private String user;

    private MfgResponseDetail responseDetail;
}
