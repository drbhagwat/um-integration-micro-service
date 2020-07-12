package api.core.services;

import api.core.entities.Company;
import api.core.entities.Division;
import api.core.entities.DivisionKey;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.DivisionRepository;
import api.core.validation.DivisionValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Create Read Update Delete (CRUD) services for Division entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-03-17
 */
@Service
@Transactional
public class DivisionService {
  @Value("${COMPANY_NOT_FOUND}")
  private String companyNotFound;

  @Value("${DIVISION_NOT_FOUND}")
  private String divisionNotFound;

  @Value("${DIVISION_NOT_FOUND_IN_COMPANY}")
  private String divisionNotFoundInCompany;

  @Value("${DIVISION_ALREADY_EXISTS_IN_COMPANY}")
  private String divisionAlreadyExistsInCompany;

  @Autowired
  DivisionValidationService divisionValidationService;

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  DivisionRepository divisionRepository;

  /**
   * Gets the first page of divisions found in the db (will be empty when there are no divisions present in the
   * db).
   *
   * @param pageNo   - page number
   * @param pageSize - page size
   * @param sortBy   - sort order (ascending/descending)
   * @param orderBy  - sort based on a key
   * @return - first page of the companies found.
   */
  public Page<Division> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return divisionRepository.findAll(paging);
  }

  /**
   * Retrieves the division whose PK matches the given divisionKey.
   *
   * @param divisionKey - PK of division
   * @return - on success, returns the found division
   * @throws DivisionNotFound - on failure, throws this exception
   */
  public Division get(DivisionKey divisionKey) throws DivisionNotFound {
    Optional<Division> division = divisionRepository.findById(divisionKey);

    if (division.isEmpty()) throw new DivisionNotFound(divisionNotFound);

    return division.get();
  }

  /**
   * Adds a new Division
   *
   * @param division - the division to be added to the db
   * @return - on success, returns the division added into the db
   * @throws DivisionCodeCannotContainSpecialCharacters - The divisionCode contains a character which is not
   *                                                    allowed
   * @throws DivisionMaxLengthExceeded                  - the divisionCode to be added exceeds the allowed max
   *                                                    length
   * @throws CompanyMaxLengthExceeded                   - the  companyCode to be added exceeds the allowed max
   *                                                    length
   * @throws CompanyCodeCannotContainSpecialCharacters  - The companyCode contains a character which is not
   *                                                    allowed
   * @throws CompanyNotFound                            - company is not found in the db.
   * @throws DivisionAlreadyExistsInCompany             - The division to be added already exists in the db
   */
  public Division add(Division division) throws DivisionCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded
      , CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters, CompanyNotFound,
      DivisionAlreadyExistsInCompany {
    DivisionKey divisionKey = division.getId();
    divisionKey = divisionValidationService.validate(divisionKey);
    Optional<Company> optionalCompany = companyRepository.findById(divisionKey.getCompCode());

    if (optionalCompany.isEmpty()) throw new CompanyNotFound(companyNotFound);

    Company company = optionalCompany.get();
    List<Division> divisions = company.getDivisions();

    if (!divisions.isEmpty()) {
      String divisionCode = divisionKey.getCode();
      Optional<Division> tempDivision = divisions.stream()
          .filter(di -> di.getId().getCode().equals(divisionCode)).findFirst();

      if (tempDivision.isPresent()) throw new DivisionAlreadyExistsInCompany(divisionAlreadyExistsInCompany);
    }
    division.setCompany(company);
    division.setId(divisionKey);
    return divisionRepository.save(division);
  }

  /**
   * Updates an existing division in the db.
   *
   * @param divisionKey - PK of the division
   * @param division    - the division to be updated
   * @return - on success, returns the updated division
   * @throws CompanyNotFound           - company is not found with the companyCode.
   * @throws DivisionNotFoundInCompany - division is not found under the company.
   */
  public Division update(DivisionKey divisionKey, Division division) throws CompanyNotFound, DivisionNotFoundInCompany {
    String companyCode = divisionKey.getCompCode();
    Optional<Company> optionalCompany = companyRepository.findById(companyCode);

    if (optionalCompany.isEmpty()) throw new CompanyNotFound(companyNotFound);

    Company existingCompany = optionalCompany.get();
    List<Division> divisions = existingCompany.getDivisions();

    if (divisions.isEmpty()) throw new DivisionNotFoundInCompany(divisionNotFoundInCompany);

    Optional<Division> tempDivision =
        divisions.stream().filter(di -> di.getId().getCode().equals(divisionKey.getCode()))
            .findFirst();

    if (tempDivision.isEmpty()) throw new DivisionNotFoundInCompany(divisionNotFoundInCompany);

    Division existingDivision = tempDivision.get();
    existingDivision.setName(division.getName());
    existingDivision.setDescription(division.getDescription());
    existingDivision.setAddress1(division.getAddress1());
    existingDivision.setAddress2(division.getAddress2());
    existingDivision.setCity(division.getCity());
    existingDivision.setState(division.getState());
    existingDivision.setZip(division.getZip());
    existingDivision.setCountry(division.getCountry());
    existingDivision.setContactName(division.getContactName());
    existingDivision.setContactNumber(division.getContactNumber());
    return divisionRepository.save(existingDivision);
  }

  /**
   * Deletes an existing division from the database.
   *
   * @param divisionKey - PK of the division to be deleted.
   * @throws DivisionNotFound - on failure, throws this exception.
   */
  public boolean delete(DivisionKey divisionKey) throws DivisionNotFound {
    divisionRepository.delete(get(divisionKey));
    return true;
  }
}