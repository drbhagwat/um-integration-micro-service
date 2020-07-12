package api.core.controllers;

import api.core.entities.Company;
import api.core.errors.*;
import api.core.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * CRUD (Create Read Update Delete) operations for the Company entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-03-05
 */
@RestController
public class CompanyController {
  @Value("${COMPANY_ADDED}")
  private String companyAdded;

  @Value("${COMPANY_UPDATED}")
  private String companyUpdated;

  @Value("${COMPANY_DELETED}")
  private String companyDeleted;

  @Autowired
  private CompanyService companyService;

  /**
   * Retrieves the first page of companies found in the db (when there is no company this will be empty).
   *
   * @param pageNo   - default is 0, can be overridden
   * @param pageSize - default is 10, can be overridden
   * @param sortBy   - default is descending, can be overridden
   * @param orderBy  - default is by last updated date time, can be overridden
   * @return - first page of the companies found
   */
  @GetMapping("/companies")
  public Page<Company> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                              @RequestParam(defaultValue = "D") String orderBy) {
    return companyService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * Retrieves the company whose PK matches the given companyKey.
   *
   * @param companyKey - companyCode - the PK of the company entity
   * @return - on success, returns the found company
   * @throws CompanyNotFound - this exception is thrown when company is not found in the db
   */
  @GetMapping("/companies/{companyKey}")
  public Company get(@PathVariable String companyKey) throws CompanyNotFound {
    return companyService.get(companyKey);
  }

  /**
   * Adds a new company.
   *
   * @param company - the company which is to be added to the db
   * @return - on success, returns the appropriate message
   * @throws CompanyCodeCannotContainSpecialCharacters - The company to be added contains a character which is not
   *                                                   allowed
   * @throws CompanyMaxLengthExceeded                  - The PK of the company to be added exceeds the allowed max
   *                                                   length
   * @throws CompanyAlreadyExists                      - The company to be added already exists in the db
   */
  @PostMapping("/companies")
  public ResponseEntity<String> add(@RequestBody @Valid Company company) throws CompanyCodeCannotContainSpecialCharacters,
      CompanyMaxLengthExceeded, CompanyAlreadyExists {
    companyService.add(company);
    return ResponseEntity.ok(companyAdded);
  }

  /**
   * Updates an existing company.
   *
   * @param companyKey - PK of the company to be updated
   * @param company    - contains the to be modified details
   * @return - on success, returns the appropriate message
   * @throws CompanyNotFound - throws this exception when the company to be updated is not found in the db
   */
  @PutMapping("/companies/{companyKey}")
  public ResponseEntity<String> update(@PathVariable String companyKey, @RequestBody @Valid Company company) throws
      CompanyNotFound {
    companyService.update(companyKey, company);
    return ResponseEntity.ok(companyUpdated);
  }

  /**
   * Deletes an existing company.
   *
   * @param companyKey - PK of the company to be deleted
   * @return - on success, returns the appropriate message
   * @throws CompanyNotFound            - throws this exception when the company is not found
   * @throws DivisionNotFound           - throws this exception when the division in not found while deleting divisions
   * @throws ManufacturingPlantNotFound - throws this exception when the manufacturingPlant is not found while
   *                                    deleting manufacturingplants
   */
  @DeleteMapping("/companies/{companyKey}")
  public ResponseEntity<String> delete(@PathVariable String companyKey) throws
      CompanyNotFound, DivisionNotFound, ManufacturingPlantNotFound {
    companyService.delete(companyKey);
    return ResponseEntity.ok(companyDeleted);
  }
}