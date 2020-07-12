package api.core.controllers;

import api.core.entities.Channel;
import api.core.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Manages CRUD (Create Read Update Delete) operations of Channel.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@RestController
public class ChannelController {
  @Value("${CHANNEL_ADDED}")
  private String channelAdded;

  @Value("${CHANNEL_UPDATED}")
  private String channelUpdated;

  @Value("${CHANNEL_DELETED}")
  private String channelDeleted;

  @Autowired
  private ChannelService channelService;

  /**
   * Gets the first page of channels found (empty when there is no channel).
   * 
   * @param pageNo   - default is 0, can be overridden.
   * @param pageSize - default is 10, can be overridden.
   * @param sortBy   - default is descending, can be overridden.
   * @param orderBy  - default is by last updated date time, can be overridden.
   * @return - first page of the channels found.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/channels")
  public Page<Channel> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                              @RequestParam(defaultValue = "D") String orderBy) throws Exception {
    return channelService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * Gets a specific channel with matched primary key
   * 
   * @param channelCode - Primary Key (PK) of the channel
   * @return - on success, returns the found channel.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @GetMapping("/channels/{channelCode}")
  public Channel get(@PathVariable String channelCode) throws Exception {
    return channelService.get(channelCode);
  }

  /**
   * Adds a channel
   * 
   * @param channel - channel to be added
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PostMapping("/channels")
  public ResponseEntity<String> add(@RequestBody @Valid Channel channel) throws Exception {
    channelService.add(channel);
    return ResponseEntity.ok(channelAdded);
  }

  /**
   * Updates an existing channel
   * 
   * @param channelCode - primary key of the channel
   * @param channel - channel to be updated
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @PutMapping("/channels/{channelCode}")
  public ResponseEntity<String> update(@PathVariable String channelCode,
                                  @RequestBody @Valid Channel channel) throws Exception {
    channelService.update(channelCode, channel);
    return ResponseEntity.ok(channelUpdated);
  }

  /**
   * Deletes an existing channel matched with the primary key.
   * 
   * @param channelCode - primary keye of the channel
   * @return - on success, returns the success message.
   * @throws Exception - on failure, a global exception handler is called
   *                   which displays an appropriate error message.
   */
  @DeleteMapping("/channels/{channelCode}")
  public ResponseEntity<String> delete(@PathVariable String channelCode)
    throws Exception {
    channelService.delete(channelCode);
    return ResponseEntity.ok(channelDeleted);
  }
}