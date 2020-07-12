
package api.external.channel.history.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.channel.history.entity.ChannelResponseHistorySku;
import api.external.channel.history.entity.HistorySearchCriteria;
import api.external.channel.history.service.ChannelResponseHistoryService;

/**
 * Manages Read and search operations of channel response history header and channel response history sku.
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-18
 *
 */
@RestController
public class ChannelResponseHistoryController {

	@Autowired
	private ChannelResponseHistoryService channelResponseHistoryService;

	/**
	 * Retrieves specific channel channelResponseHistories based on the search parameters.
	 * 
	 * @param channel - code of the channel (i.e. web, catalog, retail)
	 * @param searchCriteria - Json class to represents search parameters
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of channelResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@PostMapping("/channelresponsehistorysearch/{channel}")
	public Page<ChannelResponseHistorySku> searchChannelResponseHistory(@PathVariable String channel, @RequestBody HistorySearchCriteria searchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) {
		return channelResponseHistoryService.searchChannelResponseHistory(channel, searchCriteria, pageNo, pageSize, orderBy);
	}
	
	/**
	 * 
	 * Gets all channelResponseHistories for a specific channel
	 * 
	 * @param channel - code of the channel (i.e. web, catalog, retail)
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of channelResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/channelresponsehistorygetall/{channel}")
	public Page<ChannelResponseHistorySku> getAllResponseHistorySkuByChannel(@PathVariable String channel, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (channelResponseHistoryService.getAllResponseHistorySkuByChannel(channel, pageNo, pageSize, sortBy, orderBy));
	}
	
	/**
	 * Gets channelResponseHistories by the transactionNumber.
	 * 
	 * @param transactionNumber - which is unique for each transaction.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return -  on success, returns a page of channelResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/channelresponsehistory/{transactionNumber}")
	public Page<ChannelResponseHistorySku> getAllResponsetHistorySkuByTransactionNumber(@PathVariable String transactionNumber, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (channelResponseHistoryService.getAllResponseHistorySkuByTransactionNumber(transactionNumber, pageNo, pageSize, sortBy, orderBy));
	}	
}
