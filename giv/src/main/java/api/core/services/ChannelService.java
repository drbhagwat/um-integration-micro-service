package api.core.services;

import api.core.entities.Channel;
import api.core.errors.*;
import api.core.repo.ChannelRepository;
import api.core.validation.ChannelValidationService;
import api.core.validation.CodeValidationService;
import api.core.validation.NameValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Provides Create Read Update Delete (CRUD) services for Channel.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-02-05
 */

@Service
@Transactional
public class ChannelService {
  @Value("${CHANNEL_NOT_FOUND}")
  private String channelNotFound;

  @Value("${CHANNEL_ALREADY_EXISTS}")
  private String channelAlreadyExists;

  @Value("${CHANNEL_NAME_MAX_LENGTH}")
  private int channelNameMaxLength;

  @Value("${CHANNEL_NAME_INVALID_LENGTH}")
  private String channelNameInvalidLength;

  @Value("${CHANNEL_NAME_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
  private String channelNameCannotContainSpecialCharacters;

  @Autowired
  CodeValidationService codeValidationService;

  @Autowired
  NameValidationService nameValidationService;

  @Autowired
  private ChannelValidationService channelValidationService;

  @Autowired
  private ChannelRepository channelRepository;

  /**
   * Gets the first page of found channels (empty when there is no channel).
   *
   * @param pageNo   - number of the page
   * @param pageSize - size of the page
   * @param sortBy   - sort order (ascending or descending)
   * @param orderBy  - how to order the result
   * @return - first page of the channels found.
   */
  public Page<Channel> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return channelRepository.findAll(paging);
  }

  /**
   * Retrieves the channel matching the Primary Key (PK) from the database.
   *
   * @param channelKey - PK of channel.
   * @return - on success, returns the found channel.
   * @throws Exception - on failure, throws ChannelNotFound exception.
   */
  public Channel get(String channelKey) throws ChannelNotFound {
    Optional<Channel> channel = channelRepository.findById(channelKey);

    if (channel.isEmpty()) {
      throw new ChannelNotFound(channelNotFound);
    }
    return channel.get();
  }

  /**
   * Adds a channel to the database
   *
   * @param channel - the channel to be added to the database.
   * @return - on success, returns the saved channel.
   * @throws ChannelCodeCannotContainSpecialCharacters, ChannelCodeMaxLengthExceeded,
   *                                                    ChannelAlreadyExists, ChannelNameCannotContainSpecialCharacters, ChannelNameMaxLengthExceeded
   *                                                    - on failure, throws an appropriate exception.
   */
  public Channel add(Channel channel) throws ChannelCodeCannotContainSpecialCharacters, ChannelCodeMaxLengthExceeded,
      ChannelAlreadyExists, ChannelNameCannotContainSpecialCharacters, ChannelNameMaxLengthExceeded {
    String channelKey = channel.getId();
    channelValidationService.validate(channelKey);
    channelKey = channelKey.stripTrailing();
    Optional<Channel> existingChannel = channelRepository.findById(channelKey);

    if (!existingChannel.isEmpty()) throw new ChannelAlreadyExists(channelAlreadyExists);

    try {
      String name = channel.getName();
      nameValidationService.validate(name);
      nameValidationService.validate(name.length(), channelNameMaxLength);
      name = name.stripTrailing();
      channel.setId(channelKey);
      channel.setName(name);
      return channelRepository.save(channel);
    } catch (NameCannotContainSpecialCharacters exception) {
      throw new ChannelNameCannotContainSpecialCharacters(channelNameCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new ChannelNameMaxLengthExceeded(channelNameInvalidLength);
    }
  }

  /**
   * Updates an existing channel in the database.
   *
   * @param channelKey - PK of the channel to be updated.
   * @param channel    - the channel to be updated in the database.
   * @return - on success, returns the updated channel.
   * @throws ChannelNameCannotContainSpecialCharacters,ChannelNameMaxLengthExceeded, ChannelNotFound
   *                                                                                 - on failure, throws an appropriate exception.
   */
  public Channel update(String channelKey, Channel channel) throws ChannelNameCannotContainSpecialCharacters,
      ChannelNameMaxLengthExceeded, ChannelNotFound {
    Channel existingChannel = get(channelKey);

    try {
      String name = channel.getName();
      nameValidationService.validate(name);
      codeValidationService.validate(name.length(), channelNameMaxLength);
      existingChannel.setName(channel.getName());
      return channelRepository.save(existingChannel);
    } catch (NameCannotContainSpecialCharacters exception) {
      throw new ChannelNameCannotContainSpecialCharacters(channelNameCannotContainSpecialCharacters);
    } catch (MaxLengthExceeded exception) {
      throw new ChannelNameMaxLengthExceeded(channelNameInvalidLength);
    }
  }

  /**
   * Deletes an existing channel from the database.
   *
   * @param channelKey - PK of the channel to be deleted.
   * @throws ChannelNotFound - on failure, throws ChannelNotFound exception.
   */
  public boolean delete(String channelKey) throws ChannelNotFound {
    channelRepository.delete(get(channelKey));
    return true;
  }
}