package api.core.validation;

/**
 *
 * Validates if a string's currentLength is more than its expectedLength.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-01-29
 *
 */
interface LengthValidator {
  /**
   *
   * @param currentLength - int
   * @param expectedLength - int
   * @throws Exception - - when currentLength exceeds expectedLength.
   */
  default void validate(int currentLength, int expectedLength) throws Exception {
    if (currentLength > expectedLength) {
      throw new Exception();
    }
  }
}
