package api.external.channel.service;

import api.core.errors.KeyFieldCannotBeBlank;
import api.core.errors.KeyFieldMandatory;
import api.core.errors.TransactionNumberCannotBeBlank;
import api.core.errors.TransactionNumberMandatory;
import api.core.validation.*;
import api.external.campaign.validation.CampaignValidationService;
import api.external.channel.entity.ChannelAPI;
import api.external.channel.entity.ChannelAPIResponseDetail;
import api.external.channel.entity.ChannelAPIResponseSku;
import api.external.channel.entity.ChannelAPISku;
import api.external.channel.errors.AllocatedQtyIsLessThanRequestedQty;
import api.external.channel.errors.AllocatedQtyZero;
import api.external.channel.errors.AvailableQtyAndProtectQtyZero;
import api.external.channel.errors.AvailableQtyZero;
import api.external.channel.history.entity.ChannelResponseHistoryHeader;
import api.external.channel.history.repo.ChannelResponseHistoryHeaderRepository;
import api.external.channel.history.service.ChannelRequestHistoryService;
import api.external.channel.history.service.ChannelResponseHistoryService;
import api.external.channel.validation.ChannelAPIValidationService;
import api.external.common.key.validation.WmsInventoryKeyValidationService;
import api.external.errors.*;
import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.ManufacturingInventoryService;
import api.external.inventory.validation.ManufacturingInventoryKeyValidationService;
import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.errors.ItemNotFound;
import api.external.item.repo.ItemRepository;
import api.util.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Process the channel request to Update the sku_inventory table and manufacturing_inventory table</h1>
 * Provides read and updation services for channelAPI Controller. The
 * channelAPIService implements business logic based on channels (catalog,
 * retail, web) with one header and multiple Skus, and displays corresponding
 * success or failure messages
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-05-21
 */

@Service
public class ChannelAPIService {

  @Autowired
  private CampaignValidationService campaignValidationService;

  @Autowired
  private ChannelResponseHistoryHeaderRepository channelResponseHistoryHeaderRepository;

  @Autowired
  private ChannelRequestHistoryService channelRequestHistoryService;

  @Autowired
  private ChannelResponseHistoryService channelResponseHistoryService;

  @Autowired
  private ManufacturingInventoryService manufacturingInventoryService;

  @Autowired
  private Request request;

  @Autowired
  private WmsInventoryKeyValidationService wmsInventoryKeyValidationService;

  @Autowired
  private ManufacturingInventoryKeyValidationService manufacturingInventoryKeyValidationService;

  @Autowired
  private ChannelAPIValidationService channelAPIValidationService;

  @Autowired
  private CodeValidationService codeValidationService;

  @Autowired
  private ItemRepository itemRepository;

  @Value("${FAILURE_RESPONSE}")
  private char f;

  @Value("${SUCCESS_RESPONSE}")
  private char s;

  @Value("${PARTIAL_RESPONSE}")
  private char p;

  @Value("${SUCCESS_SKUS}")
  private String successSkus;

  @Value("${FAILURE_SKUS}")
  private String failureSkus;

  @Value("${PARTIAL_SUCCESS_SKUS}")
  private String partialSuccessSkus;

  @Value("${INVALID_CHANNEL_API_REQUEST_TYPE}")
  private String invalidChannelApiRequestType;

  @Value("${INVALID_INVENTORY_SOURCE}")
  private String invalidInventorySource;

  @Value("${INVALID_QTY}")
  private String invalidQty;

  @Value("${CHANNEL_SKU_REQUEST_FULFILLED}")
  private String channelSkuRequestFulfilled;

  @Value("${SALES_ORDER_NUMBER_FIELD_IS_REQUIRED}")
  private String salesOrderNumberFieldIsRequired;

  @Value("${SALES_ORDER_NUMBER_NOT_BLANK}")
  private String salesOrderNumberNotBlank;

  @Value("${INVALID_WMS_INVENTORY_SOURCE}")
  private String invalidWmsInventorySource;

  @Value("${ORDER_TYPE_FIELD_IS_REQUIRED}")
  private String orderTypeFieldIsRequired;

  @Value("${WMS_INVENTORY_SOURCE}")
  private String wmsInventorySource;

  @Value("${CHANNEL_MFG_SKU_REQUEST_FULFILLED}")
  private String channelMfgSkuRequestFulfilled;

  @Value("${ACTION_FIELD_IS_REQUIRED}")
  private String actionFieldIsRequired;

  @Value("${INVENTORY_SOURCE_FIELD_IS_REQUIRED}")
  private String inventorySourceFieldIsRequired;

  @Value("${SHIPDATE_IS_NOT_SPECIFIED_OR_EMPTY}")
  private String shipDateIsNotSpecifiedOrEmpty;

  @Value("${AVAILABLE_QTY_LESS_THAN_REQUESTED}")
  private String availableQtyLessThanRequested;

  @Value("${ITEM_NOT_FOUND}")
  private String itemNotFound;

  @Value("${INCORRECT_RESULT_SIZE_DATA_ACCESS_EXCEPTION}")
  private String incorrectResultSizeDataAccessException;

  /**
   * @param channelAPI    - the channelAPI, from which channel you want to process
   * @param channelAPISku - the channelAPISku, these are all the SKU level fields
   *                      which you want to process
   * @throws Exception - on failure, an exception is thrown and a meaningful error
   *                   message is displayed.
   *                   <p>
   *                   This method is used to process request for inventory updation based on
   *                   channel and action parameters
   */
  private SkuInventory processRequest(ChannelAPI channelAPI, ChannelAPISku channelAPISku) throws Exception {
    String channel = channelAPI.getChannel();
    String skuBarcode = channelAPISku.getSkuBarcode();
    String season = channelAPISku.getSeason();
    String seasonYear = channelAPISku.getSeasonYear();
    String style = channelAPISku.getStyle();
    String styleSfx = channelAPISku.getStyleSfx();
    String color = channelAPISku.getColor();
    String colorSfx = channelAPISku.getColorSfx();
    String secDimension = channelAPISku.getSecDimension();
    String quality = channelAPISku.getQuality();
    String sizeRngeCode = channelAPISku.getSizeRngeCode();
    String sizeRelPosnIn = channelAPISku.getSizeRelPosnIn();
    String inventoryType = channelAPISku.getInventoryType();
    String lotNumber = channelAPISku.getLotNumber();
    String countryOfOrigin = channelAPISku.getCountryOfOrigin();
    String productStatus = channelAPISku.getProductStatus();
    String skuAttribute1 = channelAPISku.getSkuAttribute1();
    String skuAttribute2 = channelAPISku.getSkuAttribute2();
    String skuAttribute3 = channelAPISku.getSkuAttribute3();
    String skuAttribute4 = channelAPISku.getSkuAttribute4();
    String skuAttribute5 = channelAPISku.getSkuAttribute5();
    String action = channelAPISku.getAction();
    int qty = channelAPISku.getQty();
    LocalDateTime dateTimeStamp = channelAPI.getDateTimeStamp();
    String user = channelAPI.getUser();
    LocalDate shipDate = channelAPISku.getShipDate();
    String campaignCode = channelAPI.getCampaignCode();

    String company = channelAPISku.getCompany();
    String division = channelAPISku.getDivision();
    String warehouse = channelAPISku.getWarehouse();

    Item item = null;
    SkuInventory skuInventory = null;

    company = company.stripTrailing();
    division = division.stripTrailing();
    warehouse = warehouse.stripTrailing();
    campaignCode = campaignCode.stripTrailing();

    if (skuBarcode.isBlank() && warehouse.isBlank()) {

      item = itemRepository
          .findByCompanyAndDivisionAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn(
              company, division, season, seasonYear, style, styleSfx, color, colorSfx,
              secDimension, quality, sizeRngeCode, sizeRelPosnIn);

      if (item == null) {
        throw new ItemNotFound(itemNotFound);
      }

      ItemKey itemKey = item.getId();
      skuBarcode = itemKey.getSkuBarcode();
      warehouse = itemKey.getWarehouse();
    }

    if (skuBarcode.isBlank()) {

      item = itemRepository
          .findByCompanyAndDivisionAndWarehouseAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn(
              company, division, warehouse, season, seasonYear, style, styleSfx,
              color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);
      if (item == null) {
        throw new ItemNotFound(itemNotFound);
      }

      ItemKey itemKey = item.getId();
      skuBarcode = itemKey.getSkuBarcode();

    }

    if (warehouse.isBlank()) {

      item = itemRepository
          .findByCompanyAndDivisionAndSkuBarcodeAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn(
              company, division, skuBarcode, season, seasonYear, style, styleSfx, color,
              colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);

      if (item == null) {
        throw new ItemNotFound(itemNotFound);
      }

      ItemKey itemKey = item.getId();
      warehouse = itemKey.getWarehouse();
      warehouse = warehouse.stripTrailing();
    }

    // updation of sku_inventory table based on channels and actions
    switch (action.toUpperCase()) {
      case "A": // allocate
        skuInventory = channelAPIValidationService.allocateForChannel(channel, campaignCode, dateTimeStamp, user,
            qty, shipDate, action, company, division, warehouse, skuBarcode, season,
            seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn,
            inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2,
            skuAttribute3, skuAttribute4, skuAttribute5);
        break;
      case "D":// de-allocate

        skuInventory = channelAPIValidationService.deallocateAndUpdateForChannel(channel, campaignCode,
            dateTimeStamp, user, qty, shipDate, action, company, division, warehouse,
            skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality,
            sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus,
            skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5);
        break;
      default:
        throw new Exception("Invalid mode");
    }
    return skuInventory;
  }

  /**
   * @param channelAPI - the channel API which you want to process.
   * @return - on success, returns HTTP status code 200. On failure, returns HTTP
   * status code 400 (bad request).
   * @throws Exception - on failure, an exception is thrown and a meaningful error
   *                   message is displayed.
   *                   <p>
   *                   This method is used to process and validate entire channel
   *                   API request
   */
  public ChannelResponseHistoryHeader updateRequest(ChannelAPI channelAPI) throws Exception {
    ChannelResponseHistoryHeader channelAPIResponseHistoryHeader = null;
    List<ChannelAPIResponseSku> channelAPIResponseSkus = new ArrayList<>();
    String transactionNumber = channelAPI.getTransactionNumber();
    int numberOfFailedSkus = 0;
    int numberOfPassedSkus = 0;
    int numberOfPartialPassedSkus = 0;
    ChannelAPIResponseDetail responseDetail = null;
    String responseId = null;
    char responseCode = 0;

    channelAPIResponseHistoryHeader = channelResponseHistoryHeaderRepository
        .findByTransactionNumber(transactionNumber);

    if (channelAPIResponseHistoryHeader != null) {
      return channelAPIResponseHistoryHeader;
    }

    channelRequestHistoryService.save(channelAPI);

    channelAPIResponseHistoryHeader = new ChannelResponseHistoryHeader();
    channelAPIResponseHistoryHeader.setTransactionNumber(transactionNumber);
    channelAPIResponseHistoryHeader.setChannel(channelAPI.getChannel());
    channelAPIResponseHistoryHeader.setCampaignCode(channelAPI.getCampaignCode());
    channelAPIResponseHistoryHeader.setDateTimeStamp(channelAPI.getDateTimeStamp());
    channelAPIResponseHistoryHeader.setUser(channelAPI.getUser());

    // get all skus from request
    List<ChannelAPISku> channelAPISkus = channelAPI.getMultipleSkus();

    // iterate through every JSON array element and update SkuInventory table
    for (ChannelAPISku sku : channelAPISkus) {
      SkuInventory skuInventory = null;
      ManufacturingInventory manufacturingInventory = null;
      double responseQty = 0;
      double onHandQty = 0;
      double availableQty = 0;
      double allocatedQty = 0;
      double protectedQty = 0;
      double lockedQty = 0;
      boolean headerValidated = false;
      ChannelAPIResponseSku channelAPIResponseSku = new ChannelAPIResponseSku();

      LocalDateTime dateTimeStamp = channelAPI.getDateTimeStamp();
      String company = sku.getCompany();
      String division = sku.getDivision();
      String warehouse = sku.getWarehouse();
      String manufacturingPlantCode = sku.getManufacturingPlantCode();
      String skuBarcode = sku.getSkuBarcode();
      String season = sku.getSeason();
      String seasonYear = sku.getSeasonYear();
      String style = sku.getStyle();
      String styleSfx = sku.getStyleSfx();
      String color = sku.getColor();
      String colorSfx = sku.getColorSfx();
      String secDimension = sku.getSecDimension();
      String quality = sku.getQuality();
      String sizeRngeCode = sku.getSizeRngeCode();
      String sizeRelPosnIn = sku.getSizeRelPosnIn();
      String inventoryType = sku.getInventoryType();
      String lotNumber = sku.getLotNumber();
      String countryOfOrigin = sku.getCountryOfOrigin();
      String salesOrderNumber = sku.getSalesOrderNumber();
      String orderType = sku.getOrderType();
      int qty = sku.getQty();
      String action = sku.getAction();
      String productStatus = sku.getProductStatus();
      String skuAttribute1 = sku.getSkuAttribute1();
      String skuAttribute2 = sku.getSkuAttribute2();
      String skuAttribute3 = sku.getSkuAttribute3();
      String skuAttribute4 = sku.getSkuAttribute4();
      String skuAttribute5 = sku.getSkuAttribute5();
      String inventorySource = sku.getInventorySource();
      LocalDate shipDate = sku.getShipDate();

      channelAPIResponseSku.setCompany(company);
      channelAPIResponseSku.setDivision(division);
      channelAPIResponseSku.setWarehouse(warehouse);
      channelAPIResponseSku.setManufacturingPlantCode(manufacturingPlantCode);
      channelAPIResponseSku.setSeason(season);
      channelAPIResponseSku.setSeasonYear(seasonYear);
      channelAPIResponseSku.setStyle(style);
      channelAPIResponseSku.setStyleSfx(styleSfx);
      channelAPIResponseSku.setColor(color);
      channelAPIResponseSku.setColorSfx(colorSfx);
      channelAPIResponseSku.setSecDimension(secDimension);
      channelAPIResponseSku.setQuality(quality);
      channelAPIResponseSku.setSizeRngeCode(sizeRngeCode);
      channelAPIResponseSku.setSizeRelPosnIn(sizeRelPosnIn);
      channelAPIResponseSku.setLotNumber(lotNumber);
      channelAPIResponseSku.setCountryOfOrigin(countryOfOrigin);
      channelAPIResponseSku.setInventoryType(inventoryType);
      channelAPIResponseSku.setSkuBarcode(skuBarcode);
      channelAPIResponseSku.setProductStatus(productStatus);
      channelAPIResponseSku.setSkuAttribute1(skuAttribute1);
      channelAPIResponseSku.setSkuAttribute2(skuAttribute2);
      channelAPIResponseSku.setSkuAttribute3(skuAttribute3);
      channelAPIResponseSku.setSkuAttribute4(skuAttribute4);
      channelAPIResponseSku.setSkuAttribute5(skuAttribute5);

      channelAPIResponseSku.setInventorySource(inventorySource);
      channelAPIResponseSku.setAction(action);
      channelAPIResponseSku.setQty(qty);
      channelAPIResponseSku.setSalesOrderNumber(salesOrderNumber);
      channelAPIResponseSku.setOrderType(orderType);
      channelAPIResponseSku.setShipDate(shipDate);

      String channel = channelAPI.getChannel();

      try {

        if (inventorySource == null) {
          responseCode = f;
          responseId = inventorySourceFieldIsRequired;
          numberOfFailedSkus++;
          continue;
        }

        // inventory source should be either G or M
        if (!request.validateInventorySource(inventorySource)) {
          responseCode = f;
          responseId = invalidInventorySource;
          numberOfFailedSkus++;
          continue;
        }

        // perform validation only once for efficiency reasons
        if (!headerValidated) {
          wmsInventoryKeyValidationService.validate(channel);
          wmsInventoryKeyValidationService.validate(dateTimeStamp);
          if (channel.toUpperCase().equals("WEB")) {
            if (!request.validateWmsInventorySource(inventorySource)) {
              responseCode = f;
              responseId = invalidWmsInventorySource;
              numberOfFailedSkus++;
              continue;
            }
          }

          headerValidated = true;
        }

        if (inventorySource.equals(wmsInventorySource)) {
          campaignValidationService.campaignValidate(channelAPI.getCampaignCode());
        }

        // channelValidationService.findByChannelAndCompany(new ChannelKey(channel),
        // company);

        if (salesOrderNumber == null) {
          responseCode = f;
          responseId = salesOrderNumberFieldIsRequired;
          numberOfFailedSkus++;
          continue;
        }

        if (salesOrderNumber.isBlank()) {
          responseCode = f;
          responseId = salesOrderNumberNotBlank;
          numberOfFailedSkus++;
          continue;
        }

        if (shipDate == null) {
          responseCode = f;
          responseId = shipDateIsNotSpecifiedOrEmpty;
          numberOfFailedSkus++;
          continue;
        }

        if (orderType == null) {
          responseCode = f;
          responseId = orderTypeFieldIsRequired;
          numberOfFailedSkus++;
          continue;
        }

        // validate quantity - should be > 0
        if (!request.isQtyGreaterThanZero(qty)) {
          responseCode = f;
          responseId = invalidQty;
          numberOfFailedSkus++;
          continue;
        }

        if (action == null) {
          responseCode = f;
          responseId = actionFieldIsRequired;
          numberOfFailedSkus++;
          continue;
        }

        // validate mode - should be either A, D, R, M
        if (!request.validateChannelAction(action)) {
          responseCode = f;
          responseId = invalidChannelApiRequestType;
          numberOfFailedSkus++;
          continue;
        }

        SKUInventoryKey skuInventoryKey = new SKUInventoryKey(company, division, warehouse, skuBarcode,
            season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode,
            sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1,
            skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5);

        ManufacturingInventoryKey manufacturingInventoryKey = new ManufacturingInventoryKey(company,
            division, manufacturingPlantCode, skuBarcode, season, seasonYear, style, styleSfx, color,
            colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber,
            countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4,
            skuAttribute5);

        //process quantity updation request of inventory tables based on the Inventory source
        if (inventorySource.equals(wmsInventorySource)) {
          try {
            // validate the wms inventory key fields
            wmsInventoryKeyValidationService.validate(skuInventoryKey);
          } catch (SkuBarcodeCannotBeBlank ex) {
          }


          // update the sku_inventory table
          skuInventory = processRequest(channelAPI, sku);
          numberOfPassedSkus++;
          responseQty = sku.getQty();
          onHandQty = skuInventory.getOnHandQuantity();
          allocatedQty = skuInventory.getAllocatedQuantity();
          protectedQty = skuInventory.getProtectedQuantity();
          lockedQty = skuInventory.getLockedQuantity();
          availableQty = skuInventory.getAvailableQuantity();
          responseCode = s;
          responseId = channelSkuRequestFulfilled;
        } else {

          // validate the mfg inventory key fields
          try {
            manufacturingInventoryKeyValidationService.validate(manufacturingInventoryKey);
          } catch (SkuBarcodeCannotBeBlank ex) {
          }

          // update the manufacturing_inventory table
          manufacturingInventory = manufacturingInventoryService.validate(manufacturingInventoryKey);
          manufacturingInventory = manufacturingInventoryService.updateMfg(manufacturingInventory,
              salesOrderNumber, action, qty);

          // success response
          numberOfPassedSkus++;
          responseQty = sku.getQty();
          onHandQty = manufacturingInventory.getOnHandQuantity();
          allocatedQty = manufacturingInventory.getAllocatedQuantity();
          protectedQty = manufacturingInventory.getProtectedQuantity();
          lockedQty = manufacturingInventory.getLockedQuantity();
          availableQty = manufacturingInventory.getAvailableQuantity();

          responseCode = s;
          responseId = channelMfgSkuRequestFulfilled;

        } // partial success response
      } catch (AvailableQtyLessThanRequestedQty ex) {
        numberOfPartialPassedSkus++;
        responseQty = Double.parseDouble(ex.getResponseQty());
        onHandQty = Double.parseDouble(ex.getOnHandQty());
        allocatedQty = Double.parseDouble(ex.getAllocatedQty());
        protectedQty = Double.parseDouble(ex.getProtectedQty());
        lockedQty = Double.parseDouble(ex.getLockedQty());
        availableQty = Double.parseDouble(ex.getAvailableQty());
        responseCode = p;
        responseId = ex.getMessage();
      } catch (AllocatedQtyIsLessThanRequestedQty ex) {
        numberOfPartialPassedSkus++;
        responseQty = Double.parseDouble(ex.getResponseQty());
        onHandQty = Double.parseDouble(ex.getOnHandQty());
        allocatedQty = Double.parseDouble(ex.getAllocatedQty());
        protectedQty = Double.parseDouble(ex.getProtectedQty());
        lockedQty = Double.parseDouble(ex.getLockedQty());
        availableQty = Double.parseDouble(ex.getAvailableQty());
        responseCode = p;
        responseId = ex.getMessage();
      } catch (AvailableQtyAndProtectQtyZero ex) {
        numberOfFailedSkus++;
        responseQty = Double.parseDouble(ex.getResponseQty());
        onHandQty = Double.parseDouble(ex.getOnHandQty());
        allocatedQty = Double.parseDouble(ex.getAllocatedQty());
        protectedQty = Double.parseDouble(ex.getProtectedQty());
        lockedQty = Double.parseDouble(ex.getLockedQty());
        availableQty = Double.parseDouble(ex.getAvailableQty());
        responseCode = f;
        responseId = ex.getMessage();
      } catch (AvailableQtyZero ex) {
        numberOfFailedSkus++;
        responseQty = Double.parseDouble(ex.getResponseQty());
        onHandQty = Double.parseDouble(ex.getOnHandQty());
        allocatedQty = Double.parseDouble(ex.getAllocatedQty());
        protectedQty = Double.parseDouble(ex.getProtectedQty());
        lockedQty = Double.parseDouble(ex.getLockedQty());
        availableQty = Double.parseDouble(ex.getAvailableQty());
        responseCode = f;
        responseId = ex.getMessage();
      } catch (AllocatedQtyZero ex) {
        numberOfFailedSkus++;
        responseQty = Double.parseDouble(ex.getResponseQty());
        onHandQty = Double.parseDouble(ex.getOnHandQty());
        allocatedQty = Double.parseDouble(ex.getAllocatedQty());
        protectedQty = Double.parseDouble(ex.getProtectedQty());
        lockedQty = Double.parseDouble(ex.getLockedQty());
        availableQty = Double.parseDouble(ex.getAvailableQty());
        responseCode = f;
        responseId = ex.getMessage();
      } catch (IncorrectResultSizeDataAccessException exception) {
        numberOfFailedSkus++;
        responseCode = f;
        responseId = incorrectResultSizeDataAccessException;
      } catch (Exception exception) { // failure response
        numberOfFailedSkus++;
        responseCode = f;
        responseId = exception.getMessage();
      } finally {

        channelAPIResponseSku.setResponseQty(responseQty);
        channelAPIResponseSku.setOnHandQty(onHandQty);
        channelAPIResponseSku.setAllocatedQty(allocatedQty);
        channelAPIResponseSku.setProtectedQty(protectedQty);
        channelAPIResponseSku.setLockedQty(lockedQty);
        channelAPIResponseSku.setAvailableQty(availableQty);

        ChannelAPIResponseDetail channelAPIResponseDetail = new ChannelAPIResponseDetail();
        channelAPIResponseDetail.setResponseCode(responseCode);
        channelAPIResponseDetail.setResponseId(responseId);
        channelAPIResponseSku.setChannelAPIResponseDetail(channelAPIResponseDetail);
        channelAPIResponseSkus.add(channelAPIResponseSku);
      }

    }

    responseDetail = new ChannelAPIResponseDetail();

    if (numberOfPartialPassedSkus != 0) {
      responseId = partialSuccessSkus;
      responseCode = p;
      responseDetail.setResponseCode(responseCode);
      responseDetail.setResponseId(responseId);
    } else {
      if (numberOfFailedSkus == 0) {
        responseId = successSkus;
        responseCode = s;
        responseDetail.setResponseCode(responseCode);
        responseDetail.setResponseId(responseId);
      } else {
        if (numberOfPassedSkus == 0) {
          responseId = failureSkus;
          responseCode = f;
          responseDetail.setResponseCode(responseCode);
          responseDetail.setResponseId(responseId);
        } else {
          responseId = partialSuccessSkus;
          responseCode = p;
          responseDetail.setResponseCode(responseCode);
          responseDetail.setResponseId(responseId);
        }
      }
    }
    channelAPIResponseHistoryHeader.setResponseDetail(responseDetail);

    return channelResponseHistoryService.save(channelAPIResponseHistoryHeader, channelAPI,
        channelAPIResponseSkus);
  }
}