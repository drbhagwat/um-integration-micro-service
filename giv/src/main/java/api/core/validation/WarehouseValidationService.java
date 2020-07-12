package api.core.validation;

import api.core.entities.DivisionKey;
import api.core.entities.WarehouseKey;
import api.core.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides Warehouse validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-02-06
 */
@Service
public class WarehouseValidationService {
  @Value("${WAREHOUSE_CODE_MANDATORY}")
  private String warehouseCodeMandatory;

  @Value("${WAREHOUSE_CODE_CANNOT_BE_BLANK}")
  private String warehouseCodeCannotBeBlank;

  @Value("${WAREHOUSE_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String warehouseCodeCannotContainSpecialCharacters;

  @Value("${WAREHOUSE_CODE_MAX_LENGTH}")
  private int warehouseCodeMaxLength;

  @Value("${WAREHOUSE_CODE_INVALID_LENGTH}")
  private String warehouseCodeInvalidLength;

  @Autowired
  private DivisionValidationService divisionValidationService;

  @Autowired
  private CodeValidationService codeValidationService;

  /**
   * Validates the given warehouseKey.
   *
   * @param warehouseKey - the composite PK(companyCode, divisionCode, warehouseCode).
   * @return - the warehouseKey formed by stripping the companyCode, the divisionCode, and the warehouseCode
   * @throws DivisionCodeCannotContainSpecialCharacters  - if the divisionCode contains a non-allowed character
   * @throws DivisionMaxLengthExceeded                   - if the divisionCode exceeds allowed max length
   * @throws CompanyMaxLengthExceeded                    - if the companyCode exceeds allowed max length
   * @throws CompanyCodeCannotContainSpecialCharacters   - if the divisionCode contains a non-allowed character
   * @throws WarehouseCodeMandatory                      - if the warehouseCode is null
   * @throws WarehouseCodeCannotBeBlank                  - if the warehouseCode is blank
   * @throws WarehouseCodeCannotContainSpecialCharacters - if the warehouseCode contains a non-allowed character
   * @throws WarehouseMaxLengthExceeded                  - if the warehouseCode exceeds the allowed max length.
   */
  public WarehouseKey validate(WarehouseKey warehouseKey) throws DivisionCodeCannotContainSpecialCharacters,
      DivisionMaxLengthExceeded, CompanyMaxLengthExceeded, CompanyCodeCannotContainSpecialCharacters,
      WarehouseCodeMandatory, WarehouseCodeCannotBeBlank, WarehouseCodeCannotContainSpecialCharacters,
      WarehouseMaxLengthExceeded {
    String companyCode = warehouseKey.getCompanyCode();
    String divisionCode = warehouseKey.getDivCode();
    DivisionKey divisionKey = new DivisionKey(companyCode, divisionCode);
    divisionKey = divisionValidationService.validate(divisionKey);
    String warehouseCode = warehouseKey.getCode();

    try {
      String newWarehouseCode = codeValidationService.validateAll(warehouseCode);
      codeValidationService.validate(warehouseCode.length(), warehouseCodeMaxLength);
      return new WarehouseKey(divisionKey.getCompCode(), divisionKey.getCode(), newWarehouseCode);
    } catch (CodeMandatory exception) {
      throw new WarehouseCodeMandatory(warehouseCodeMandatory);
    } catch (CodeCannotBeBlank exception) {
      throw new WarehouseCodeCannotBeBlank(warehouseCodeCannotBeBlank);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new WarehouseCodeCannotContainSpecialCharacters(warehouseCodeCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new WarehouseMaxLengthExceeded(warehouseCodeInvalidLength);
    }
  }
}