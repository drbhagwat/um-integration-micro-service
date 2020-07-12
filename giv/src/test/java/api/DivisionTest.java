package api;

import api.core.entities.Company;
import api.core.entities.Division;
import api.core.entities.DivisionKey;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.DivisionRepository;
import api.core.services.DivisionService;
import api.core.validation.DivisionValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * This class tests Division
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2020-03-17
 */
@RunWith(SpringRunner.class)
public class DivisionTest {
  @InjectMocks
  private DivisionService divisionService;

  @Mock
  private DivisionValidationService divisionValidationService;

  @Mock
  private DivisionRepository divisionRepository;

  @Mock
  private CompanyRepository companyRepository;

  private Division division;

  private Company company;

  private DivisionKey divisionKey;

  private static final int pageNo = 0;
  private static final int pageSize = 10;
  private static final String sortBy = "lastUpdatedDateTime";
  private static final String orderBy = "D";

  /**
   * Sets up the necessary data structures to be used in all unit tests.
   */
  @Before
  public void setUp() {
    divisionKey = new DivisionKey("", "");
    company = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList());
    division = new
        Division(divisionKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", company);
  }

  /**
   * Tests the getAll method of DivisionService.
   */
  @Test
  public void testGetAll() {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    Page<Division> divisionPage = new PageImpl<>(Arrays.asList(), pageable, Arrays.asList().size());
    Assertions.assertNotNull(when(divisionService.getAll(pageNo, pageSize, sortBy, orderBy)).thenReturn(divisionPage));
  }

  /**
   * Tests the get method of the DivisionService, when the division is not present in the db.
   */
  @Test
  public void testGetWhenDivisionNotInDb() {
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.empty());
    Assertions.assertThrows(DivisionNotFound.class, () -> {
      divisionService.get(divisionKey);
    });
  }

  /**
   * Tests the get method of the DivisionService, when the division is already present in the db.
   */
  @Test
  public void testGetWhenDivisionInDb() throws DivisionNotFound {
    DivisionService tempDivisionService = Mockito.mock(DivisionService.class);
    when(divisionRepository.findById(division.getId())).thenReturn(Optional.of(division));
    when(tempDivisionService.get(division.getId())).thenReturn(division);
    Assert.assertNotNull(division.getId());
  }

  /**
   * Tests the add method of the Division service, when the divisionCode is blank (or empty) and there is a
   * company present in the db for the given companyCode.
   */
  @Test
  public void testAddWithBlankDivisionCodeWhenCompanyIsInDb() throws Exception {
    when(divisionValidationService.validate(divisionKey)).thenReturn(divisionKey);
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    Assertions.assertNotNull(when(divisionService.add(division)).thenReturn(division));
  }

  /**
   * Tests the add method of the Division service, when the divisionCode is blank (or empty) and there is no
   * company present in the db for the given companyCode.
   */
  @Test
  public void testAddWithBlankDivisionCodeWhenCompanyNotInDb() throws DivisionCodeCannotContainSpecialCharacters,
      DivisionMaxLengthExceeded, CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters {
    when(divisionValidationService.validate(divisionKey)).thenReturn(divisionKey);
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.empty());
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      divisionService.add(division);
    });
  }

  /**
   * Tests the add method of the Division service, when the companyCode has special character.
   */
  @Test
  public void testAddWithSpecialCharacterInCompanyCode() throws DivisionCodeCannotContainSpecialCharacters,
      DivisionMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters, CompanyMaxLengthExceeded {
    when(divisionValidationService.validate(divisionKey)).thenThrow(CompanyCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(CompanyCodeCannotContainSpecialCharacters.class, () -> {
      divisionService.add(division);
    });
  }

  /**
   * Tests the add method of the Division service, when the divisionCode has special character.
   */
  @Test
  public void testAddWithSpecialCharacterInDivisionCode() throws DivisionCodeCannotContainSpecialCharacters,
      DivisionMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters, CompanyMaxLengthExceeded  {
    when(divisionValidationService.validate(divisionKey)).thenThrow(DivisionCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(DivisionCodeCannotContainSpecialCharacters.class, () -> {
      divisionService.add(division);
    });
  }

  /**
   * Tests the add method of the Division service, when the divisionCode has more than expected length.
   */
  @Test
  public void testAddWithMoreThanExpectedLengthInDivisionCode() throws Exception {
    when(divisionValidationService.validate(divisionKey)).thenThrow(DivisionMaxLengthExceeded.class);
    Assertions.assertThrows(DivisionMaxLengthExceeded.class, () -> {
      divisionService.add(division);
    });
  }

  /**
   * Tests the add method of the Division service, when the division is present in the db.
   */
  @Test
  public void testAddWhenDivisionAlreadyInDb() throws DivisionCodeCannotContainSpecialCharacters,
      DivisionMaxLengthExceeded, CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionValidationService.validate(divisionKey)).thenReturn(divisionKey);
    Assertions.assertThrows(DivisionAlreadyExistsInCompany.class, () -> {
      divisionService.add(division);
    });
  }

  /**
   * Tests the add method of the Division service, when the division is not in the db.
   */
  @Test
  public void testAddWhenDivisionNotInDb() throws CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters
      , DivisionMaxLengthExceeded, CompanyNotFound,
      DivisionAlreadyExistsInCompany, DivisionCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), null);
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionValidationService.validate(divisionKey)).thenReturn(divisionKey);
    when(divisionRepository.findById(division.getId())).thenReturn(Optional.empty());
    Assertions.assertNotNull(when(divisionService.add(division)).thenReturn(division));
  }

  /**
   * Tests the update method of the Division service, when the division is present in the db.
   */
  @Test
  public void testUpdateWhenDivisionInDb() throws Exception {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    Division tempDivision = new
        Division(divisionKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", tempCompany);
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(division.getId())).thenReturn(Optional.of(tempDivision));
    Assertions.assertNotNull(when(divisionService.update(divisionKey, tempDivision)).thenReturn(tempDivision));
  }

  /**
   * Tests the update method of the Division service, when the division is not in the db.
   */
  @Test
  public void testUpdateWhenDivisionNotInDb() {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), null);
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(DivisionNotFoundInCompany.class, () -> {
      divisionService.update(divisionKey, division);
    });
  }


  /**
   * Tests the delete method of the Division service, when the division is present in the db.
   */
  @Test
  public void testDeleteWhenDivisionInDb() throws Exception {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), null);
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    Assertions.assertTrue(divisionService.delete(divisionKey));
  }


  /**
   * Tests the delete method of the Division service, when the division is not in the db.
   */
  @Test
  public void testDeleteWhenDivisionNotInDb() {
    when(divisionRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(DivisionNotFound.class, () -> {
      divisionService.delete(divisionKey);
    });
  }
}
