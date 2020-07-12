package api.core.validation;

import api.core.entities.DivisionKey;
import api.core.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides Division validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-05-01
 * @since : 2019-11-23
 */
@Service
public class DivisionValidationService {
  @Value("${DIVISION_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String divisionCodeCannotContainSpecialCharacters;

  @Value("${DIVISION_CODE_MAX_LENGTH}")
  private int divisionCodeMaxLength;

  @Value("${DIVISION_CODE_INVALID_LENGTH}")
  private String divisionCodeInvalidLength;

  @Autowired
  private CodeValidationService codeValidationService;

  @Autowired
  CompanyValidationService companyValidationService;

  /**
   * Validates a divisionKey which consists of companyCode and divisionCode
   * - to check if either the companyCode or the divisionCode contains a special character other than the set of
   * allowed ones - as configured in  application.yml
   *
   * @param divisionKey - the composite PK (companyCode and divisionCode)
   * @return - valid divisionKey with trailing spaces removed in both companyCode and divisionCode
   * @throws CompanyCodeCannotContainSpecialCharacters - if the companyCode contains a non-allowable character
   * @throws CompanyMaxLengthExceeded - if the companyCode exceeds the max allowed length
   * @throws DivisionMaxLengthExceeded - - if the divisionCode exceeds the max allowed length
   * @throws DivisionCodeCannotContainSpecialCharacters - if the divisionCode contains a non-allowable character
   */
  public DivisionKey validate(DivisionKey divisionKey) throws CompanyCodeCannotContainSpecialCharacters,
      CompanyMaxLengthExceeded, DivisionCodeCannotContainSpecialCharacters, DivisionMaxLengthExceeded {
    String companyCode = companyValidationService.validate(divisionKey.getCompCode());

    try {
      String divisionCode = divisionKey.getCode();
      codeValidationService.validate(divisionCode);
      codeValidationService.validate(divisionCode.length(), divisionCodeMaxLength);
      divisionCode = divisionCode.stripTrailing();
      return new DivisionKey(companyCode, divisionCode);
    } catch (CodeCannotContainSpecialCharacters exception) {
      throw new DivisionCodeCannotContainSpecialCharacters(divisionCodeCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new DivisionMaxLengthExceeded(divisionCodeInvalidLength);
    }
  }
}