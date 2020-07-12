package api.external.item.validation;

import api.core.entities.Warehouse;
import api.core.entities.WarehouseKey;
import api.core.errors.*;
import api.core.repo.WarehouseRepository;
import api.core.validation.CodeValidationService;
import api.core.validation.WarehouseValidationService;
import api.external.errors.*;
import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.errors.*;
import api.external.item.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;


/**
 * This class describes the item validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Component
public class ItemValidationService {
  @Value("${WAREHOUSE_NOT_FOUND}")
  private String warehouseNotFound;

  @Value("${COMPANY_MANDATORY}")
  private String companyMandatory;

  @Value("${COMPANY_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String companyCannotContainSpecialCharacters;

  @Value("${COMPANY_INVALID_LENGTH}")
  private String companyInvalidLength;

  @Value("${DIVISION_MANDATORY}")
  private String divisionMandatory;

  @Value("${DIVISION_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String divisionCannotContainSpecialCharacters;

  @Value("${DIVISION_INVALID_LENGTH}")
  private String divisionInvalidLength;

  @Value("${WAREHOUSE_MANDATORY}")
  private String warehouseMandatory;

  @Value("${WAREHOUSE_CANNOT_BE_BLANK}")
  private String warehouseCannotBeBlank;

  @Value("${WAREHOUSE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String warehouseCannotContainSpecialCharacters;

  @Value("${WAREHOUSE_INVALID_LENGTH}")
  private String warehouseInvalidLength;

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

  @Value("${ACTION_MANDATORY}")
  private String actionMandatory;

  @Value("${INVALID_ACTION}")
  private String invalidAction;

  @Value("${DATETIME_IS_NOT_SPECIFIED_OR_EMPTY}")
  private String dateTimeIsNotSpecifiedOrEmpty;

  @Value("${INVALID_LOTCONTROLUSED}")
  private String invalidLotControlUsed;

  @Value("${INVALID_SERIALNUMBERUSED}")
  private String invalidSerialNumberUsed;

  @Value("${ADD_REQUEST}")
  private String addRequest;

  @Value("${UPDATE_REQUEST}")
  private String updateRequest;

  @Value("${LOTCONTROLUSED}")
  private String[] lotControlUsed;

  @Value("${SERIALNUMBERUSED}")
  private String[] serialNumberUsed;

  @Value("${ITEM_ALREADY_EXISTS}")
  private String itemAlreadyExists;

  @Value("${ITEM_NOT_FOUND}")
  private String itemNotFound;

  @Autowired
  private CodeValidationService codeValidationService;

  @Autowired
  private WarehouseValidationService warehouseValidationService;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private WarehouseRepository warehouseRepository;

  public void validate(ItemKey itemKey) throws Exception {
    String company = itemKey.getCompany();
    String division = itemKey.getDivision();
    String warehouse = itemKey.getWarehouse();
    String skuBarcode = itemKey.getSkuBarcode();
    String season = itemKey.getSeason();
    String seasonYear = itemKey.getSeasonYear();
    String style = itemKey.getStyle();
    String styleSfx = itemKey.getStyleSfx();
    String color = itemKey.getColor();
    String colorSfx = itemKey.getColorSfx();
    String secDimension = itemKey.getSecDimension();
    String quality = itemKey.getQuality();
    String sizeRngeCode = itemKey.getSizeRngeCode();
    String sizeRelPosnIn = itemKey.getSizeRelPosnIn();
    WarehouseKey warehouseKey = new WarehouseKey(company, division, warehouse);

    try {
      warehouseValidationService.validate(warehouseKey);
    } catch (CompanyCodeCannotContainSpecialCharacters exception) {
      throw new CompanyCodeCannotContainSpecialCharacters(companyCannotContainSpecialCharacters);
    } catch (CompanyMaxLengthExceeded exception) {
      throw new CompanyMaxLengthExceeded(companyInvalidLength);
    } catch (DivisionCodeCannotContainSpecialCharacters exception) {
      throw new DivisionCodeCannotContainSpecialCharacters(divisionCannotContainSpecialCharacters);
    } catch (DivisionMaxLengthExceeded exception) {
      throw new DivisionMaxLengthExceeded(divisionInvalidLength);
    }
    company = company.stripTrailing();
    division = division.stripTrailing();
    warehouse = warehouse.stripTrailing();
    warehouseKey = new WarehouseKey(company, division, warehouse);
    Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouseKey);

    if (optionalWarehouse.isEmpty()) {
      throw new WarehouseNotFound(warehouseNotFound);
    }

    try {
      codeValidationService.validateAll(skuBarcode);
      codeValidationService.validate(skuBarcode.length(), skuBarcodeMaxLength);

      if (Predicate.isEqual(null).test(season)) {
        throw new SeasonMandatory(seasonMandatory);
      }

      if (Predicate.isEqual(null).test(seasonYear)) {
        throw new SeasonYearMandatory(seasonYearMandatory);
      }

      if (Predicate.isEqual(null).test(style)) {
        throw new StyleMandatory(styleMandatory);
      }

      if (Predicate.isEqual(null).test(styleSfx)) {
        throw new StyleSfxMandatory(styleSfxMandatory);
      }

      if (Predicate.isEqual(null).test(color)) {
        throw new ColorMandatory(colorMandatory);
      }

      if (Predicate.isEqual(null).test(colorSfx)) {
        throw new ColorSfxMandatory(colorSfxMandatory);
      }

      if (Predicate.isEqual(null).test(secDimension)) {
        throw new SecDimensionMandatory(secDimensionMandatory);
      }

      if (Predicate.isEqual(null).test(quality)) {
        throw new QualityMandatory(qualityMandatory);
      }

      if (Predicate.isEqual(null).test(sizeRngeCode)) {
        throw new SizeRngeCodeMandatory(sizeRngeCodeMandatory);
      }

      if (Predicate.isEqual(null).test(sizeRelPosnIn)) {
        throw new SizeRelPosnInMandatory(sizeRelPosnInMandatory);
      }
    } catch (CodeMandatory exception) {
      throw new SkuBarcodeMandatory(skuBarcodeMandatory);
    } catch (CodeCannotBeBlank exception) {
      throw new SkuBarcodeCannotBeBlank(skuBarcodeCannotBeBlank);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new SkuBarcodeCannotContainSpecialCharacters(skuBarcodeCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new SkuBarcodeMaxLengthExceeded(skuBarcodeInvalidLength);
    }
  }

  /**
   * @param item   - Item that was given by the API caller.
   * @param action - What action he wants to perform.
   * @return - On success, valid ItemKey would be returned, otherwise null would be returned
   * @throws Exception - Appropriate Exception would be thrown if we cannot continue.
   */
  public Item validate(Item item, String action) throws Exception {
    String upperCaseAction = validate(action);

    // action should be either Add or Subtract
    if (upperCaseAction == null) {
      throw new InvalidAction(invalidAction);
    }

    String itemDesc = item.getItemDesc();

    // if the itemDesc field is present in the API and blank
    if (((itemDesc != null) && (itemDesc.isBlank()))) {

      // replace with style description (only if it is present in Json input)
      if (item.getStyleDesc() != null) {
        item.setItemDesc(item.getStyleDesc());
      }
    }
    // validate lot control used
    if (!validateLotControlUsed(item.getLotControlUsed())) {
      throw new InvalidLotControlUsed(invalidLotControlUsed);
    }

    // validate serial number used
    if (!validateSerialNumberUsed(item.getSerialNumberUsed())) {
      throw new InvalidSerialNumberUsed(invalidSerialNumberUsed);
    }

    // get user supplied datetime stamp
    LocalDateTime dateTimeStamp = item.getDateTimeStamp();

    // validate it
    if (dateTimeStamp == null) {
      throw new DateTimeIsMandatoryAndCannotBeBlank(dateTimeIsNotSpecifiedOrEmpty);
    }
    ItemKey tempKey = item.getId();
    validate(tempKey);

    Optional<Item> optionalItem = itemRepository.findById(tempKey);
    Item existingItem = null;

    if (!optionalItem.isEmpty()) {
      existingItem = optionalItem.get();
    }

    // if request is add, existingItem should be null.
    if (upperCaseAction.equals(addRequest) && (existingItem != null)) {
      throw new ItemAlreadyExists(itemAlreadyExists);
    }

    // if request is change, existingItem should not be null.
    if (upperCaseAction.equals(updateRequest) && (existingItem == null)) {
      throw new ItemNotFound(itemNotFound);
    }
    return existingItem;
  }

  private String validate(String action) throws Exception {
    if (action == null) {
      throw new ActionMandatory(actionMandatory);
    }
    String toUpperAction = action.trim().toUpperCase();
    return (toUpperAction.equals(addRequest) || toUpperAction.equals(updateRequest)) ? toUpperAction : null;
  }

  public boolean validateLotControlUsed(String lotControl) {

    if ((lotControl == null) || (lotControl.equals(""))) {
      return true;
    } else {
      String toUpperLotControl = lotControl.toUpperCase();

      return toUpperLotControl.equals(lotControlUsed[0]) || toUpperLotControl.equals(lotControlUsed[1]);
    }
  }

  public boolean validateSerialNumberUsed(String serialNumber) {
    if ((serialNumber == null) || (serialNumber.equals(""))) {
      return true;
    } else {
      String toUpperSerialNumber = serialNumber.toUpperCase();

      return toUpperSerialNumber.equals(serialNumberUsed[0])
        || toUpperSerialNumber.equals(serialNumberUsed[1]);
    }
  }
}