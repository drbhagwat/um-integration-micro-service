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
public class QueryInventoryTransactionsSkuResponse {
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double allocatedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double protectedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String campaignCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String channel;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reasonCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double onHandQty;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double availableQty;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lockedQty;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serialNumber;
}
