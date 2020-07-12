
package api.external.channel.history.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.channel.history.entity.ChannelRequestHistorySku;
import api.external.channel.history.entity.HistorySearchCriteria;
import api.external.channel.history.service.ChannelRequestHistoryService;

/**
 * Performs Read and Search operations of channel request history header and channel request history sku.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-18
 * 
 *
 */
@RestController
public class ChannelRequestHistoryController {

	@Autowired
	private ChannelRequestHistoryService channelRequestHistoryService;
	
	/**
	 * Retrieves specific channel channelRequestHistories based on the search parameters.
	 * 
	 * @param channel - code of the channel (i.e. web, catalog, retail)
	 * @param searchCriteria - Json class to represents search parameters
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of channelRequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@PostMapping("/channelrequesthistorysearch/{channel}")
	public Page<ChannelRequestHistorySku> searchChannelRequestHistory(@PathVariable String channel, @RequestBody HistorySearchCriteria searchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) {
		return channelRequestHistoryService.searchChannelRequestHistory(channel, searchCriteria, pageNo, pageSize, orderBy);
	}
	
	/**
	 * 
	 * Gets all channelRequestHistories for a specific channel
	 * 
	 * @param channel - code of the channel (i.e. web, catalog, retail)
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of channelRequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/channelrequesthistorygetall/{channel}")
	public Page<ChannelRequestHistorySku> getAllRequestHistorySkuByChannel(@PathVariable String channel, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (channelRequestHistoryService.getAllRequestHistorySkuByChannel(channel, pageNo, pageSize, sortBy, orderBy));
	}	
	
	/**
	 * Gets channelRequestHistories by the transactionNumber.
	 * 
	 * @param transactionNumber - which is unique for each transaction.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return -  on success, returns a page of channelRequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/channelrequesthistory/{transactionNumber}")
	public Page<ChannelRequestHistorySku> getAllRequestHistorySkuByTransactionNumber(@PathVariable String transactionNumber, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (channelRequestHistoryService.getAllRequestHistorySkuByTransactionNumber(transactionNumber, pageNo, pageSize, sortBy, orderBy));
	}	
}
