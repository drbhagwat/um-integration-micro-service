
package api.external.mfg.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.inventory.service.ManufacturingInventoryService;
import api.external.mfg.entity.MfgInvRequest;
import api.external.mfg.history.entity.MfgResponseHistoryHeader;
import api.external.mfg.service.MfgInvService;

/**
 * Manages (Create, Read and Update) operations of manufacturing_inventory
 * table.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-03
 */

@RestController
public class MFGInvController {

	@Autowired
	private MfgInvService mfgInvService;

	@Autowired
	private ManufacturingInventoryService manufacturingInventoryService;

	/**
	 *
	 * @param pageNo   - default is 0, can be overridden by caller.
	 * @param pageSize - default is 10, can be overridden by caller.
	 * @param sortBy   - default is lastUpdatedDateTime, can be overridden by
	 *                 caller.
	 * @param orderBy  - default is descending, can be overridden by caller.
	 * @return - on success, returns a page of existing manufacturing_inventory in
	 *         the db. otherwise, the global rest exception handler is automatically
	 *         called and a meaningful error message is displayed.
	 */

	@GetMapping("/mfginventory")
	public Page<ManufacturingInventory> getAllMfgInventories(@RequestParam(defaultValue = "0") Integer pageNo,

			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return manufacturingInventoryService.getAllManufacturingInventory(pageNo, pageSize, sortBy, orderBy);
	}

	/**
	 * 
	 * @param company,       manufacturingPlantCode, skuBarcode, productStatus,
	 *                       skuAttribute1
	 * @param skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5 - combined
	 *                       primary keys by which you want to find the
	 *                       manufacturing_inventory in the db.
	 * @return on success, it returns the manufacturing inventory found.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */

	 @GetMapping("/mfginventory/{companyKey},{divisionKey},{manufacturingPlantCodeKey},{skuBarcodeKey},{season},{seasonYear},{style}," +
       "{styleSfx},{color},{colorSfx},{secDimension},{quality},{sizeRngeCode},{sizeRelPosnIn},{inventoryType},{lotNumber}," +
       "{countryOfOrigin},{productStatus},{skuAttribute1},{skuAttribute2},{skuAttribute3},{skuAttribute4},{skuAttribute5}")
public ManufacturingInventory getMfgInv(@PathVariable String companyKey, @PathVariable String divisionKey,
                             @PathVariable String manufacturingPlantCodeKey, @PathVariable String skuBarcodeKey,
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
   return manufacturingInventoryService.getManufacturingInventoryById(new ManufacturingInventoryKey(companyKey, divisionKey, manufacturingPlantCodeKey, skuBarcodeKey,
           season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn,
           inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3,
           skuAttribute4, skuAttribute5));
}
	/**
	 * @param mfgInv - the mfgInv, which you want to process and store in the db.
	 * @return - on success, returns HTTP status code 200.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed with HTTP status code 400 (bad
	 *                   request).
	 */

	@PostMapping("/mfginventory")
	public ResponseEntity<MfgResponseHistoryHeader> process(@RequestBody @Valid MfgInvRequest mfgInv) throws Exception {
		return ResponseEntity.ok(mfgInvService.process(mfgInv));
	}
}
