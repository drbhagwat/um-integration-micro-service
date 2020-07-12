package api.core.validation;

import api.core.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides channel code validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-05-01
 */
@Service
public class ChannelValidationService {
  @Value("${CHANNEL_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS")
  private String channelCodeCannotContainSpecialCharcaters;

  @Value("${CHANNEL_CODE_INVALID_LENGTH")
  private String channelCodeInvalidLength;

  @Value("${CHANNEL_CODE_MAX_LENGTH}")
  private int channelCodeMaxLength;

  @Autowired
  private CodeValidationService codeValidationService;

  /**
   * Validates special characters and length checks of the channelCode.
   *
   * @param channelCode - primary key of the channel
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */

  /**
   *
   * @param channelCode - primary key of the channel
   * @return - valid channelCode after right trimming
   * @throws ChannelCodeCannotContainSpecialCharacters - if the channelCode contains a non-allowed character
   * @throws ChannelCodeMaxLengthExceeded - if the channelCode has a length more than the expected length
   */
  public String validate(String channelCode) throws ChannelCodeCannotContainSpecialCharacters,
      ChannelCodeMaxLengthExceeded {
    try {
      codeValidationService.validate(channelCode);
      codeValidationService.validate(channelCode.length(), channelCodeMaxLength);
      return channelCode.stripTrailing();
    } catch (CodeCannotContainSpecialCharacters codeCannotContainSpecialCharactersException) {
      throw new ChannelCodeCannotContainSpecialCharacters(channelCodeCannotContainSpecialCharcaters);
    } catch (MaxLengthExceeded maxLengthExceededException) {
      throw new ChannelCodeMaxLengthExceeded(channelCodeInvalidLength);
    }
  }
}