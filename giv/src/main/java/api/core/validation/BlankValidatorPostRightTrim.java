package api.core.validation;

/**
 * Validates if a string is empty, after trimming trailing spaces.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-01-29
 */
interface BlankValidatorPostRightTrim {
  /**
   * @param value - String
   * @throws Exception - when it is non-empty, after eliminating trailing spaces.
   */
  default void validate(String value) throws Exception {
    if (value.stripTrailing().isBlank()) {
      throw new Exception();
    }
  }
}
