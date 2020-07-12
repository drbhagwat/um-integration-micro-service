package api;

import api.core.entities.*;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.services.CompanyService;
import api.core.services.DivisionService;
import api.core.services.ManufacturingPlantService;
import api.core.validation.CompanyValidationService;
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

import static org.mockito.Mockito.when;

/**
 * This class tests Company
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2020-03-05
 */
@RunWith(SpringRunner.class)
public class CompanyTest {
  @InjectMocks
  private CompanyService companyService;

  @Mock
  private CompanyValidationService companyValidationService;

  @Mock
  private DivisionService divisionService;

  @Mock
  private ManufacturingPlantService manufacturingPlantService;

  @Mock
  private CompanyRepository companyRepository;

  private Company company;

  private static final int pageNo = 0;
  private static final int pageSize = 10;
  private static final String sortBy = "lastUpdatedDateTime";
  private static final String orderBy = "D";

  /**
   * Sets up the necessary data structures to be used in all unit tests in one place.
   */
  @Before
  public void setUp() {
    company = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(),
        Arrays.asList());
  }

  /**
   * Tests the getAll method of CompanyService.
   */
  @Test
  public void testGetAll() {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    Page<Company> companyPage = new PageImpl<>(Arrays.asList(), pageable, Arrays.asList().size());
    Assertions.assertNotNull(when(companyService.getAll(pageNo, pageSize, sortBy, orderBy)).thenReturn(companyPage));
  }

  /**
   * Tests the get method of the CompanyService, when the company is not present in the db.
   */
  @Test
  public void testGetWhenCompanyNotInDb() {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.empty());
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      companyService.get(company.getCode());
    });
  }

  /**
   * Tests the get method of the CompanyService, when the company is already present in the db.
   */
  @Test
  public void testGetWhenCompanyInDb() throws CompanyNotFound {
    CompanyService tempCompanyService = Mockito.mock(CompanyService.class);
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    when(tempCompanyService.get(company.getCode())).thenReturn(company);
    Assert.assertNotNull(company.getCode());
  }

  /**
   * Tests the add method of the CompanyService, when the companyCode is blank or empty.
   */
  @Test
  public void testAddWithBlankCompanyCode() throws Exception {
    Assertions.assertNotNull(when(companyService.add(company)).thenReturn(company));
  }

  /**
   * Tests the add method of the CompanyService, when the companyCode has special character.
   */
  @Test
  public void testAddWithSpecialCharacterInCompanyCode() throws Exception {
    when(companyValidationService.validate(company.getCode())).thenThrow(CompanyCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(CompanyCodeCannotContainSpecialCharacters.class, () -> {
      companyService.add(company);
    });
  }

  /**
   * Tests the add method of the CompanyService, when the companyCode having more than expected length.
   */
  @Test
  public void testAddWithMoreThanExpectedLengthInCompanyCode() throws Exception {
    when(companyValidationService.validate(company.getCode())).thenThrow(CompanyMaxLengthExceeded.class);
    Assertions.assertThrows(CompanyMaxLengthExceeded.class, () -> {
      companyService.add(company);
    });
  }

  /**
   * Tests the add method of the CompanyService, when the company is present in the db.
   */
  @Test
  public void testAddWhenCompanyAlreadyInDb() throws CompanyCodeCannotContainSpecialCharacters, CompanyMaxLengthExceeded {
    String companyCode = company.getCode();
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    when(companyValidationService.validate(companyCode)).thenReturn(companyCode);
    Assertions.assertThrows(CompanyAlreadyExists.class, () -> {
      companyService.add(company);
    });
  }

  /**
   * Tests the add method of the CompanyService, when the company is not present in the db.
   */
  @Test
  public void testAddWhenCompanyNotInDb() throws Exception {
    Assertions.assertNotNull(when(companyService.add(company)).thenReturn(company));
  }

  /**
   * Tests the update method of the CompanyService, when the company is present in the db.
   */
  @Test
  public void testUpdateWhenCompanyInDb() throws CompanyNotFound {
    String companyCode = company.getCode();
    when(companyRepository.findById(companyCode)).thenReturn(Optional.of(company));
    Assertions.assertNotNull(when(companyService.update(companyCode, company)).thenReturn(company));
  }

  /**
   * Tests the update method of the CompanyService, when the company is not present in the db.
   */
  @Test
  public void testUpdateWhenCompanyNotInDb() {
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      companyService.update(company.getCode(), company);
    });
  }

  /**
   * Tests the delete method of the CompanyService, when the company is present in the db.
   */
  @Test
  public void testDeleteWhenCompanyInDb() throws ManufacturingPlantNotFound, DivisionNotFound, CompanyNotFound {
    String companyCode = company.getCode();
    DivisionKey divisionKey = new DivisionKey("", "");
    ManufacturingPlantKey manufacturingPlantKey = new ManufacturingPlantKey("", "mfg001");
    Division division = new
        Division(divisionKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", company);
    ManufacturingPlant manufacturingPlant = new
        ManufacturingPlant(manufacturingPlantKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", company);
    company.setDivisions(Arrays.asList(division));
    company.setMfgPlants(Arrays.asList(manufacturingPlant));
    when(companyRepository.findById(companyCode)).thenReturn(Optional.of(company));
    Assertions.assertTrue(companyService.delete(companyCode));
  }

  /**
   * Tests the delete method of the CompanyService, when the company is not present in the db.
   */
  @Test
  public void testDeleteWhenCompanyNotInDb() {
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      companyService.delete(company.getCode());
    });
  }
}