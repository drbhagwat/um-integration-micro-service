package api.core.validation;

import api.core.entities.ManufacturingPlantKey;
import api.core.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides ManufacturingPlant validation service.
 *
 * @author : Dinesh
 * @version : 2.0
 * @since : 2019-02-10
 */
@Service
public class ManufacturingPlantValidationService {
  @Value("${MANUFACTURING_PLANT_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String mfgPlantCodeCannotContainSpecialCharacters;

  @Value("${MANUFACTURING_PLANT_CODE_INVALID_LENGTH}")
  private String mfgPlantCodeInvalidLength;

  @Value("${MANUFACTURING_PLANT_CODE_MAX_LENGTH}")
  private int manufacturingPlantCodeMaxLength;

  @Autowired
  CodeValidationService codeValidationService;

  @Autowired
  CompanyValidationService companyValidationService;


  /**
   * @param manufacturingPlantKey - the composite PK (companyCode and manufacturingPlantCode)
   * @return - valid manufacturingPlantKey with trailing spaces removed in both companyCode and manufacturingPlantCode
   * @throws CompanyCodeCannotContainSpecialCharacters            - if the companyCode contains a non-allowable character
   * @throws CompanyMaxLengthExceeded                             - if the companyCode exceeds the max allowed length
   * @throws ManufacturingPlantCodeCannotContainSpecialCharacters - if the manufacturingPlantCode contains a non-allowable character
   * @throws ManufacturingPlantMaxLengthExceeded                  - if the manufacturingPlantCode exceeds the max allowed length
   */
  public ManufacturingPlantKey validate(ManufacturingPlantKey manufacturingPlantKey) throws CompanyCodeCannotContainSpecialCharacters, CompanyMaxLengthExceeded,
      ManufacturingPlantCodeCannotContainSpecialCharacters, ManufacturingPlantMaxLengthExceeded {
    String companyCode = companyValidationService.validate(manufacturingPlantKey.getCompCode());

    try {
      String manufacturingPlantCode = manufacturingPlantKey.getCode();
      codeValidationService.validate(manufacturingPlantCode);
      codeValidationService.validate(manufacturingPlantCode.length(), manufacturingPlantCodeMaxLength);
      manufacturingPlantCode = manufacturingPlantCode.stripTrailing();
      return new ManufacturingPlantKey(companyCode, manufacturingPlantCode);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new ManufacturingPlantCodeCannotContainSpecialCharacters(mfgPlantCodeCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new ManufacturingPlantMaxLengthExceeded(mfgPlantCodeInvalidLength);
    }
  }
}