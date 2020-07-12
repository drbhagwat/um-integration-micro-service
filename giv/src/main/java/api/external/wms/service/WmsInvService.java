package api.external.wms.service;

import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.SkuInventoryService;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import api.external.item.entity.ItemKey;
import api.external.item.service.ItemService;
import api.external.wms.entity.ResponseDetail;
import api.external.wms.entity.WmsInvRequest;
import api.external.wms.entity.WmsRequestDetail;
import api.external.wms.entity.WmsRequestHeader;
import api.external.wms.entity.WmsResponseDetail;
import api.external.wms.entity.WmsResponseHeader;
import api.external.wms.entity.WmsSku;
import api.external.wms.repo.WmsRequestHeaderRepository;
import api.external.wms.repo.WmsResponseHeaderRepository;
import api.external.wms.validation.WmsValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class provides various services for wms inventory.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2019-05-02
 */

@Service
@Transactional
public class WmsInvService {
    @Value("${FAILURE_RESPONSE}")
    private char failure;

    @Value("${SUCCESS_RESPONSE}")
    private char success;

    @Value("${PARTIAL_RESPONSE}")
    private char partialSuccess;

    @Value("${SUCCESS_SKUS}")
    private String successSkus;

    @Value("${FAILURE_SKUS}")
    private String failureSkus;

    @Value("${PARTIAL_SUCCESS_SKUS}")
    private String partialSuccessSkus;

    @Value("${ACTION_MANDATORY}")
    private String actionMandatory;

    @Value("${SERIAL_NUMBER_MANDATORY}")
    private String serialNumberMandatory;

    @Value("${INVENTORY_SOURCE_MANDATORY}")
    private String inventorySourceMandatory;

    @Autowired
    private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SkuInventoryService skuInventoryService;

    @Autowired
    private WmsResponseHeaderRepository wmsResponseHeaderRepository;

    @Autowired
    private WmsValidationService wmsValidationService;

    @Autowired
    private WmsRequestHeaderRepository wmsRequestHeaderRepository;

    // This method adds/subtracts/locks/unlocks sku in/from wms inventory
    public WmsResponseHeader process(WmsInvRequest wmsInvRequest) {
        String transactionNumber = wmsInvRequest.getTransactionNumber();
        WmsResponseHeader wmsResponseHeader = wmsResponseHeaderRepository.findByTransactionNumber(transactionNumber);

        if (wmsResponseHeader != null) {
            return wmsResponseHeader;
        }

        List<WmsRequestDetail> wmsRequestDetails = new ArrayList<>();
        wmsInvRequest.getMultipleSkus().forEach(sku -> {
            SKUInventoryKey key = sku.getId();
            wmsRequestDetails.add(new WmsRequestDetail(null, transactionNumber, wmsInvRequest.getDateTimeStamp(),
                    wmsInvRequest.getUser(), key.getCompany(), key.getDivision(), key.getWarehouse(), key.getSkuBarcode(),
                    key.getSeason(), key.getSeasonYear(), key.getStyle(), key.getStyleSfx(), key.getColor(), key.getColorSfx(),
                    key.getSecDimension(), key.getQuality(), key.getSizeRngeCode(), key.getSizeRelPosnIn(), key.getInventoryType(),
                    key.getLotNumber(), key.getCountryOfOrigin(), key.getProductStatus(), key.getSkuAttribute1(), key.getSkuAttribute2(),
                    key.getSkuAttribute3(), key.getSkuAttribute4(), key.getSkuAttribute5(), sku.getSerialNumber(), sku.getQty(),
                    sku.getAction(), sku.getInventorySource(), sku.getReasonCode(), sku.getDesc()));
        });

        // Save everything to request history.
        wmsRequestHeaderRepository.save(new WmsRequestHeader(transactionNumber, wmsInvRequest.getDateTimeStamp(),
                wmsInvRequest.getUser(), wmsRequestDetails));

        // Every response sku will be added to this list.
        List<WmsResponseDetail> wmsResponseDetailList = new ArrayList<>();

        // This is header level response object and assuming it as success initially.
        ResponseDetail headerResponseDetail = new ResponseDetail(success, successSkus);

        // These fields are used to calculate overall response
        int successResponse = 0;
        int partialResponse = 0;
        int failedResponse = 0;

        List<WmsSku> skuList = wmsInvRequest.getMultipleSkus();
        for (WmsSku sku : skuList) {
            SKUInventoryKey key = sku.getId();

            // Right trimming if company, division and warehouse are not null as json may contain some whitespaces at the end of these fields
            Optional.ofNullable(key.getCompany()).ifPresent(company -> key.setCompany(company.stripTrailing()));
            Optional.ofNullable(key.getDivision()).ifPresent(division -> key.setDivision(division.stripTrailing()));
            Optional.ofNullable(key.getWarehouse()).ifPresent(warehouse -> key.setWarehouse(warehouse.stripTrailing()));

            String company = key.getCompany();
            String division = key.getDivision();
            String warehouse = key.getWarehouse();
            String skuBarcode = key.getSkuBarcode();
            String season = key.getSeason();
            String seasonYear = key.getSeasonYear();
            String style = key.getStyle();
            String styleSfx = key.getStyleSfx();
            String color = key.getColor();
            String colorSfx = key.getColorSfx();
            String secDimension = key.getSecDimension();
            String quality = key.getQuality();
            String sizeRangeCode = key.getSizeRngeCode();
            String sizeRelPositionIn = key.getSizeRelPosnIn();
            String serialNumber = sku.getSerialNumber();
            String inventorySource = sku.getInventorySource();
            String action = sku.getAction();
            long qty = sku.getQty();

            // This is the sku level response object
            ResponseDetail responseDetail = new ResponseDetail();

            // Set all fields to sku level response object
            WmsResponseDetail wmsResponseDetail = new WmsResponseDetail(null, transactionNumber, wmsInvRequest.getDateTimeStamp(),
                    wmsInvRequest.getUser(), company, division, warehouse, skuBarcode, season, seasonYear, style, styleSfx,
                    color, colorSfx, secDimension, quality, sizeRangeCode, sizeRelPositionIn, key.getInventoryType(),
                    key.getLotNumber(), key.getCountryOfOrigin(), key.getProductStatus(), key.getSkuAttribute1(),
                    key.getSkuAttribute2(), key.getSkuAttribute3(), key.getSkuAttribute4(), key.getSkuAttribute5(), serialNumber,
                    qty, 0, 0, 0, 0, 0, 0, action,
                    inventorySource, sku.getReasonCode(), sku.getDesc(), responseDetail);

            // Add sku to the response list
            wmsResponseDetailList.add(wmsResponseDetail);

            try {
                // Validating json key fields
                skuInventoryKeyValidationService.validate(key);
                wmsValidationService.validateSerialNumber(serialNumber);
                wmsValidationService.validateWMSAction(action);
                wmsValidationService.validateWmsInventorySource(inventorySource);
                wmsValidationService.validateQty(qty);

                // Checking item before performing the action.
                itemService.getItem(new ItemKey(company, division, warehouse, skuBarcode, season, seasonYear, style,
                        styleSfx, color, colorSfx, secDimension, quality, sizeRangeCode, sizeRelPositionIn));

                // Perform the action (add/subtract/lock/unlock)
                SkuInventory skuInventory = skuInventoryService.perform(key, wmsInvRequest, sku, wmsResponseDetail);

                if (responseDetail.getResponseCode().equals(success)) {
                    wmsResponseDetail.setResponseQty(qty);
                    successResponse++;
                } else {
                    partialResponse++;
                }

                // Set Allocated, OnHand, Protected, Locked, and Available Quantity after performing the request
                wmsResponseDetail.setOnhandQty(skuInventory.getOnHandQuantity());
                wmsResponseDetail.setAllocatedQty(skuInventory.getAllocatedQuantity());
                wmsResponseDetail.setProtectedQty(skuInventory.getProtectedQuantity());
                wmsResponseDetail.setLockedQty(skuInventory.getLockedQuantity());
                wmsResponseDetail.setAvailableQty(skuInventory.getAvailableQuantity());
            } catch (Exception exception) {
                failedResponse++;
                responseDetail.setResponseCode(failure);
                responseDetail.setResponseId(exception.getMessage());
            }
        }

        // Overall response may change here
        if (successResponse == 0 && partialResponse == 0) {
            headerResponseDetail.setResponseId(failureSkus);
            headerResponseDetail.setResponseCode(failure);
        } else if (successResponse != 0 && failedResponse != 0) {
            headerResponseDetail.setResponseId(partialSuccessSkus);
            headerResponseDetail.setResponseCode(partialSuccess);
        }
        return wmsResponseHeaderRepository.save(new WmsResponseHeader(transactionNumber, wmsInvRequest.getDateTimeStamp(),
                wmsInvRequest.getUser(), wmsResponseDetailList, headerResponseDetail));
    }
}