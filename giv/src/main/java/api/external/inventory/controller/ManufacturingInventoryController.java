
package api.external.inventory.controller;

import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.inventory.service.ManufacturingInventoryService;
import api.external.mfg.entity.MfgInventorySearchCriteria;
import api.external.mfg.service.ManufacturingInventorySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages Read operations of manufacturing_inventory.
 *
 * @author Thamilarasi
 * @version : 1.0
 * @since : 2019-07-08
 */

@RestController
public class ManufacturingInventoryController {
    @Autowired
    private ManufacturingInventoryService manufacturingInventoryService;

    @Autowired
    private ManufacturingInventorySearchService manufacturingInventorySearchService;

    /**
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param sortBy   - default is lastUpdatedDateTime, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns a page of skus in the manufacturing inventory.
     * @throws Exception - on failure, an exception is thrown and a meaningful error message
     *                   is displayed.
     */
    @GetMapping("/mfgs")
    public Page<ManufacturingInventory> getAllManufacturingInventories(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                       @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy, @RequestParam(defaultValue = "D") String orderBy) throws Exception {
        return manufacturingInventoryService.getAllManufacturingInventory(pageNo, pageSize, sortBy, orderBy);
    }

    /**
     * @param manufacturingInventoryKey - combined primary key by which you want to find the skus in the manufacturing inventory in the db.
     * @return - on success, returns a page of skus in the manufacturing inventory.
     * @throws Exception - on failure, an exception is thrown and a meaningful error message
     *                   is displayed.
     */
    @GetMapping("/mfgs/{manufacturingInventoryKey}")
    public ManufacturingInventory getManufacturingInventory(@PathVariable ManufacturingInventoryKey manufacturingInventoryKey) throws Exception {
        return manufacturingInventoryService.getManufacturingInventoryById(manufacturingInventoryKey);
    }

    /**
     * @param mfgInventorySearchCriteria - search parameters to find the sku in the manufacturing inventory
     * @param pageNo                     - default is 0, can be overridden by caller.
     * @param pageSize                   - default is 10, can be overridden by caller.
     * @param orderBy                    - default is descending, can be overridden by caller.
     * @return - on success, returns a page of skus in the manufacturing inventory.
     */
    @PostMapping("/mfginventorysearch")
    public Page<ManufacturingInventory> searchInventory(@RequestBody MfgInventorySearchCriteria mfgInventorySearchCriteria,
                                                        @RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "D") String orderBy) {
        return manufacturingInventorySearchService.searchMfgInventory(mfgInventorySearchCriteria, pageNo, pageSize, orderBy);
    }
}

