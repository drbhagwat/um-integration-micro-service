package api.external.wms.query.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sachin Kulkarni
 * @date : 29-10-2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryInventoryDetailsSkuResponse {
    private String company;

    private String division;

    private String warehouse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String channel;

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

    private double allocatedQty;

    private double protectedQty;

    private double onHandQty;

    private double availableQty;

    private double lockedQty;

    private String serialNumber;
}
