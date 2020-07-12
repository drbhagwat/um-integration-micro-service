package api.external.inventory.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents key fields of SKU inventory.
 * 
 * @author Dinesh Bhagwat
 * @version 1.0
 * @since 2019-04-30
 *
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SKUInventoryKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotBlank
    private String company;

    @NotNull
    @NotBlank
    private String division;

    @NotNull
    @NotBlank
    private String warehouse;

    @NotNull
    @NotBlank
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
}