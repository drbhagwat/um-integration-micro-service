package api.core.services;

import api.core.entities.Company;
import api.core.entities.ManufacturingPlant;
import api.core.entities.ManufacturingPlantKey;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.ManufacturingPlantRepository;
import api.core.validation.ManufacturingPlantValidationService;
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
 * Provides Create Read Update Delete (CRUD) services for ManufacturingPlant
 * controller.
 *
 * @author : Dinesh
 * @version : 2.0
 * @since : 2019-02-10
 */

@Service
@Transactional
public class ManufacturingPlantService {
  @Value("${COMPANY_NOT_FOUND}")
  private String companyNotFound;

  @Value("${MANUFACTURING_PLANT_NOT_FOUND}")
  private String mfgPlantNotFound;

  @Value("${MANUFACTURING_PLANT_NOT_FOUND_IN_COMPANY}")
  private String mfgPlantNotFoundInCompany;

  @Value("${MANUFACTURING_PLANT_ALREADY_EXISTS_IN_COMPANY}")
  private String mfgPlantAlreadyExistsInCompany;

  @Autowired
  ManufacturingPlantValidationService manufacturingPlantValidationService;

  @Autowired
  ManufacturingPlantRepository manufacturingPlantRepository;

  @Autowired
  CompanyRepository companyRepository;

  /**
   * Gets the first page of found ManufacturingPlants (empty when there is no ManufacturingPlant).
   *
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the ManufacturingPlants found.
   */
  public Page<ManufacturingPlant> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return manufacturingPlantRepository.findAll(pageable);
  }

  /**
   * @param manufacturingPlantKey - manufacturingPlantKey
   * @return - on success, returns the found ManufacturingPlantNot
   * @throws ManufacturingPlantNotFound -  on failure, throws this exception
   */
  public ManufacturingPlant get(ManufacturingPlantKey manufacturingPlantKey) throws ManufacturingPlantNotFound {
    Optional<ManufacturingPlant> manufacturingPlant = manufacturingPlantRepository.findById(manufacturingPlantKey);

    if (manufacturingPlant.isEmpty()) throw new ManufacturingPlantNotFound(mfgPlantNotFound);
    
    return manufacturingPlant.get();
  }

  /**
   * @param manufacturingPlant - the ManufacturingPlant to be added
   * @return -  the ManufacturingPlant added
   * @throws CompanyMaxLengthExceeded                             - if the companyCode exceeds the max allowed length                        -
   * @throws CompanyCodeCannotContainSpecialCharacters            - if the companyCode contains a non-allowable character
   * @throws ManufacturingPlantCodeCannotContainSpecialCharacters - if the manufacturingPlantCode contains a non-allowable character
   * @throws ManufacturingPlantMaxLengthExceeded                  - if the manufacturingPlantCode exceeds the max allowed length
   * @throws CompanyNotFound                                      - if the company with the companyCode is not found in the db
   * @throws ManufacturingPlantAlreadyExistsInCompany             - if the ManufacturingPlant with the
   *                                                              ManufacturingPlantKey already exists in the db
   */
  public ManufacturingPlant add(ManufacturingPlant manufacturingPlant) throws CompanyMaxLengthExceeded,
      CompanyCodeCannotContainSpecialCharacters, ManufacturingPlantCodeCannotContainSpecialCharacters,
      ManufacturingPlantMaxLengthExceeded, CompanyNotFound, ManufacturingPlantAlreadyExistsInCompany {
    ManufacturingPlantKey manufacturingPlantKey = manufacturingPlant.getId();
    manufacturingPlantKey = manufacturingPlantValidationService.validate(manufacturingPlantKey);
    String companyCode = manufacturingPlantKey.getCompCode();
    Optional<Company> tempCompany = companyRepository.findById(companyCode);

    if (tempCompany.isEmpty()) throw new CompanyNotFound(companyNotFound);

    Company company = tempCompany.get();
    List<ManufacturingPlant> manufacturingPlants = company.getMfgPlants();

    if (manufacturingPlants.size() != 0) {
      String mfgPlantCode = manufacturingPlantKey.getCode();
      Optional<ManufacturingPlant> tempMfgPlant = manufacturingPlants.stream()
          .filter(mfg -> mfg.getId().getCode().equals(mfgPlantCode)).findFirst();

      if (!tempMfgPlant.isEmpty()) throw new ManufacturingPlantAlreadyExistsInCompany(mfgPlantAlreadyExistsInCompany);
    }
    manufacturingPlant.setCompany(company);
    manufacturingPlant.setId(manufacturingPlantKey);
    return manufacturingPlantRepository.save(manufacturingPlant);
  }

  /**
   * @param manufacturingPlantKey - manufacturingPlantKey
   * @param manufacturingPlant    - ManufacturingPlant
   * @return - on success, returns the updated ManufacturingPlant
   * @throws CompanyNotFound                     - - company is not found with the companyCode
   * @throws ManufacturingPlantNotFoundInCompany - - ManufacturingPlant is not found under the company
   */
  public ManufacturingPlant update(ManufacturingPlantKey manufacturingPlantKey, ManufacturingPlant manufacturingPlant)
      throws CompanyNotFound, ManufacturingPlantNotFoundInCompany {
    String companyCode = manufacturingPlantKey.getCompCode();
    Optional<Company> tempCompany = companyRepository.findById(companyCode);

    if (tempCompany.isEmpty()) throw new CompanyNotFound(companyNotFound);

    Company company = tempCompany.get();
    List<ManufacturingPlant> mfgPlants = company.getMfgPlants();

    if (mfgPlants.size() == 0) throw new ManufacturingPlantNotFoundInCompany(mfgPlantNotFoundInCompany);

    String manufacturingPlantCode = manufacturingPlantKey.getCode();
    Optional<ManufacturingPlant> tempMfgPlant = mfgPlants.stream()
        .filter(mfg -> mfg.getId().getCode().equals(manufacturingPlantCode)).findFirst();

    if (tempMfgPlant.isEmpty()) throw new ManufacturingPlantNotFoundInCompany(mfgPlantNotFoundInCompany);

    ManufacturingPlant existingManufacturingPlant = tempMfgPlant.get();
    existingManufacturingPlant.setName(manufacturingPlant.getName());
    existingManufacturingPlant.setDescription(manufacturingPlant.getDescription());
    existingManufacturingPlant.setAddress1(manufacturingPlant.getAddress1());
    existingManufacturingPlant.setAddress2(manufacturingPlant.getAddress2());
    existingManufacturingPlant.setCity(manufacturingPlant.getCity());
    existingManufacturingPlant.setState(manufacturingPlant.getState());
    existingManufacturingPlant.setZip(manufacturingPlant.getZip());
    existingManufacturingPlant.setCountry(manufacturingPlant.getCountry());
    existingManufacturingPlant.setContactName(manufacturingPlant.getContactName());
    existingManufacturingPlant.setContactNumber(manufacturingPlant.getContactNumber());
    return manufacturingPlantRepository.save(existingManufacturingPlant);
  }

  /**
   * Deletes an existing ManufacturingPlant from the database.
   *
   * @param manufacturingPlantKey - PK of the ManufacturingPlant to be deleted
   * @return - true on success
   * @throws ManufacturingPlantNotFound - on failure, throws this exception
   */
  public boolean delete(ManufacturingPlantKey manufacturingPlantKey) throws ManufacturingPlantNotFound {
    manufacturingPlantRepository.delete(get(manufacturingPlantKey));
    return true;
  }
}