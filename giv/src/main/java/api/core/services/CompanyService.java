package api.core.services;

import api.core.entities.*;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.validation.CompanyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Create Read Update Delete (CRUD) services for Company entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-03-05
 */
@Service
@Transactional
public class CompanyService {
  @Value("${COMPANY_NOT_FOUND}")
  private String companyNotFound;

  @Value("${COMPANY_ALREADY_EXISTS}")
  private String companyAlreadyExists;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private CompanyValidationService companyValidationService;

  @Autowired
  private DivisionService divisionService;

  @Autowired
  private ManufacturingPlantService manufacturingPlantService;

  /**
   * Gets the first page of companies found in the db (will be empty when there are no companies present in the db).
   *
   * @param pageNo   - page number
   * @param pageSize - page size
   * @param sortBy   - sort order (ascending/descending)
   * @param orderBy  - sort based on a key
   * @return - first page of the companies found.
   */
  public Page<Company> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return companyRepository.findAll(pageable);
  }

  /**
   * Retrieves the company whose PK matches the given companyKey.
   *
   * @param companyKey - PK of company
   * @return - on success, returns the found company
   * @throws CompanyNotFound - on failure, throws this exception
   */
  public Company get(String companyKey) throws CompanyNotFound {
    Optional<Company> company = companyRepository.findById(companyKey);

    if (company.isEmpty()) throw new CompanyNotFound(companyNotFound);

    return company.get();
  }

  /**
   * Adds a new company.
   *
   * @param company - the company to be added to the db
   * @return - on success, returns the company added into the db
   * @throws CompanyCodeCannotContainSpecialCharacters - The company to be added contains a character which is not
   *                                                   allowed
   * @throws CompanyMaxLengthExceeded                  - the PK of the company to be added exceeds the allowed max
   *                                                   length
   * @throws CompanyAlreadyExists                      - The company to be added already exists in the db
   */
  public Company add(Company company) throws CompanyCodeCannotContainSpecialCharacters,
      CompanyMaxLengthExceeded, CompanyAlreadyExists {
    String companyCode = company.getCode();
    companyCode = companyValidationService.validate(companyCode);
    Optional<Company> tempCompany = companyRepository.findById(companyCode);

    if (tempCompany.isPresent()) throw new CompanyAlreadyExists(companyAlreadyExists);
    company.setCode(companyCode);
    return companyRepository.save(company);
  }

  /**
   * Updates an existing company in the db.
   *
   * @param companyKey - PK of the company
   * @param company    - the company to be updated
   * @return - on success, returns the updated company
   * @throws - CompanyNotFound - on failure, throws this exception
   */
  public Company update(String companyKey, Company company) throws CompanyNotFound {
    Company existingCompany = get(companyKey);
    existingCompany.setName(company.getName());
    existingCompany.setDescription(company.getDescription());
    existingCompany.setAddress1(company.getAddress1());
    existingCompany.setAddress2(company.getAddress2());
    existingCompany.setCity(company.getCity());
    existingCompany.setState(company.getState());
    existingCompany.setZip(company.getZip());
    existingCompany.setCountry(company.getCountry());
    existingCompany.setContactName(company.getContactName());
    existingCompany.setContactNumber(company.getContactNumber());
    return companyRepository.save(existingCompany);
  }

  /**
   * Deletes an existing company from the db. The divisions, and manufacturing plants under this company are also
   * automatically deleted. This is a hard delete. Use it with care.
   *
   * @param companyKey - PK of the company.
   * @return - true when successful
   * @throws CompanyNotFound            - when the company is not found
   * @throws DivisionNotFound           - when the division in not found
   * @throws ManufacturingPlantNotFound - when the manufacturingPlant is not found
   */
  public boolean delete(String companyKey) throws DivisionNotFound, ManufacturingPlantNotFound, CompanyNotFound {
    Company company = get(companyKey);
    List<Division> divisions = company.getDivisions();

    for (Division division : divisions) {
      divisionService.delete(new DivisionKey(companyKey, division.getId().getCode()));
    }
    List<ManufacturingPlant> mfgPlants = company.getMfgPlants();

    for (ManufacturingPlant mfgPlant : mfgPlants) {
      manufacturingPlantService.delete(new ManufacturingPlantKey(companyKey, mfgPlant.getId().getCode()));
    }
    companyRepository.delete(company);
    return true;
  }
}