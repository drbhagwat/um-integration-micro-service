package api.core.services;

import api.core.entities.Company;
import api.core.entities.Division;
import api.core.entities.Warehouse;
import api.core.entities.WarehouseKey;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.WarehouseRepository;
import api.core.validation.WarehouseValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Provides Create Read Update Delete (CRUD) services for Warehouse.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-11-23
 */
@Service
@Transactional
public class WarehouseService {
  @Value("${WAREHOUSE_NOT_FOUND}")
  private String warehouseNotFound;

  @Value("${WAREHOUSE_ALREADY_EXISTS}")
  private String warehouseAlreadyExists;

  @Value("${COMPANY_NOT_FOUND}")
  private String companyNotFound;

  @Value("${DIVISION_NOT_FOUND}")
  private String divisionNotFound;

  @Value("${DIVISION_NOT_FOUND_IN_COMPANY}")
  private String divisionNotFoundInCompany;

  @Autowired
  private WarehouseValidationService warehouseValidationService;

  @Autowired
  private WarehouseRepository warehouseRepository;

  @Autowired
  private CompanyRepository companyRepository;

  /**
   * Gets the first page of found warehouses (empty when there is no warehouse).
   *
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the warehouses found.
   */
  public Page<Warehouse> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return warehouseRepository.findAll(pageable);
  }

  /**
   * Gets the warehouse given the warehouseKey.
   *
   * @param warehouseKey - companyKey, divisionKey, and warehouseKey form the composite PK of
   *                     warehouse).
   * @return - on success, returns the warehouse found in the db.
   * @throws WarehouseNotFound - on failure it throws this exception.
   */
  public Warehouse get(WarehouseKey warehouseKey) throws WarehouseNotFound {
    Optional<Warehouse> warehouse = warehouseRepository.findById(warehouseKey);

    if (warehouse.isEmpty()) {
      throw new WarehouseNotFound(warehouseNotFound);
    }
    return warehouse.get();
  }

  /**
   * Adds a warehouse to the database.
   *
   * @param warehouse - the warehouse to be added.
   * @return - on success, returns the warehouse newly added to the db.
   * @throws CompanyNotFound                             - unable to find the company in the db with the companyCode.
   * @throws DivisionNotFoundInCompany                   - unable to find the division under the company with the given
   *                                                     companyCode
   * @throws DivisionCodeCannotContainSpecialCharacters  - if the divisionCode contains a non-allowed character
   * @throws DivisionMaxLengthExceeded                   - if the length of divisionCode exceeds the max
   *                                                     allowable length
   * @throws CompanyMaxLengthExceeded                    - if the length of companyCode exceeds allowed max length
   * @throws CompanyCodeCannotContainSpecialCharacters   - if the companyCode contains a non-allowed character
   * @throws WarehouseCodeMandatory                      - if the warehouseCode is null
   * @throws WarehouseCodeCannotBeBlank                  - if the warehouseCode is blank
   * @throws WarehouseCodeCannotContainSpecialCharacters - if the warehouseCode contains a non-allowed character
   * @throws WarehouseMaxLengthExceeded                  - if the length of the warehouseCode exceeds the allowed max
   *                                                     length
   * @throws WarehouseAlreadyExists                      - if the warehouseCode exists in db already
   */
  public Warehouse add(Warehouse warehouse) throws CompanyNotFound, DivisionNotFoundInCompany,
      DivisionCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded, CompanyMaxLengthExceeded,
      CompanyCodeCannotContainSpecialCharacters, WarehouseCodeMandatory, WarehouseCodeCannotBeBlank,
      WarehouseCodeCannotContainSpecialCharacters, WarehouseMaxLengthExceeded, WarehouseAlreadyExists {
    final WarehouseKey warehouseKey = warehouse.getId();
    String companyCode = warehouseKey.getCompanyCode();
    Optional<Company> optionalCompany = companyRepository.findById(companyCode);

    if (optionalCompany.isEmpty()) throw new CompanyNotFound(companyNotFound);

    Company existingCompany = optionalCompany.get();
    List<Division> divisions = existingCompany.getDivisions();

    if (divisions.size() == 0) throw new DivisionNotFoundInCompany(divisionNotFoundInCompany);

    Optional<Division> tempDivision =
        divisions.stream().filter(di -> di.getId().getCode().equals(warehouseKey.getDivCode()))
            .findFirst();

    if (tempDivision.isEmpty()) throw new DivisionNotFoundInCompany(divisionNotFoundInCompany);

    WarehouseKey newWarehouseKey = warehouseValidationService.validate(warehouseKey);
    Optional<Warehouse> existingWarehouse = warehouseRepository.findById(newWarehouseKey);

    if (!existingWarehouse.isEmpty()) throw new WarehouseAlreadyExists(warehouseAlreadyExists);

    warehouse.setId(newWarehouseKey);
    return warehouseRepository.save(warehouse);
  }

  /**
   * Updates an existing warehouse in the database.
   *
   * @param warehouseKey - the composite PK - consisting of companyCode, divisionCode, and warehouseCode
   * @param warehouse    - the warehouse to be updated in the db
   * @return - on success, the updated warehouse is returned
   * @throws WarehouseNotFound - on failure, WarehouseNotFound is thrown
   */
  public Warehouse update(WarehouseKey warehouseKey, Warehouse warehouse) throws WarehouseNotFound {
    Warehouse existingWarehouse = get(warehouseKey);

    existingWarehouse.setName(warehouse.getName());
    existingWarehouse.setDescription(warehouse.getDescription());
    existingWarehouse.setAddress1(warehouse.getAddress1());
    existingWarehouse.setAddress2(warehouse.getAddress2());
    existingWarehouse.setCity(warehouse.getCity());
    existingWarehouse.setState(warehouse.getState());
    existingWarehouse.setZip(warehouse.getZip());
    existingWarehouse.setCountry(warehouse.getCountry());
    existingWarehouse.setContactName(warehouse.getContactName());
    existingWarehouse.setContactNumber(warehouse.getContactNumber());
    existingWarehouse.setAlternateAddress1(warehouse.getAlternateAddress1());
    existingWarehouse.setAlternateAddress2(warehouse.getAlternateAddress2());
    existingWarehouse.setAlternateCity(warehouse.getAlternateCity());
    existingWarehouse.setAlternateState(warehouse.getAlternateState());
    existingWarehouse.setAlternateZip(warehouse.getAlternateZip());
    existingWarehouse.setAlternateCountry(warehouse.getAlternateCountry());
    return warehouseRepository.save(existingWarehouse);
  }

  /**
   * Deletes an existing warehouse from the database.
   *
   * @param warehouseKey - PK of the warehouse to be deleted
   * @return - true on success
   * @throws WarehouseNotFound - on failure, throws WarehouseNotFound exception.
   */
  public boolean delete(WarehouseKey warehouseKey) throws WarehouseNotFound {
    warehouseRepository.delete(get(warehouseKey));
    return true;
  }
}