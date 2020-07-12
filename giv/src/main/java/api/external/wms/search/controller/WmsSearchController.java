package api.external.wms.search.controller;

import api.external.inventory.entity.SkuInventory;
import api.external.wms.entity.WmsRequestDetail;
import api.external.wms.entity.WmsResponseDetail;
import api.external.wms.repo.WmsRequestDetailRepository;
import api.external.wms.repo.WmsResponseDetailRepository;
import api.external.wms.search.entity.WmsHistorySearchCriteria;
import api.external.wms.search.entity.WmsInventorySearchCriteria;
import api.external.wms.search.service.WmsInventorySearchService;
import api.external.wms.service.WmsRequestHistoryService;
import api.external.wms.service.WmsResponseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WmsSearchController {
    @Autowired
    private WmsInventorySearchService wmsInventorySearchService;

    @Autowired
    private WmsRequestHistoryService wmsRequestHistoryService;

    @Autowired
    private WmsResponseHistoryService wmsResponseHistoryService;

    @Autowired
    private WmsRequestDetailRepository wmsRequestDetailRepository;

    @Autowired
    private WmsResponseDetailRepository wmsResponseDetailRepository;

    /**
     * @param wmsInvSearchCriteria   - This is the request body
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns pages of all request histories related to the apiName.
     */
    @PostMapping("/wmsinventorysearch")
    public Page<SkuInventory> searchInventory(@RequestBody WmsInventorySearchCriteria wmsInvSearchCriteria,
                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(defaultValue = "D") String orderBy) {
        return wmsInventorySearchService.searchWmsInventory(wmsInvSearchCriteria, pageNo, pageSize, orderBy);
    }

    /**
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param sortBy   - default is lastUpdatedDateTime, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns pages of all request histories related to the apiName.
     */
    @GetMapping("/requesthistorygetall/wms")
    public Page<WmsRequestDetail> getAllWmsRequestHistory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "last_updated_date_time") String sortBy,
                                                          @RequestParam(defaultValue = "D") String orderBy) {
        return wmsRequestHistoryService.getAll(pageNo, pageSize, sortBy, orderBy);
    }

    /**
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param sortBy   - default is lastUpdatedDateTime, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns pages of all request histories related to the apiName.
     */
    @GetMapping("responsehistorygetall/wms")
    public Page<WmsResponseDetail> getAllWmsResponseHistory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "last_updated_date_time") String sortBy,
                                                            @RequestParam(defaultValue = "D") String orderBy) {
        return wmsResponseHistoryService.getAll(pageNo, pageSize, sortBy, orderBy);
    }

    /**
     * @param wmsHistorySearchCriteria - This is the Request body.
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns pages of all request histories related to the apiName.
     */
    @PostMapping("/wmsrequestsearch")
    public Page<WmsRequestDetail> searchWmsRequestLogHistory(@RequestBody WmsHistorySearchCriteria wmsHistorySearchCriteria,
                                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "D") String orderBy) {
        return wmsRequestHistoryService.searchWmsRequestHistory(wmsHistorySearchCriteria, pageNo, pageSize, orderBy);
    }

    /**
     * @param wmsHistorySearchCriteria - This is the Request body.
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns pages of all request histories related to the apiName.
     */
    @PostMapping("/wmsresponsesearch")
    public Page<WmsResponseDetail> searchWmsResponseLogHistory(@RequestBody WmsHistorySearchCriteria wmsHistorySearchCriteria,
                                                               @RequestParam(defaultValue = "0") Integer pageNo,
                                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                                               @RequestParam(defaultValue = "D") String orderBy) {
        return wmsResponseHistoryService.searchWmsResponseHistory(wmsHistorySearchCriteria, pageNo, pageSize, orderBy);
    }

    /**
     * @param transactionNumber - This is the Request parameter.
     * @return - on success, returns Request details by Transaction Number.
     */
    @GetMapping("/wmsrequesthistory/{transactionNumber}")
    public WmsRequestDetail getRequestHistory(@PathVariable String transactionNumber) {
        return wmsRequestDetailRepository.findByTransactionNumber(transactionNumber);
    }

    /**
     * @param transactionNumber - This is the Request parameter.
     * @return - on success, returns Response details by Transaction Number.
     */
    @GetMapping("/wmsresponsehistory/{transactionNumber}")
    public WmsResponseDetail getResponseHistory(@PathVariable String transactionNumber) {
        return wmsResponseDetailRepository.findByTransactionNumber(transactionNumber);
    }
}
