package api;

import api.core.entities.Channel;
import api.core.errors.*;
import api.core.repo.ChannelRepository;
import api.core.services.ChannelService;
import api.core.validation.ChannelValidationService;
import api.core.validation.CodeValidationService;
import api.core.validation.NameValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * This Class has all the unit test cases for the Channel service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-02-05
 */
@RunWith(SpringRunner.class)
public class ChannelTest {
  @InjectMocks
  private ChannelService channelService;

  @Mock
  private ChannelValidationService channelValidationService;

  @Mock
  private NameValidationService nameValidationService;

  @Mock
  private CodeValidationService codeValidationService;

  @Mock
  private ChannelRepository channelRepository;

  private Channel channel;

  private static final int pageNo = 0;
  private static final int pageSize = 10;
  private static final String sortBy = "lastUpdatedDateTime";
  private static final String orderBy = "D";

  /**
   * Sets up the necessary data structures to be used in all unit tests in one place.
   */
  @Before
  public void setUp() {
    channel = new
        Channel("Web", "Web Channel");
  }

  /**
   * Tests the getAll method of channel service.
   */
  @Test
  public void testGetAll() {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    Page<Channel> channelPage = new PageImpl<>(Arrays.asList(), pageable, Arrays.asList().size());
    Assertions.assertNotNull(when(channelRepository.findAll(pageable)).thenReturn(channelPage));
  }

  /**
   * Tests the get method of the channel service, when the channel is not in the db.
   */
  @Test
  public void testGetWhenChannelNotInDb() {
    when(channelRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ChannelNotFound.class, () -> {
      channelService.get(channel.getId());
    });
  }

  /**
   * Tests the get method of the channel service, when the channel is already in the db.
   */
  @Test
  public void testGetWhenChannelInDb() {
    Assert.assertNotNull(when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel)));
  }

  /**
   * Tests the add method of the channel service, when the channelCode has special character.
   */
  @Test
  public void testAddWithSpecialCharacterInChannelCode() throws ChannelCodeCannotContainSpecialCharacters,
      ChannelCodeMaxLengthExceeded {
    when(channelValidationService.validate(channel.getId())).thenThrow(ChannelCodeCannotContainSpecialCharacters.class);
    Assertions.assertThrows(ChannelCodeCannotContainSpecialCharacters.class, () -> {
      channelService.add(channel);
    });
  }

  /**
   * Tests the add method of the channel service, when the channelCode having more than expected length.
   */
  @Test
  public void testAddWithMoreThanExpectedLengthInChannelCode() throws ChannelCodeCannotContainSpecialCharacters,
      ChannelCodeMaxLengthExceeded {
    when(channelValidationService.validate(channel.getId())).thenThrow(ChannelCodeMaxLengthExceeded.class);
    Assertions.assertThrows(ChannelCodeMaxLengthExceeded.class, () -> {
      channelService.add(channel);
    });
  }

  /**
   * Tests the add method of the channel service, when the channel is present in the db.
   */
  @Test
  public void testAddWhenChannelAlreadyInDb() {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));
    Assertions.assertThrows(ChannelAlreadyExists.class, () -> {
      channelService.add(channel);
    });
  }

  /**
   * Tests the add method of the channel service, when the channel is not in the db.
   */
  @Test
  public void testAddWhenChannelNotInDb() throws Exception {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertNotNull(when(channelService.add(channel)).thenReturn(channel));
  }

  /**
   * Tests the update method of the channel service, when the channel is present in the db.
   */
  @Test
  public void testUpdateWhenChannelInDb() throws Exception {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));
    Assertions.assertNotNull(when(channelService.update(channel.getId(), channel)).thenReturn(channel));
  }

  /**
   * Tests the update method of the channel service, when the channel is not in the db.
   */
  @Test
  public void testUpdateWhenChannelNotInDb() {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ChannelNotFound.class, () -> {
      channelService.update(channel.getId(), channel);
    });
  }

  /**
   * Tests the delete method of the channel service, when the channel is present in the db.
   */
  @Test
  public void testDeleteWhenChannelInDb() throws Exception {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));
    Assertions.assertTrue(channelService.delete(channel.getId()));
  }

  /**
   * Tests the delete method of the channel service, when the channel is not in the db.
   */
  @Test
  public void testDeleteWhenChannelNotInDb() {
    when(channelRepository.findById(channel.getId())).thenReturn(Optional.ofNullable(null));
    Assertions.assertThrows(ChannelNotFound.class, () -> {
      channelService.delete(channel.getId());
    });
  }
}