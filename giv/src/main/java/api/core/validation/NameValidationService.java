package api.core.validation;

import api.core.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides channel Name validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-05-01
 */
@Service
public class NameValidationService {
  @Value("${NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String nameCannotContainSpecialCharacters;

  @Value("${NAME_INVALID_LENGTH}")
  private String nameInvalidLength;

  @Value("${ALPHA_NUMERIC_REGEX}")
  private String alphaNumericRegEx;

  /**
   * This method is to validate the given name to be empty or blank or special character check
   * 
   * @param name - json input of the given name(a string)
   * @throws Exception - when the string is null or when the string contains non alpha-numeric character.
   *                   However, blank or empty string is valid.
   */
  public void validate(String name) throws NameCannotContainSpecialCharacters {
    if (!name.matches(alphaNumericRegEx)) {
      throw new NameCannotContainSpecialCharacters(nameCannotContainSpecialCharacters);
    }
  }

  /**
   * Validates the length of the name field
   * 
   * @param length - code length
   * @param maxLength - expected length of the code
   * @throws MaxLengthExceeded -  on failure, when the length exceeds the expected length.
   *                  
   */
  public void validate(int length, int maxLength) throws MaxLengthExceeded {
    if (length > maxLength) {
      throw new MaxLengthExceeded(nameInvalidLength);
    }
  }
}