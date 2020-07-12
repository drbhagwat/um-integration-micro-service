package api.core.validation;

import api.core.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Validation service (used in validating primary keys of company, division, warehouse etc.,)
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-03-05
 */
@Service
public class CodeValidationService implements NullValidator, BlankValidatorPostRightTrim, LengthValidator {
  @Value("${CODE_MANDATORY}")
  private String codeMandatory;

  @Value("${CODE_CANNOT_BE_BLANK}")
  private String codeCannotBeBlank;

  @Value("${CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String codeCannotContainSpecialCharacters;

  @Value("${CODE_INVALID_LENGTH}")
  private String codeInvalidLength;

  @Value("${KEY_FIELD_MANDATORY}")
  private String keyFieldMandatory;

  @Value("${KEY_FIELD_CANNOT_BE_BLANK}")
  private String keyFieldCannotBeBlank;

  @Value("${ALPHA_NUMERIC_AND_SPACE_REGEX}")
  private String alphaNumericAndSpaceRegEx;

  /**
   * Validates a code
   * - checks if code contains a special character other than the set of allowed ones - as configured
   * in application.yml
   *
   * @param code - String
   * @throws CodeCannotContainSpecialCharacters - if code contains a special character
   */
  @Override
  public void validate(String code) throws CodeCannotContainSpecialCharacters {
    if (!code.matches(alphaNumericAndSpaceRegEx))
      throw new CodeCannotContainSpecialCharacters(codeCannotContainSpecialCharacters);
  }

  /**
   * Validates a code
   * - checks if it is null
   * - checks if it is blank (after trimming trailing spaces)
   * - checks if it contains a special character other than the set of allowed ones - as configured in
   * application.yml
   *
   * @param code - String
   * @return - valid String with trailing spaces removed.
   * @throws CodeMandatory                      - when code is null
   * @throws CodeCannotBeBlank                  - when code is blank after stripping the trailing spaces
   * @throws CodeCannotContainSpecialCharacters - when code contains a special character
   */
  public String validateAll(String code) throws CodeMandatory, CodeCannotBeBlank, CodeCannotContainSpecialCharacters {
    try {
      NullValidator.super.validate(code);
    } catch (Exception e) {
      throw new CodeMandatory(codeMandatory);
    }

    try {
      BlankValidatorPostRightTrim.super.validate(code);
    } catch (Exception e) {
      throw new CodeCannotBeBlank(codeCannotBeBlank);
    }

    if (!code.matches(alphaNumericAndSpaceRegEx)) {
      throw new CodeCannotContainSpecialCharacters(codeCannotContainSpecialCharacters);
    }
    return code.stripTrailing();
  }

  /**
   * Validates a code
   * - checks if it is null
   * - checks if it is blank (after trimming trailing spaces)
   *
   * @param code - String
   * @return - valid code with trailing spaces removed.
   * @throws KeyFieldMandatory     - when code is null
   * @throws KeyFieldCannotBeBlank - when code is blank (post right trim)
   */
  public String validateNullAndBlank(String code) throws KeyFieldMandatory, KeyFieldCannotBeBlank {
    try {
      NullValidator.super.validate(code);
    } catch (Exception exception) {
      throw new KeyFieldMandatory(keyFieldMandatory);
    }

    try {
      BlankValidatorPostRightTrim.super.validate(code);
    } catch (Exception exception) {
      throw new KeyFieldCannotBeBlank(keyFieldCannotBeBlank);
    }
    return code.stripTrailing();
  }

  /**
   * Validates length
   *
   * @param currentLength  - int
   * @param expectedLength - int
   * @throws MaxLengthExceeded - when currentLength exceeds expectedLength.
   */
  @Override
  public void validate(int currentLength, int expectedLength) throws MaxLengthExceeded {
    try {
      LengthValidator.super.validate(currentLength, expectedLength);
    } catch (Exception exception) {
      throw new MaxLengthExceeded(codeInvalidLength);
    }
  }
}