package api.core.controllers;

import api.core.entities.Warehouse;
import api.core.entities.WarehouseKey;
import api.core.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Performs CRUD (Create Read Update Delete) operations for Warehouse.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-04-15
 * @since : 2019-11-23
 */
@RestController
public class WarehouseController {
  @Value("${WAREHOUSE_ADDED}")
  private String warehouseAdded;

  @Value("${WAREHOUSE_UPDATED}")
  private String warehouseUpdated;

  @Value("${WAREHOUSE_DELETED}")
  private String warehouseDeleted;

  @Autowired
  private WarehouseService warehouseService;

  /**
   * Gets the first page of found warehouses found (empty when there is no warehouse).
   *
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the warehouses found.
   */
  @GetMapping("/warehouses")
  public Page<Warehouse> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                                @RequestParam(defaultValue = "D") String orderBy) {
    return warehouseService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * Gets the warehouse matching the Primary Key (PK).
   *
   * @param companyKey   - companyKey (can be blank).
   * @param divisionKey  - divisionKey (can be blank).
   * @param warehouseKey - warehouseKey. (companyKey, divisionKey, and warehouseKey form the composite PK of warehouse).
   * @return - on success, returns the found warehouse.
   * @throws Exception on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/warehouses/{companyKey},{divisionKey},{warehouseKey}")
  public Warehouse get(@PathVariable String companyKey, @PathVariable String divisionKey,
                       @PathVariable String warehouseKey) throws Exception {
    return warehouseService.get(new WarehouseKey(companyKey, divisionKey, warehouseKey));
  }

  /**
   * Adds a warehouse.
   *
   * @param warehouse - the warehouse to be added.
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PostMapping("/warehouses")
  public ResponseEntity<String> add(@RequestBody @Valid Warehouse warehouse) throws Exception {
    warehouseService.add(warehouse);
    return ResponseEntity.ok(warehouseAdded);
  }

  /**
   * Updates an existing warehouse.
   *
   * @param companyKey   - companyKey (can be blank).
   * @param divisionKey  - divisionKey (can be blank).
   * @param warehouseKey - warehouseKey. (companyKey, divisionKey, and warehouseKey form the composite PK of warehouse).
   * @param warehouse    - the warehouse to be updated.
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PutMapping("/warehouses/{companyKey},{divisionKey},{warehouseKey}")
  public ResponseEntity<String> update(@PathVariable String companyKey, @PathVariable String divisionKey,
                                  @PathVariable String warehouseKey, @RequestBody @Valid Warehouse warehouse) throws Exception {
    warehouseService.update(new WarehouseKey(companyKey, divisionKey, warehouseKey), warehouse);
    return ResponseEntity.ok(warehouseUpdated);
  }

  /**
   * Deletes an existing warehouse.
   *
   * @param companyKey   - companyKey (can be blank).
   * @param divisionKey  - divisionKey (can be blank).
   * @param warehouseKey - warehouseKey. (companyKey, divisionKey, and warehouseKey form the composite PK of warehouse).
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @DeleteMapping("/warehouses/{companyKey},{divisionKey},{warehouseKey}")
  public ResponseEntity<String> delete(@PathVariable String companyKey, @PathVariable String divisionKey,
                                  @PathVariable String warehouseKey) throws Exception {
    warehouseService.delete(new WarehouseKey(companyKey, divisionKey, warehouseKey));
    return ResponseEntity.ok(warehouseDeleted);
  }
}