
package api.external.inventory.validation;

import api.core.entities.ManufacturingPlant;
import api.core.entities.ManufacturingPlantKey;
import api.core.errors.*;
import api.core.services.ManufacturingPlantService;
import api.core.validation.CodeValidationService;
import api.core.validation.ManufacturingPlantValidationService;
import api.external.errors.SkuBarcodeCannotBeBlank;
import api.external.errors.SkuBarcodeCannotContainSpecialCharacters;
import api.external.errors.SkuBarcodeMandatory;
import api.external.errors.SkuBarcodeMaxLengthExceeded;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.util.SKUAttributeLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;


/**
 * Provides MfgInventory validation service.
 *
 * @author : Thamilarasi
 * @Version 2.0
 * @since : 2019-09-10
 */

@Service
public class ManufacturingInventoryKeyValidationService {
  @Autowired
  private CodeValidationService codeValidationService;

  @Autowired
  private SKUAttributeLength skuAttributeLength;

  @Autowired
  private ManufacturingPlantValidationService manufacturingPlantValidationService;

  @Autowired
  private ManufacturingPlantService manufacturingPlantService;

  @Value("${COMPANY_MANDATORY}")
  private String companyMandatory;

  @Value("${COMPANY_CANNOT_BE_BLANK}")
  private String companyCannotBeBlank;

  @Value("${COMPANY_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String companyCannotContainSpecialCharacters;

  @Value("${COMPANY_MAX_LENGTH}")
  private int companyMaxLength;

  @Value("${COMPANY_INVALID_LENGTH}")
  private String companyInvalidLength;

  @Value("${DIVISION_MANDATORY}")
  private String divisionMandatory;

  @Value("${DIVISION_CANNOT_BE_BLANK}")
  private String divisionCannotBeBlank;

  @Value("${DIVISION_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String divisionCannotContainSpecialCharacters;

  @Value("${DIVISION_MAX_LENGTH}")
  private int divisionMaxLength;

  @Value("${DIVISION_INVALID_LENGTH}")
  private String divisionInvalidLength;

  @Value("${SKUBARCODE_MANDATORY}")
  private String skuBarcodeMandatory;

  @Value("${SKUBARCODE_CANNOT_BE_BLANK}")
  private String skuBarcodeCannotBeBlank;

  @Value("${SKUBARCODE_MAX_LENGTH}")
  private int skuBarcodeMaxLength;

  @Value("${SKUBARCODE_INVALID_LENGTH}")
  private String skuBarcodeInvalidLength;

  @Value("${SKUBARCODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String skuBarcodeCannotContainSpecialCharacters;

  @Value("${PRODUCT_STATUS_MANDATORY}")
  private String productStatusMandatory;

  @Value("${SEASON_MANDATORY}")
  private String seasonMandatory;

  @Value("${SEASON_YEAR_MANDATORY}")
  private String seasonYearMandatory;

  @Value("${STYLE_MANDATORY}")
  private String styleMandatory;

  @Value("${STYLE_SFX_MANDATORY}")
  private String styleSfxMandatory;

  @Value("${COLOR_MANDATORY}")
  private String colorMandatory;

  @Value("${COLOR_SFX_MANDATORY}")
  private String colorSfxMandatory;

  @Value("${SEC_DIMENSION_MANDATORY}")
  private String secDimensionMandatory;

  @Value("${QUALITY_MANDATORY}")
  private String qualityMandatory;

  @Value("${SIZE_RNGE_CODE_MANDATORY}")
  private String sizeRngeCodeMandatory;

  @Value("${SIZE_REL_POSN_IN_MANDATORY}")
  private String sizeRelPosnInMandatory;

  @Value("${INVENTORY_TYPE_MANDATORY}")
  private String inventoryTypeMandatory;

  @Value("${INVALID_INVENTORY_TYPE}")
  private String invalidInventoryType;

  @Value("${LOT_NUMBER_MANDATORY}")
  private String lotNumberMandatory;

  @Value("${COUNTRY_OF_ORIGIN_MANDATORY}")
  private String countryOfOrigin;

  @Value("${MANUFACTURING_PLANT_CODE_MAX_LENGTH}")
  private int manufacturingPlantCodeMaxLength;

  @Value("${MANUFACTURING_PLANT_CODE_INVALID_LENGTH}")
  private String manufacturingPlantCodeInvalidLength;

  @Value("${MANUFACTURING_PLANT_CODE_MANDATORY}")
  private String manufacturingPlantCodeMandatory;

  @Value("${MANUFACTURING_PLANT_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String manufacturingPlantCodeCannotContainSpecialCharacters;

  @Value("${MANUFACTURING_PLANT_CODE_CANNOT_BE_BLANK}")
  private String manufacturingPlantCodeCannotBeBlank;

  @Value("${MANUFACTURING_PLANT_CODE_NOT_FOUND}")
  private String manufacturingPlantNotFound;

  /**
   * Validates given code - for null, empty, invalid characters, and max length.
   *
   * @param manufacturingInventoryKey - key for the manufacturing inventory.
   * @throws Exception - on different failures, throws different exceptions.
   */

  public void validate(ManufacturingInventoryKey manufacturingInventoryKey) throws Exception {
    String companyCode = manufacturingInventoryKey.getCompany();
    String mfgPlantCode = manufacturingInventoryKey.getManufacturingPlantCode();
    ManufacturingPlantKey manufacturingPlantKey = new ManufacturingPlantKey(companyCode, mfgPlantCode);
    manufacturingPlantValidationService.validate(manufacturingPlantKey);
    String division = manufacturingInventoryKey.getDivision();
    companyCode = companyCode.stripTrailing();
    mfgPlantCode = mfgPlantCode.stripTrailing();
    manufacturingPlantKey = new ManufacturingPlantKey(companyCode, mfgPlantCode);
    ManufacturingPlant manufacturingPlant = manufacturingPlantService.get(manufacturingPlantKey);

    if (manufacturingPlant == null) {
      throw new ManufacturingPlantNotFound(manufacturingPlantNotFound);
    }

    try {
      codeValidationService.validate(division);
      codeValidationService.validate(division.length(), divisionMaxLength);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new DivisionCodeCannotContainSpecialCharacters(divisionCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new DivisionMaxLengthExceeded(divisionInvalidLength);
    }

    try {
      String skuBarcode = manufacturingInventoryKey.getSkuBarcode();
      codeValidationService.validateAll(skuBarcode);
      codeValidationService.validate(skuBarcode.length(), skuBarcodeMaxLength);
    } catch (CodeMandatory exception) {
      throw new SkuBarcodeMandatory(skuBarcodeMandatory);
    } catch (CodeCannotBeBlank exception) {
      throw new SkuBarcodeCannotBeBlank(skuBarcodeCannotBeBlank);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new SkuBarcodeCannotContainSpecialCharacters(skuBarcodeCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new SkuBarcodeMaxLengthExceeded(skuBarcodeInvalidLength);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getSeason())) {
      throw new Exception(seasonMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getSeasonYear())) {
      throw new Exception(seasonYearMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getStyle())) {
      throw new Exception(styleMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getStyleSfx())) {
      throw new Exception(styleSfxMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getColor())) {
      throw new Exception(colorMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getColorSfx())) {
      throw new Exception(colorSfxMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getSecDimension())) {
      throw new Exception(secDimensionMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getQuality())) {
      throw new Exception(qualityMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getSizeRngeCode())) {
      throw new Exception(sizeRngeCodeMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getSizeRelPosnIn())) {
      throw new Exception(sizeRelPosnInMandatory);
    }
    String inventoryType = manufacturingInventoryKey.getInventoryType();

    if (Predicate.isEqual(null).test(inventoryType)) {
      throw new Exception(inventoryTypeMandatory);
    }

    if (Predicate.isEqual("F").negate().test(inventoryType)) {
      throw new Exception(invalidInventoryType);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getLotNumber())) {
      throw new Exception(lotNumberMandatory);
    }

    if (Predicate.isEqual(null).test(manufacturingInventoryKey.getCountryOfOrigin())) {
      throw new Exception(countryOfOrigin);
    }

    if (manufacturingInventoryKey.getProductStatus() == null) {
      throw new Exception(productStatusMandatory);
    }

    skuAttributeLength.validate(manufacturingInventoryKey.getSkuAttribute1());
    skuAttributeLength.validate(manufacturingInventoryKey.getSkuAttribute2());
    skuAttributeLength.validate(manufacturingInventoryKey.getSkuAttribute3());
    skuAttributeLength.validate(manufacturingInventoryKey.getSkuAttribute4());
    skuAttributeLength.validate(manufacturingInventoryKey.getSkuAttribute5());
  }
}
