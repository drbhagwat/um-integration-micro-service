package api.external.channel.controller;

import api.external.channel.entity.ChannelAPI;
import api.external.channel.history.entity.ChannelResponseHistoryHeader;
import api.external.channel.service.ChannelAPIService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Manages (Read and Update) operations of sku_inventory and manufacturing_inventory tables.
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-05-23
 */
@RestController
public class ChannelAPIController {
  @Autowired
  private ChannelAPIService channelAPIService;

  /**
   * @param channelAPI - the channelAPI, which you want to process.
   * @return - on success, returns HTTP status code 200. On failure,
   * returns HTTP status code 400 (bad request).
   * @throws Exception - on failure, an exception is thrown and a meaningful error message
   *                   is displayed.
   */
  @PostMapping("/channel")
  public ResponseEntity<?> channel(@RequestBody @Valid ChannelAPI channelAPI) throws Exception {
    ChannelResponseHistoryHeader response = channelAPIService.updateRequest(channelAPI);
    return ResponseEntity.ok(response);
  }
}