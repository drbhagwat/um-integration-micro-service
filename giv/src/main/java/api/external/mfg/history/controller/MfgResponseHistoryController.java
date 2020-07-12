
package api.external.mfg.history.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.mfg.history.entity.MfgHistorySearchCriteria;
import api.external.mfg.history.entity.MfgResponseHistorySku;
import api.external.mfg.history.service.MfgResponseHistoryService;


/**
 * Manages read operations of mfg response history header and sku
 * tables.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-11-01
 */
@RestController
public class MfgResponseHistoryController {

	@Autowired
	private MfgResponseHistoryService mfgResponseHistoryService;
	
	/**
	 *  Retrieves mfgInvenory API ResponseHistories based on the search parameters.
	 * 
	 * @param searchCriteria - contains json search fields 
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of mfgInventory API ResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@PostMapping("/mfgresponsehistorysearch")
	public Page<MfgResponseHistorySku> searchMfgRequestHistory(@RequestBody MfgHistorySearchCriteria searchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) {
		return mfgResponseHistoryService.searchMfgRequestHistory(searchCriteria, pageNo, pageSize, orderBy);
	}
	
	/**
	 * 
	 * Gets all mfgInventory API ResponseHistories
	 * 
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, returns a page of mfgInventory API ResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/mfgresponsehistory")
	public Page<MfgResponseHistorySku> getAllRequestHistory(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (mfgResponseHistoryService.getAllResponseHistory(pageNo, pageSize, sortBy, orderBy));
	}
	
	/**
	 * Gets mfgInventory API ResponseHistories by the transactionNumber.
	 * 
	 * @param transactionNumber - which is unique for each transaction.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return -  on success, returns a page of mfgInventory API ResponseHistories (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	@GetMapping("/mfgresponsehistory/{transactionNumber}")
	public Page<MfgResponseHistorySku> getAllRequestHistorySkuByTransactionNumber(@PathVariable String transactionNumber, @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (mfgResponseHistoryService.getAllResponseHistorySkuByTransactionNumber(transactionNumber, pageNo, pageSize, sortBy, orderBy));
	}	
}
