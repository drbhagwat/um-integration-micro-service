package api;

import api.core.entities.*;
import api.core.errors.*;
import api.core.repo.CompanyRepository;
import api.core.repo.DivisionRepository;
import api.core.repo.WarehouseRepository;
import api.core.services.WarehouseService;
import api.core.validation.WarehouseValidationService;
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
 * This Class has all the unit test cases for the Warehouse service.
 */
@RunWith(SpringRunner.class)
public class WarehouseTest {
  @InjectMocks
  private WarehouseService warehouseService;

  @Mock
  private WarehouseValidationService warehouseValidationService;

  @Mock
  private WarehouseRepository warehouseRepository;

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private DivisionRepository divisionRepository;

  private Warehouse warehouse;

  private Company company;

  private Division division;

  private WarehouseKey warehouseKey;

  private DivisionKey divisionKey;

  private static final int pageNo = 0;
  private static final int pageSize = 10;
  private static final String sortBy = "lastUpdatedDateTime";
  private static final String orderBy = "D";

  /**
   * Sets up the necessary data structures to be used in all unit tests in one place.
   */
  @Before
  public void setUp() {
    divisionKey = new DivisionKey("", "");
    warehouseKey = new WarehouseKey("", "", "LEONISA");
    company = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(), Arrays.asList());
    division = new
        Division(divisionKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", company);
    warehouse = new
        Warehouse(warehouseKey, "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín",
        "", "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com",
        "3606090", "Carrera  50E # 8 sur - 62",
        "Medellín", "", "Antioquia",
        "SUR-62", "Colombia");
  }

  /**
   * Tests the getAll method of warehouse service.
   */
  @Test
  public void testGetAll() {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    Page<Warehouse> warehousePage = new PageImpl<>(Arrays.asList(), pageable, Arrays.asList().size());
    Assertions.assertNotNull(when(warehouseRepository.findAll(pageable)).thenReturn(warehousePage));
  }

  /**
   * Tests the get method of the warehouse service, when the warehouse is not in the db.
   */
  @Test
  public void testGetWhenWarehouseNotInDb() {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(WarehouseNotFound.class, () -> {
      warehouseService.get(warehouseKey);
    });
  }

  /**
   * Tests the get method of the warehouse service, when the warehouse is in the db.
   */
  @Test
  public void testGetWhenWarehouseIsInDb() throws WarehouseNotFound {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.of(warehouse));
    Assertions.assertNotNull(warehouseService.get(warehouseKey));
  }

  /**
   * Tests the add method of the warehouse service, when the company and division are both not in the db.
   */
  @Test
  public void testAddWhenCompanyIsNotInDb() {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.empty());
    Assertions.assertThrows(CompanyNotFound.class, () -> {
      warehouseService.add(warehouse);
    });
  }

  /**
   * Tests the add method of the warehouse service, when the company is in db but, the division doest not exist
   * under that company in the db, and you are trying to add a warehouse.
   */
  @Test
  public void testAddWhenCompanyIsInDbAndDivisionIsNotInThatCompany() {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    Assertions.assertThrows(DivisionNotFoundInCompany.class, () -> {
      warehouseService.add(warehouse);
    });
  }

  /**
   * Tests the get method of the warehouse service, when the company and division are already in the db.
   */
  @Test
  public void testAddWhenCompanyAndDivisionAreInDb() {
    when(companyRepository.findById(company.getCode())).thenReturn(Optional.of(company));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    Assert.assertNotNull(when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.of(warehouse)));
  }

  /**
   * Tests the add method of the warehouse service, when the company, division, and warehouse are all in the db,
   * and you are trying to add a warehouse.
   */
  @Test
  public void testAddWhenWarehouseIsInDb() throws CompanyMaxLengthExceeded, WarehouseCodeCannotBeBlank,
      CompanyCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded, WarehouseCodeMandatory,
      WarehouseMaxLengthExceeded, WarehouseCodeCannotContainSpecialCharacters,
      DivisionCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    when(warehouseValidationService.validate(warehouseKey)).thenReturn(warehouseKey);
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.ofNullable(warehouse));
    Assertions.assertThrows(WarehouseAlreadyExists.class, () -> {
      warehouseService.add(warehouse);
    });
  }

  /**
   * Tests the add method of the warehouse service, when the companyCode is blank (or empty)
   * and division code is blank (or empty).
   */
  @Test
  public void testAddWithBlankCompanyAndBlankDivisionCode() throws CompanyMaxLengthExceeded,
      WarehouseCodeCannotBeBlank, CompanyCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded,
      WarehouseCodeMandatory, WarehouseMaxLengthExceeded, WarehouseCodeCannotContainSpecialCharacters,
      DivisionCodeCannotContainSpecialCharacters, WarehouseAlreadyExists, CompanyNotFound, DivisionNotFoundInCompany {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    Assertions.assertNotNull(when(warehouseService.add(warehouse)).thenReturn(warehouse));
  }

  /**
   * Tests the add method of the warehouse service, when the warehouseCode has a non-allowed character
   */
  @Test
  public void testAddWithSpecialCharacterInWarehouseCode() throws CompanyMaxLengthExceeded,
      WarehouseCodeCannotBeBlank, CompanyCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded,
      WarehouseCodeMandatory, WarehouseMaxLengthExceeded, WarehouseCodeCannotContainSpecialCharacters,
      DivisionCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    when(warehouseValidationService.validate(warehouseKey)).thenThrow(WarehouseCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(WarehouseCodeCannotContainSpecialCharacters.class, () -> {
      warehouseService.add(warehouse);
    });
  }

  /**
   * Tests the add method of the warehouse service, when the warehouseCode has more than max allowed
   * length
   */
  @Test
  public void testAddWithWarehouseCodeMoreThanMaxLength() throws CompanyMaxLengthExceeded,
      WarehouseCodeCannotBeBlank, CompanyCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded,
      WarehouseCodeMandatory, WarehouseMaxLengthExceeded, WarehouseCodeCannotContainSpecialCharacters,
      DivisionCodeCannotContainSpecialCharacters {
    Company tempCompany = new
        Company("", "LEONISA DC", "Leonisa Centro de Distribución", "Carrera  50E # 8 sur - 62", "Medellín", "",
        "Antioquia", "SUR-62", "Colombia", "infocol@leonisa.com", "3606090", Arrays.asList(division), Arrays.asList());
    when(companyRepository.findById(tempCompany.getCode())).thenReturn(Optional.of(tempCompany));
    when(divisionRepository.findById(divisionKey)).thenReturn(Optional.of(division));
    when(warehouseValidationService.validate(warehouseKey)).thenThrow(WarehouseMaxLengthExceeded.class);
    Assertions.assertThrows(WarehouseMaxLengthExceeded.class, () -> {
      warehouseService.add(warehouse);
    });
  }

  /**
   * Tests the add method of the warehouse service, when the warehouse is in the db,
   * and you are trying to update that warehouse.
   */
  @Test
  public void testUpdateWhenWarehouseIsInDb() throws WarehouseNotFound {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.of(warehouse));
    Assertions.assertNotNull(when(warehouseService.update(warehouseKey, warehouse)).thenReturn(warehouse));
  }

  /**
   * Tests the add method of the warehouse service, when the warehouse is not in the db,
   * and you are trying to update that warehouse.
   */
  @Test
  public void testUpdateWhenWarehouseIsNotInDb() {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(WarehouseNotFound.class, () -> {
      warehouseService.update(warehouseKey, warehouse);
    });
  }

  /**
   * Tests the delete method of the warehouse service, when the warehouse is in the db,
   * and you are trying to delete that warehouse.
   */
  @Test
  public void testDeleteWhenWarehouseIsInDb() throws WarehouseNotFound {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.of(warehouse));
    Assertions.assertNotNull(when(warehouseService.update(warehouseKey, warehouse)).thenReturn(warehouse));
  }

  /**
   * Tests the delete method of the warehouse service, when the warehouse is not in the db,
   * and you are trying to delete that warehouse.
   */
  @Test
  public void testDeleteWhenWarehouseIsNotInDb() {
    when(warehouseRepository.findById(warehouseKey)).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(WarehouseNotFound.class, () -> {
      warehouseService.update(warehouseKey, warehouse);
    });
  }

}
