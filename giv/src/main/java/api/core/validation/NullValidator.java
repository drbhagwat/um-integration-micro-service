package api.core.validation;

/**
 * Validates if the given string is null.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-01-29
 */
interface NullValidator {
  /**
   *
   * @param value - String
   * @throws Exception - when it is null.
   */
  default void validate(String value) throws Exception {
    if (value == null) {
      throw new Exception();
    }
  }
}
