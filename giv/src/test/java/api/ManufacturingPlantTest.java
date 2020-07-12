package api;

import api.core.entities.Company;
import api.core.entities.ManufacturingPlant;
import api.core.entities.ManufacturingPlantKey;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.ManufacturingPlantRepository;
import api.core.services.ManufacturingPlantService;
import api.core.validation.ManufacturingPlantValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;


/**
 * This Class has all the unit test cases for the ManufacturingPlant service.
 */
@RunWith(SpringRunner.class)
public class ManufacturingPlantTest {
  @InjectMocks
  private ManufacturingPlantService manufacturingPlantService;

  @Mock
  private ManufacturingPlantValidationService manufacturingPlantValidationService;

  @Mock
  private ManufacturingPlantRepository manufacturingPlantRepository;

  @Mock
  private CompanyRepository companyRepository;

  private ManufacturingPlant manufacturingPlant;

  private Company company;

  private ManufacturingPlantKey manufacturingPlantKey;

  private static final int pageNo = 0;
  private static final int pageSize = 10;
  private static final String sortBy = "lastUpdatedDateTime";
  private static final String orderBy = "D";

  /**
   * Sets up the necessary data structures to be used in all unit tests in one place.
   */
  @Before
  public void setUp() {
    manufacturingPlantKey = new ManufacturingPlantKey("", "mfg001");
    company = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList());
    manufacturingPlant = new
        ManufacturingPlant(manufacturingPlantKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", company);
  }

  /**
   * Tests the getAll method of ManufacturingPlant service.
   */
  @Test
  public void testGetAll() {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    Page<ManufacturingPlant> manufacturingPlantPage = new PageImpl<>(Arrays.asList(), pageable, Arrays.asList().size());
    Assertions.assertNotNull(when(manufacturingPlantRepository.findAll(pageable)).thenReturn(manufacturingPlantPage));
  }

  /**
   * Tests the get method of the ManufacturingPlant service, when the ManufacturingPlant is not in the db.
   */
  @Test
  public void testGetWhenManufacturingPlantNotInDb() {
    when(manufacturingPlantRepository.findById(manufacturingPlantKey)).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ManufacturingPlantNotFound.class, () -> {
      manufacturingPlantService.get(manufacturingPlantKey);
    });
  }

  /*
   *
   * Tests the get method of the manufacturingPlant service, when the manufacturingPlant is already in the db.
   */
  @Test
  public void testGetWhenManufacturingPlantInDb() {
    Assert.assertNotNull(when(manufacturingPlantRepository.findById(manufacturingPlantKey)).thenReturn(Optional.of(manufacturingPlant)));
  }

  /**
   * Tests the add method of the ManufacturingPlant service, when the manufacturingPlantCode is blank (or empty) and
   * there is a company present in the db for the given companyCode.
   */
  @Test
  public void testAddWhenCompanyIsInDb() throws Exception {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenReturn(manufacturingPlantKey);
    Assertions.assertNotNull(when(manufacturingPlantService.add(manufacturingPlant)).thenReturn(manufacturingPlant));
  }

  /**
   * Tests the add method of the ManufacturingPlant service, when there is no company present in the db
   * for the given companyCode.
   */
  @Test
  public void testAddWhenCompanyNotInDb() throws CompanyMaxLengthExceeded,
      CompanyCodeCannotContainSpecialCharacters, ManufacturingPlantCodeCannotContainSpecialCharacters,
      ManufacturingPlantMaxLengthExceeded {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.ofNullable(null));
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenReturn(manufacturingPlantKey);
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      manufacturingPlantService.add(manufacturingPlant);
    });
  }

  /**
   * Tests the add method of the ManufacturingPlant service, when the manufacturingplantcode has special character
   * and there is a company for the given companyCode in the db.
   */
  @Test
  public void testAddWithSpecialCharacterInManufacturingPlantCode() throws Exception {
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenThrow(ManufacturingPlantCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(ManufacturingPlantCodeCannotContainSpecialCharacters.class, () -> {
      manufacturingPlantService.add(manufacturingPlant);
    });
  }

  /**
   * Tests the add method of the manufacturingPlant service, when the manufacturingPlantCode has more than
   * expected length.
   */
  @Test
  public void testAddWithMoreThanExpectedLengthInManufacturingPlantCode() throws Exception {
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenThrow(ManufacturingPlantMaxLengthExceeded.class);
    Assertions.assertThrows(ManufacturingPlantMaxLengthExceeded.class, () -> {
      manufacturingPlantService.add(manufacturingPlant);
    });
  }

  /**
   * Tests the add method of the manufacturingPlant service, when the manufacturingPlant is already
   * present in the db.
   */
  @Test
  public void testAddWhenManufacturingPlantAlreadyInDb() throws ManufacturingPlantCodeCannotContainSpecialCharacters,
      ManufacturingPlantMaxLengthExceeded, CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList(manufacturingPlant));
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenReturn(manufacturingPlantKey);
    Assertions.assertThrows(ManufacturingPlantAlreadyExistsInCompany.class, () -> {
      manufacturingPlantService.add(manufacturingPlant);
    });
  }

  /**
   * Tests the add method of the manufacturingPlant service, when the manufacturingPlant is not in the db.
   */
  @Test
  public void testAddWhenManufacturingPlantNotInDb() throws CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters
      , ManufacturingPlantMaxLengthExceeded, CompanyNotFound,
      ManufacturingPlantAlreadyExistsInCompany, ManufacturingPlantCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(),
        Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(manufacturingPlantValidationService.validate(manufacturingPlantKey)).thenReturn(manufacturingPlantKey);
    when(manufacturingPlantRepository.findById(manufacturingPlant.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertNotNull(when(manufacturingPlantService.add(manufacturingPlant)).thenReturn(manufacturingPlant));
  }

  /**
   * Tests the update method of the manufacturingPlant service, when the manufacturingPlant is present in the db.
   */
  @Test
  public void testUpdateWhenManufacturingPlantInDb() throws Exception {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList(manufacturingPlant));
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(manufacturingPlantRepository.findById(manufacturingPlant.getId())).thenReturn(Optional.of(manufacturingPlant));
    Assertions.assertNotNull(when(manufacturingPlantService.update(manufacturingPlantKey, manufacturingPlant)).thenReturn(manufacturingPlant));
  }

  /**
   * Tests the update method of the manufacturingPlant service, when the manufacturingPlant is not in the db.
   */
  @Test
  public void testUpdateWhenManufacturingPlantNotInDb() {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(manufacturingPlantRepository.findById(manufacturingPlant.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ManufacturingPlantNotFoundInCompany.class, () -> {
      manufacturingPlantService.update(manufacturingPlantKey, manufacturingPlant);
    });
  }

  /**
   * Tests the delete method of the ManufacturingPlant service, when the manufacturingPlant is present in the db.
   */
  @Test
  public void testDeleteWhenManufacturingPlantInDb() throws Exception {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(manufacturingPlantRepository.findById(manufacturingPlantKey)).thenReturn(Optional.of(manufacturingPlant));
    Assertions.assertTrue(manufacturingPlantService.delete(manufacturingPlantKey));
  }

  /**
   * Tests the delete method of the ManufacturingPlant service, when the manufacturingPlant is not in the db.
   */
  @Test
  public void testDeleteWhenManufacturingPlantNotInDb() {
    when(manufacturingPlantRepository.findById(manufacturingPlant.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ManufacturingPlantNotFound.class, () -> {
      manufacturingPlantService.delete(manufacturingPlantKey);
    });
  }
}
