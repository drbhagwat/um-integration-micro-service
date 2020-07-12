package api.external.wms.entity;

import api.external.entity.BasicLogger;
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
 * @author : Sachin Kulkarni
 * @date : 16-10-2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"id", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime"})
public class WmsResponseDetail extends BasicLogger<String> {
    @Id
    @GeneratedValue
    Long id;

    private String transactionNumber;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTimeStamp;

    @Column(name = "usr")
    private String user = "";

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

    private String serialNumber;

    private long requestedQty;

    private double responseQty;

    private double onhandQty;

    private double allocatedQty;

    private double protectedQty;

    private double lockedQty;

    private double availableQty;

    private String action;

    private String inventorySource;

    private String reasonCode;

    @Column(name = "description")
    private String desc;

    private ResponseDetail responseDetail;
}
