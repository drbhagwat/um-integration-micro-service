package api.external.mfg.history.entity;

import api.external.entity.BasicLogger;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * This class represents entity of the MfgRequestHistorySku.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MfgRequestHistorySku extends BasicLogger<String> {
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
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  	@JsonSerialize(using = LocalDateTimeSerializer.class)
  	private LocalDateTime dateTimeStamp;
  	
  	@Column(name = "usr")
  	private String user;

    private String serialNumber;

    private long qty;

    private String action;

    private String inventorySource;

    private String reasonCode;

    @Column(name = "description")
    private String desc;
}
