
package api.external.mfg.history.controller;

import api.external.mfg.history.entity.MfgHistorySearchCriteria;
import api.external.mfg.history.entity.MfgRequestHistorySku;
import api.external.mfg.history.service.MfgRequestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


/**
 * Manages read operations of mfg request history header and sku
 * tables.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */
@RestController
public class MfgRequestHistoryController {

	@Autowired
	private MfgRequestHistoryService mfgRequestHistoryService;
	
	/**
	 *  Retrieves mfgInvenory API RequestHistories based on the search parameters.
	 * 
	 * @param searchCriteria - contains json search fields 
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of mfgInventory API RequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@PostMapping("/mfgrequesthistorysearch")
	public Page<MfgRequestHistorySku> searchMfgRequestHistory(@RequestBody MfgHistorySearchCriteria searchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) {
		return mfgRequestHistoryService.searchMfgRequestHistory(searchCriteria, pageNo, pageSize, orderBy);
	}
	
	/**
	 * 
	 * Gets all mfgInventory API RequestHistories
	 * 
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of mfgInventory API RequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/mfgrequesthistory")
	public Page<MfgRequestHistorySku> getAllRequestHistorySku(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (mfgRequestHistoryService.getAllRequestHistory(pageNo, pageSize, sortBy, orderBy));
	}	
	
	/**
	 * Gets mfgInventory API RequestHistories by the transactionNumber.
	 * 
	 * @param transactionNumber - which is unique for each transaction.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return -  on success, returns a page of mfgInventory API RequestHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/mfgrequesthistory/{transactionNumber}")
	public Page<MfgRequestHistorySku> getAllRequestHistorySkuByTransactionNumber(@PathVariable String transactionNumber, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (mfgRequestHistoryService.getAllRequestHistorySkuByTransactionNumber(transactionNumber, pageNo, pageSize, sortBy, orderBy));
	}	
}
