package api.core.controllers;

import api.core.entities.ManufacturingPlant;
import api.core.entities.ManufacturingPlantKey;
import api.core.services.ManufacturingPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Manages CRUD (Create Read Update Delete) operations of ManufacturingPlant.
 *
 * @author : Thamilarasi
 * @version : 2.0
 * @since : 2019-06-13
 * @since : 2019-11-27
 */
@RestController
public class ManufacturingPlantController {
  @Value("${MANUFACTURING_PLANT_ADDED}")
  private String mfgPlantAdded;

  @Value("${MANUFACTURING_PLANT_UPDATED}")
  private String mfgPlantUpdated;

  @Value("${MANUFACTURING_PLANT_DELETED}")
  private String mfgPlantDeleted;

  @Autowired
  private ManufacturingPlantService manufacturingPlantService;
  
  /**
   * Gets the first page of manufacturingPlants found (empty when there is no manufacturingPlant).
   *
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the manufacturingPlants found.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/manufacturingplants")
  public Page<ManufacturingPlant> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                                         @RequestParam(defaultValue = "D") String orderBy) throws Exception {
    return manufacturingPlantService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * Gets a specific manufacturingPlant matched with the Primary Key(PK).
   * 
   * @param companyCode - primary key of the company
   * @param manufacturingPlantCode - primary key of the manufacturingPlant
   * @return - on success, returns the found manufacturingPlant.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/manufacturingplants/{companyCode},{manufacturingPlantCode}")
  public ManufacturingPlant get(@PathVariable String companyCode,
                                @PathVariable String manufacturingPlantCode) throws Exception {
    return manufacturingPlantService.get(new ManufacturingPlantKey(companyCode, manufacturingPlantCode));
  }

  /**
   * Add a manufacturingPlant
   * 
   * @param manufacturingPlant - manufacturingPlant to be added
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PostMapping("/manufacturingplants")
  public ResponseEntity<String> add(@RequestBody @Valid ManufacturingPlant manufacturingPlant) throws Exception {
    manufacturingPlantService.add(manufacturingPlant);
    return ResponseEntity.ok(mfgPlantAdded);
  }

  /**
   * Updates an existing manufacturingPlant matched with the primary key.
   * 
   * @param companyCode - primary key of the company.
   * @param manufacturingPlantCode - primary key of the manufacturingPlant.
   * @param manufacturingPlant - manufacturingPlant to be updated
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PutMapping("/manufacturingplants/{companyCode},{manufacturingPlantCode}")
  public ResponseEntity<String> update(@PathVariable String companyCode,
                                  @PathVariable String manufacturingPlantCode,
                                  @RequestBody @Valid ManufacturingPlant manufacturingPlant) throws Exception {
    manufacturingPlantService.update(new ManufacturingPlantKey(companyCode, manufacturingPlantCode),
        manufacturingPlant);
    return ResponseEntity.ok(mfgPlantUpdated);
  }

  /**
   * Deletes an existing manufacturingPlant matched with the primary key.
   * 
   * @param companyCode - primary key of the company
   * @param manufacturingPlantCode - primary key of the manufacturingPlant
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @DeleteMapping("/manufacturingplants/{companyCode},{manufacturingPlantCode}")
  public ResponseEntity<String> delete(@PathVariable String companyCode,
                                  @PathVariable String manufacturingPlantCode) throws Exception {
    manufacturingPlantService.delete(new ManufacturingPlantKey(companyCode, manufacturingPlantCode));
    return ResponseEntity.ok(mfgPlantDeleted);
  }
}