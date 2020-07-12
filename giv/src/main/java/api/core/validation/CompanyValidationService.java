package api.core.validation;

import api.core.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Company Validation Service
 * - validates the companyCode which is the Primary Key (PK)
 * - validates the length of the companyCode - configured through COMPANY_CODE_MAX_LENGTH (currently 10) defined in
 * application.yml
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2020-03-05
 */
@Service
public class CompanyValidationService {
  @Value("${COMPANY_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS")
  private String companyCodeCannotContainSpecialCharcaters;

  @Value("${COMPANY_CODE_INVALID_LENGTH")
  private String companyCodeInvalidLength;

  @Value("${COMPANY_CODE_MAX_LENGTH}")
  private int companyCodeMaxLength;

  @Autowired
  private CodeValidationService codeValidationService;

  /**
   * @param companyCode - which is the PK
   * @return - the same companyCode but, with trailing spaces removed.
   * @throws CompanyCodeCannotContainSpecialCharacters - throws this when the companyCode contains a special character
   * @throws CompanyMaxLengthExceeded                  - throws this when the companyCode exceeds the allowable max
   *                                                   length
   */
  public String validate(String companyCode) throws CompanyCodeCannotContainSpecialCharacters,
      CompanyMaxLengthExceeded {
    try {
      codeValidationService.validate(companyCode);
      codeValidationService.validate(companyCode.length(), companyCodeMaxLength);
      return companyCode.stripTrailing();
    } catch (CodeCannotContainSpecialCharacters codeCannotContainSpecialCharactersException) {
      throw new CompanyCodeCannotContainSpecialCharacters(companyCodeCannotContainSpecialCharcaters);
    } catch (MaxLengthExceeded maxLengthExceededException) {
      throw new CompanyMaxLengthExceeded(companyCodeInvalidLength);
    }
  }
}