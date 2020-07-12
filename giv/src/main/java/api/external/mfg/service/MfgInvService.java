
package api.external.mfg.service;

import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.inventory.service.ManufacturingInventoryService;
import api.external.inventory.validation.ManufacturingInventoryKeyValidationService;
import api.external.item.entity.Item;
import api.external.item.repo.ItemRepository;
import api.external.mfg.entity.MfgInvRequest;
import api.external.mfg.entity.MfgInvSku;
import api.external.mfg.entity.MfgResponseDetail;
import api.external.mfg.history.entity.MfgResponseHistoryHeader;
import api.external.mfg.history.entity.MfgResponseHistorySku;
import api.external.mfg.history.repo.MfgResponseHistoryHeaderRepository;
import api.external.mfg.history.service.MfgRequestHistoryService;
import api.external.mfg.validation.MfgValidationService;
import api.external.wms.entity.ResponseDetail;
import api.util.Request;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <h1>Creation and Updation of the manufacturing_inventory table</h1> Provides
 * create, read and update services for MFGInv Controller. The MFGInvService
 * implements business logic with one header and multiple Skus and displays
 * corresponding success or failure messages.
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-01
 */

@Service
@Slf4j
@Transactional
public class MfgInvService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ManufacturingInventoryService manufacturingInventoryService;

    @Autowired
    private ManufacturingInventoryKeyValidationService manufacturingInventoryKeyValidationService;

    @Autowired
    private Request request;

    @Autowired
    private MfgRequestHistoryService mfgRequestHistoryService;

    @Autowired
    private MfgResponseHistoryHeaderRepository mfgResponseHistoryHeaderRepository;

    @Autowired
    private MfgValidationService mfgValidationService;

    @Value("${FAILURE_RESPONSE}")
    private char failure;

    @Value("${SUCCESS_RESPONSE}")
    private char success;

    @Value("${PARTIAL_RESPONSE}")
    private char partialSuccess;

    @Value("${INVALID_QTY}")
    private String invalidQty;

    @Value("${INVALID_MFG_INVENTORY_SOURCE}")
    private String invalidMfgInventorySource;

    @Value("${TRANSACTION_NUMBER_MANDATORY}")
    private String transactionNumberMandatory;

    @Value("${TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
    private String transactionNumberCannotBeBlank;

    @Value("${ITEM_NOT_FOUND}")
    private String itemNotFound;

    @Value("${INVENTORY_SOURCE_MANDATORY}")
    private String inventorySourceMandatory;

    @Value("${ACTION_MANDATORY}")
    private String actionMandatory;

    @Value("${INVALID_MFG_ACTION}")
    private String invalidMfgAction;

    @Value("${SERIAL_NUMBER_MANDATORY}")
    private String serialNumberMandatory;

    @Value("${SUCCESS_SKUS}")
    private String successSkus;

    @Value("${FAILURE_SKUS}")
    private String failureSkus;

    @Value("${PARTIAL_SUCCESS_SKUS}")
    private String partialSuccessSkus;

    @Value("${COMPANY_MANDATORY}")
    private String companyMandatory;

    @Value("${DIVISION_MANDATORY}")
    private String divisionMandatory;

   /**
    * Processes entire MfgInventory API Json request
    * 
    * @param mfgInv - mfgInv, which is entire json request of the MfgInventory API
    * @return - on success, returns HTTP status code 200. On failure, returns HTTP
	*         status code 400 (bad request).
	* @throws Exception - on failure, an exception is thrown and a meaningful error
	*                   message is displayed.
    */
    public MfgResponseHistoryHeader process(MfgInvRequest mfgInv) throws Exception {
        log.info("Entered into process : {} ", mfgInv);
        String transactionNumber = mfgInv.getTransactionNumber();
        MfgResponseHistoryHeader mfgResponseHistoryHeader = null;

        try {
            mfgResponseHistoryHeader = mfgResponseHistoryHeaderRepository.findByTransactionNumber(transactionNumber);

            if (mfgResponseHistoryHeader != null) {
                log.info("Returning the existing transaction record : {} ", mfgResponseHistoryHeader);
                return mfgResponseHistoryHeader;
            }

            mfgRequestHistoryService.save(mfgInv);
            mfgResponseHistoryHeader = new MfgResponseHistoryHeader();
            mfgResponseHistoryHeader.setTransactionNumber(transactionNumber);
            mfgResponseHistoryHeader.setDateTimeStamp(mfgInv.getDateTimeStamp());
            mfgResponseHistoryHeader.setUser(mfgInv.getUser());
            mfgValidationService.validate(mfgInv.getProductionOrderNumber(), mfgInv.getCustomerOrderNumber());
            mfgResponseHistoryHeader.setProductionOrderNumber(mfgInv.getProductionOrderNumber());
            mfgResponseHistoryHeader.setPurchaseOrderNumber(mfgInv.getPurchaseOrderNumber());
            mfgResponseHistoryHeader.setCustomerOrderNumber(mfgInv.getCustomerOrderNumber());
            mfgResponseHistoryHeader.setMultipleSkus(validateMfgSku(mfgInv, mfgResponseHistoryHeader));
            log.info("Exit from process : {} ", mfgResponseHistoryHeader);
            return mfgResponseHistoryHeaderRepository.save(mfgResponseHistoryHeader);
        } catch (Exception e) {
            mfgResponseHistoryHeaderRepository.save(mfgResponseHistoryHeader);
            throw new Exception(e.getMessage());
        }
    }

    /*
     * Validates all sku fields of the mfg inventory API comes from the JSON request/payload
     */
    private List<MfgResponseHistorySku> validateMfgSku(MfgInvRequest mfgInv, MfgResponseHistoryHeader mfgResponseHistoryHeader) {
        log.info("Entered into validateMfgSku: {} {} ", mfgInv, mfgResponseHistoryHeader);
        // get all skus from request
        List<MfgInvSku> mfgInvSkuList = mfgInv.getMultipleSkus();
        List<MfgResponseHistorySku> mfgResponseHistorySkuList = new ArrayList<>();
        int successResponse = 0;
        int partialResponse = 0;
        int failedResponse = 0;

        for (MfgInvSku mfgInvSku : mfgInvSkuList) {
            MfgResponseHistorySku mfgResponseHistorySku = new MfgResponseHistorySku();

            // set all quantity fields to zero initially and increase failed response
            mfgResponseHistorySku.setResponseQty(0);
            mfgResponseHistorySku.setAllocatedQty(0);
            mfgResponseHistorySku.setOnhandQty(0);
            mfgResponseHistorySku.setProtectedQty(0);
            mfgResponseHistorySku.setLockedQty(0);
            mfgResponseHistorySku.setAvailableQty(0);
            failedResponse++;

            String company = mfgInvSku.getCompany();
            mfgResponseHistorySku.setCompany(company);

            String division = mfgInvSku.getDivision();
            mfgResponseHistorySku.setDivision(division);

            String manufacturingPlantCode = mfgInvSku.getManufacturingPlantCode();
            mfgResponseHistorySku.setManufacturingPlantCode(manufacturingPlantCode);

            String skuBarcode = mfgInvSku.getSkuBarcode();
            mfgResponseHistorySku.setSkuBarcode(skuBarcode);

            String season = mfgInvSku.getSeason();
            mfgResponseHistorySku.setSeason(season);

            String seasonYear = mfgInvSku.getSeasonYear();
            mfgResponseHistorySku.setSeasonYear(seasonYear);

            String style = mfgInvSku.getStyle();
            mfgResponseHistorySku.setStyle(style);

            String styleSfx = mfgInvSku.getStyleSfx();
            mfgResponseHistorySku.setStyleSfx(styleSfx);

            String color = mfgInvSku.getColor();
            mfgResponseHistorySku.setColor(color);

            String colorSfx = mfgInvSku.getColorSfx();
            mfgResponseHistorySku.setColorSfx(colorSfx);

            String secDimension = mfgInvSku.getSecDimension();
            mfgResponseHistorySku.setSecDimension(secDimension);

            String quality = mfgInvSku.getQuality();
            mfgResponseHistorySku.setQuality(quality);

            String sizeRngeCode = mfgInvSku.getSizeRngeCode();
            mfgResponseHistorySku.setSizeRngeCode(sizeRngeCode);

            String sizeRelPosnIn = mfgInvSku.getSizeRelPosnIn();
            mfgResponseHistorySku.setSizeRelPosnIn(sizeRelPosnIn);

            String inventoryType = mfgInvSku.getInventoryType();
            String lotNumber = mfgInvSku.getLotNumber();
            String countryOfOrigin = mfgInvSku.getCountryOfOrigin();
            String productStatus = mfgInvSku.getProductStatus();
            String skuAttribute1 = mfgInvSku.getSkuAttribute1();
            String skuAttribute2 = mfgInvSku.getSkuAttribute2();
            String skuAttribute3 = mfgInvSku.getSkuAttribute3();
            String skuAttribute4 = mfgInvSku.getSkuAttribute4();
            String skuAttribute5 = mfgInvSku.getSkuAttribute5();

            mfgResponseHistorySku.setInventoryType(inventoryType);
            mfgResponseHistorySku.setLotNumber(lotNumber);
            mfgResponseHistorySku.setCountryOfOrigin(countryOfOrigin);
            mfgResponseHistorySku.setProductStatus(productStatus);
            mfgResponseHistorySku.setSkuAttribute1(skuAttribute1);
            mfgResponseHistorySku.setSkuAttribute2(skuAttribute2);
            mfgResponseHistorySku.setSkuAttribute3(skuAttribute3);
            mfgResponseHistorySku.setSkuAttribute4(skuAttribute4);
            mfgResponseHistorySku.setSkuAttribute5(skuAttribute5);
            mfgResponseHistorySku.setTransactionNumber(mfgInv.getTransactionNumber());
            mfgResponseHistorySku.setProductionOrderNumber(mfgInv.getProductionOrderNumber());
            mfgResponseHistorySku.setCustomerOrderNumber(mfgInv.getCustomerOrderNumber());
            mfgResponseHistorySku.setPurchaseOrderNumber(mfgInv.getPurchaseOrderNumber());
            mfgResponseHistorySku.setDateTimeStamp(mfgInv.getDateTimeStamp());
            mfgResponseHistorySku.setUser(mfgInv.getUser());

            String serialNumber = mfgInvSku.getSerialNumber();
            mfgResponseHistorySku.setSerialNumber(serialNumber);

            String inventorySource = mfgInvSku.getInventorySource();
            mfgResponseHistorySku.setInventorySource(inventorySource);

            String action = mfgInvSku.getAction();
            mfgResponseHistorySku.setAction(action);

            long qty = mfgInvSku.getQty();
            mfgResponseHistorySku.setRequestedQty(qty);

            String reasonCode = mfgInvSku.getReasonCode();
            mfgResponseHistorySku.setReasonCode(reasonCode);

            String desc = mfgInvSku.getDesc();
            mfgResponseHistorySku.setDesc(desc);
            mfgResponseHistorySkuList.add(mfgResponseHistorySku);

            MfgResponseDetail mfgResponseDetail = new MfgResponseDetail();
            mfgResponseDetail.setResponseCode(failure);
            mfgResponseHistorySku.setResponseDetail(mfgResponseDetail);

            ManufacturingInventoryKey manufacturingInventoryKey = new ManufacturingInventoryKey(company, division,
                    manufacturingPlantCode, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension,
                    quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus,
                    skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5);

            try {
                if (company != null) {
                    company = company.stripTrailing();
                    manufacturingInventoryKey.setCompany(company);
                    mfgResponseHistorySku.setCompany(company);
                }

                if (division != null) {
                    division = division.stripTrailing();
                    manufacturingInventoryKey.setDivision(division);
                    mfgResponseHistorySku.setDivision(division);
                }

                if (manufacturingPlantCode != null) {
                    manufacturingPlantCode = manufacturingPlantCode.stripTrailing();
                    manufacturingInventoryKey.setManufacturingPlantCode(manufacturingPlantCode);
                    mfgResponseHistorySku.setManufacturingPlantCode(manufacturingPlantCode);
                }
                manufacturingInventoryKeyValidationService.validate(manufacturingInventoryKey);

                if (action == null || action.isBlank()) {
                    throw new Exception(actionMandatory);
                }

                // Action should be either A or S or L or U
                mfgResponseHistorySku.setAction(request.validateMfgAction(action));

                if (serialNumber == null) {
                    throw new Exception(serialNumberMandatory);
                }

                if (inventorySource == null) {
                    throw new Exception(inventorySourceMandatory);
                }

                if (!request.validateMfgInventorySource(inventorySource)) {
                    throw new Exception(invalidMfgInventorySource);
                }

                if (!request.isQtyGreaterThanZero(qty)) {
                    throw new Exception(invalidQty);
                }

                Item item = itemRepository.findByCompanyAndDivisionAndSkuBarcodeAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn(
                        company, division, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);

                if (item == null) {
                    throw new Exception(itemNotFound);
                }

                ManufacturingInventory manufacturingInventory = manufacturingInventoryService.perform(manufacturingInventoryKey, mfgInv, mfgInvSku,
                        mfgResponseDetail);

                if (mfgResponseDetail.getResponseCode().equals(success) || mfgResponseDetail.getResponseCode().equals(partialSuccess)) {
                    failedResponse--;
                    if (mfgResponseDetail.getResponseCode().equals(success)) {
                        mfgResponseHistorySku.setResponseQty(qty);
                        successResponse++;
                    } else {
                        partialResponse++;
                    }
                }

                if (manufacturingInventory != null) {
                    mfgResponseHistorySku.setOnhandQty((long) manufacturingInventory.getOnHandQuantity());
                    mfgResponseHistorySku.setAllocatedQty((long) manufacturingInventory.getAllocatedQuantity());
                    mfgResponseHistorySku.setProtectedQty((long) manufacturingInventory.getProtectedQuantity());
                    mfgResponseHistorySku.setLockedQty((long) manufacturingInventory.getLockedQuantity());
                    mfgResponseHistorySku.setAvailableQty((long) manufacturingInventory.getAvailableQuantity());
                }
            } catch (Exception exception) {
                mfgResponseDetail.setResponseId(exception.getMessage());
            }
        }
        ResponseDetail headerResponseDetail = new ResponseDetail();
        mfgResponseHistoryHeader.setResponseDetail(headerResponseDetail);

        if (failedResponse == 0 && partialResponse == 0) {
            headerResponseDetail.setResponseId(successSkus);
            headerResponseDetail.setResponseCode(success);
        } else if (successResponse == 0 && partialResponse == 0) {
            headerResponseDetail.setResponseId(failureSkus);
            headerResponseDetail.setResponseCode(failure);
        } else {
            headerResponseDetail.setResponseId(partialSuccessSkus);
            headerResponseDetail.setResponseCode(partialSuccess);
        }
        log.info("Exit from validateMfgSku: {} ", mfgResponseHistorySkuList);
        return mfgResponseHistorySkuList;
    }
}