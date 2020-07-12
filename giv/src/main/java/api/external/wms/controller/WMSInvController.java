package api.external.wms.controller;

import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.SkuInventoryService;
import api.external.wms.entity.WmsInvRequest;
import api.external.wms.entity.WmsResponseHeader;
import api.external.wms.service.WmsInvService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides various services for wms inventory.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2019-05-02
 */

@Slf4j
@RestController
public class WMSInvController {
    @Autowired
    private WmsInvService wmsInvService;

    @Autowired
    private SkuInventoryService skuInventoryService;

    /**
     * This API is used to retrieve all the sku resources in the form of page.
     *
     * @param pageNo   This is the first parameter to specify page number
     * @param pageSize This is the second parameter to specify page size
     * @param sortBy   This is the third parameter to specify page sorting order
     * @param orderBy  This is the fourth parameter to specify page ordering
     * @return Page<SkuInventory> This returns all the sku resources.
     */
    @GetMapping("/wmsinventory")
    public Page<SkuInventory> getAllWmsInv(@RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                                           @RequestParam(defaultValue = "D") String orderBy) {
        return skuInventoryService.getAllWmsInv(pageNo, pageSize, sortBy, orderBy);
    }

    /**
     * This API is used to retrieve a single Sku based on a composite primary key mentioned below.
     *
     * @param companyKey      - id of the company
     * @param divisionKey     - id of the division
     * @param warehouseKey    - id of the warehouse
     * @param skuBarcodeKey   - represents the skuBarcode (usually a number)
     * @param season          - represents the season (winter or summer)
     * @param seasonYear      - represents the seasonYear (2019)
     * @param style           - represents the style of the item
     * @param styleSfx        - represents the style suffix of the item (if any)
     * @param color           - represents the color of the item
     * @param colorSfx        - represents the color suffix of the item (if any)
     * @param secDimension    - represents if the item is a second sale item
     * @param quality         - represents the quality of the item
     * @param sizeRngeCode    - represents the size range code
     * @param sizeRelPosnIn   - represents the size relative position in table
     * @param inventoryType   - represents the inventory type code
     * @param lotNumber       - represents the lot number in table
     * @param countryOfOrigin - represents the country of origin of the item
     * @param productStatus   - represents the product status of the item (if any)
     * @param skuAttribute1   - represents the skuAttribute1 of the item (if any)
     * @param skuAttribute2   - represents the skuAttribute2 of the item (if any)
     * @param skuAttribute3   - represents the skuAttribute3 of the item (if any)
     * @param skuAttribute4   - represents the skuAttribute4 of the item (if any)
     * @param skuAttribute5   - represents the skuAttribute5 of the item (if any)
     * @return SkuInventory  - This returns an SkuInventory based on criteria.
     * @throws Exception - if the SkuInventory does not exists
     */
    @GetMapping("/wmsinventory/{companyKey},{divisionKey},{warehouseKey},{skuBarcodeKey},{season},{seasonYear},{style}," +
            "{styleSfx},{color},{colorSfx},{secDimension},{quality},{sizeRngeCode},{sizeRelPosnIn},{inventoryType},{lotNumber}," +
            "{countryOfOrigin},{productStatus},{skuAttribute1},{skuAttribute2},{skuAttribute3},{skuAttribute4},{skuAttribute5}")
    public SkuInventory getWmsInv(@PathVariable String companyKey, @PathVariable String divisionKey,
                                  @PathVariable String warehouseKey, @PathVariable String skuBarcodeKey,
                                  @PathVariable String season, @PathVariable String seasonYear,
                                  @PathVariable String style, @PathVariable String styleSfx,
                                  @PathVariable String color, @PathVariable String colorSfx,
                                  @PathVariable String secDimension, @PathVariable String quality,
                                  @PathVariable String sizeRngeCode, @PathVariable String sizeRelPosnIn,
                                  @PathVariable String inventoryType, @PathVariable String lotNumber,
                                  @PathVariable String countryOfOrigin, @PathVariable String productStatus,
                                  @PathVariable String skuAttribute1, @PathVariable String skuAttribute2,
                                  @PathVariable String skuAttribute3, @PathVariable String skuAttribute4,
                                  @PathVariable String skuAttribute5) throws Exception {
        return skuInventoryService.validateSku(new SKUInventoryKey(companyKey, divisionKey, warehouseKey, skuBarcodeKey,
                season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn,
                inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3,
                skuAttribute4, skuAttribute5));
    }

    /**
     * This API is used to add/subtract/lock/unlock sku to/from wms inventory.
     *
     * @param wmsInvRequest This is the request body of wms inventory
     * @return WmsResponseHeader On success it returns HTTP status code 200.
     * @throws Exception On failure, an exception is thrown and a meaningful error message is displayed with HTTP
     *                   status code 400 (bad request).
     */
    @PostMapping("/wmsinventory")
    public ResponseEntity<WmsResponseHeader> wmsinv(@RequestBody @Valid WmsInvRequest wmsInvRequest) throws Exception {
        log.info("Entered into wmsinv : {} ", wmsInvRequest);
        return ResponseEntity.ok(wmsInvService.process(wmsInvRequest));
    }
}