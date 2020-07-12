package api.core.controllers;

import api.core.entities.Division;
import api.core.entities.DivisionKey;
import api.core.services.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Performs CRUD (Create Read Update Delete) operations for Division.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-04-15
 * @since : 2019-11-23
 */
@RestController
public class DivisionController {
  @Value("${DIVISION_ADDED}")
  private String divisionAdded;

  @Value("${DIVISION_UPDATED}")
  private String divisionUpdated;

  @Value("${DIVISION_DELETED}")
  private String divisionDeleted;

  @Autowired
  DivisionService divisionService;

  /**
   * Gets the first page of found divisions found (empty when there is no division).
   *
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the divisions found.
   */
  @GetMapping("/divisions")
  public Page<Division> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                               @RequestParam(defaultValue = "D") String orderBy) {
    return divisionService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * Gets the division matching the Primary Key (PK).
   *
   * @param companyKey  - companyKey (can be blank).
   * @param divisionKey - divisionKey (can be blank. companyKey and divisionKey form the composite PK of division).
   * @return - on success, returns the found division.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/divisions/{companyKey},{divisionKey}")
  public Division get(@PathVariable String companyKey, @PathVariable String divisionKey) throws Exception {
    return divisionService.get(new DivisionKey(companyKey, divisionKey));
  }

  /**
   * Adds a division.
   *
   * @param division - the division to be added.
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PostMapping("/divisions")
  public ResponseEntity<String> add(@RequestBody @Valid Division division) throws Exception {
    divisionService.add(division);
    return ResponseEntity.ok(divisionAdded);
  }

  /**
   * Updates an existing division.
   *
   * @param companyKey  - companyKey.
   * @param divisionKey - divisionKey.
   * @param division    - the division to be updated.
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global catch-all exception handler is called
   *                   which displays an appropriate error message.
   */
  @PutMapping("/divisions/{companyKey},{divisionKey}")
  public ResponseEntity<String> update(@PathVariable String companyKey, @PathVariable String divisionKey,
                                  @RequestBody @Valid Division division) throws Exception {
    divisionService.update(new DivisionKey(companyKey, divisionKey), division);
    return ResponseEntity.ok(divisionUpdated);
  }

  /**
   * Deletes an existing division.
   *
   * @param companyKey  - companyKey.
   * @param divisionKey - divisionKey.
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @DeleteMapping("/divisions/{companyKey},{divisionKey}")
  public ResponseEntity<String> delete(@PathVariable String companyKey, @PathVariable String divisionKey) throws Exception {
    divisionService.delete(new DivisionKey(companyKey, divisionKey));
    return ResponseEntity.ok(divisionDeleted);
  }
}