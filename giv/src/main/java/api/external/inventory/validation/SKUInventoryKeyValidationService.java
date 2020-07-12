package api.external.inventory.validation;

import api.external.errors.*;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.item.entity.ItemKey;
import api.external.item.validation.ItemValidationService;
import api.external.util.SKUAttributeLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Provides SkuInventory validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @Version 2.0
 * @since : 2019-04-15
 * @since : 2019-09-10
 */
@Service
public class SKUInventoryKeyValidationService {
  @Autowired
  private ItemValidationService itemValidationService;

  @Autowired
  private SkuInventoryRepository skuInventoryRepository;

  @Autowired
  private SKUAttributeLength skuAttributeLength;

  @Value("${INVENTORY_TYPE_MANDATORY}")
  private String inventoryTypeMandatory;

  @Value("${INVALID_INVENTORY_TYPE}")
  private String invalidInventoryType;

  @Value("${LOT_NUMBER_MANDATORY}")
  private String lotNumberMandatory;

  @Value("${COUNTRY_OF_ORIGIN_MANDATORY}")
  private String countryOfOrigin;

  @Value("${PRODUCT_STATUS_MANDATORY}")
  private String productStatusMandatory;
  
  @Value("${SKU_NOT_FOUND}")
  private String skuNotFound;

  /**
   * Finds the company with a given primary key.
   *
   * @param skuInventoryKey - primary key of the SKUInventory.
   * @return SkuInventory - on success, returns the found SkuInventory entity.
   * @throws Exception - on failure, it throws an appropriate exception.
   */
  public SkuInventory findSkuInventory(SKUInventoryKey skuInventoryKey) throws Exception {
    Optional<SkuInventory> skuInventory = skuInventoryRepository.findById(skuInventoryKey);

    if (skuInventory.isEmpty()) {
      throw new SkuNotFound(skuNotFound);
    }
    return skuInventory.get();
  }

  /**
   * Validates given code - for null, empty, invalid characters, and max length.
   *
   * @param skuInventoryKey - key for the sku inventory.
   * @return none - on success, returns nothing.
   * @throws Exception - on different failures, throws different exceptions.
   */
  public SKUInventoryKey validate(SKUInventoryKey skuInventoryKey) throws Exception {
    String company = skuInventoryKey.getCompany();
    String division = skuInventoryKey.getDivision();
    String warehouse = skuInventoryKey.getWarehouse();
    String skuBarcode = skuInventoryKey.getSkuBarcode();
    String season = skuInventoryKey.getSeason();
    String seasonYear = skuInventoryKey.getSeasonYear();
    String style = skuInventoryKey.getStyle();
    String styleSfx = skuInventoryKey.getStyleSfx();
    String  color = skuInventoryKey.getColor();
    String colorSfx = skuInventoryKey.getColorSfx();
    String secDimension = skuInventoryKey.getSecDimension();
    String quality = skuInventoryKey.getQuality();
    String  sizeRngeCode = skuInventoryKey.getSizeRngeCode();
    String sizeRelPosnIn = skuInventoryKey.getSizeRelPosnIn();
    ItemKey itemKey = new ItemKey(company, division, warehouse, skuBarcode, season, seasonYear, style, styleSfx,
        color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);
    itemValidationService.validate(itemKey);

    String inventoryType = skuInventoryKey.getInventoryType();

    if (Predicate.isEqual(null).test(inventoryType)) {
      throw new InventoryTypeMandatory(inventoryTypeMandatory);
    }

    if (Predicate.isEqual("F").negate().test(inventoryType)) {
      throw new InvalidInventoryType(invalidInventoryType);
    }

    if (Predicate.isEqual(null).test(skuInventoryKey.getLotNumber())) {
      throw new LotNumberMandatory(lotNumberMandatory);
    }

    if (Predicate.isEqual(null).test(skuInventoryKey.getCountryOfOrigin())) {
      throw new CountryOfOriginMandatory(countryOfOrigin);
    }

    if (skuInventoryKey.getProductStatus() == null) {
      throw new ProductStatusMandatory(productStatusMandatory);
    }
    skuAttributeLength.validate(skuInventoryKey.getSkuAttribute1());
    skuAttributeLength.validate(skuInventoryKey.getSkuAttribute2());
    skuAttributeLength.validate(skuInventoryKey.getSkuAttribute3());
    skuAttributeLength.validate(skuInventoryKey.getSkuAttribute4());
    skuAttributeLength.validate(skuInventoryKey.getSkuAttribute5());

    return skuInventoryKey;
  }
  
  public SkuInventory findSkuInventoryKey(SKUInventoryKey skuInventoryKey) throws Exception {
	    Optional<SkuInventory> skuInventory = skuInventoryRepository.findById(skuInventoryKey);
	    return skuInventory.get();
	  }
}