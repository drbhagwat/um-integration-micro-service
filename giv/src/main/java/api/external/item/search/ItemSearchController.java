package api.external.item.search;

import api.external.item.entity.Item;
import api.external.item.repo.ItemRequestHistoryRepository;
import api.external.item.repo.ItemResponseHistoryRepository;
import api.external.item.requesthistory.ItemRequestHistory;
import api.external.item.requesthistory.SearchCriteria;
import api.external.item.responsehistory.ItemResponseHistory;
import api.external.item.service.ItemRequestHistoryService;
import api.external.item.service.ItemResponseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
public class ItemSearchController {

  @Autowired
  private ItemSearchService itemSearchService;

  @Autowired
  private ItemRequestHistoryService itemRequestHistoryService;

  @Autowired
  private ItemResponseHistoryService itemResponseHistoryService;

  @Autowired
  private ItemRequestHistoryRepository itemRequestHistoryRepository;

  @Autowired
  private ItemResponseHistoryRepository itemResponseHistoryRepository;

  @PostMapping("/itemsearch")
  public Page<Item> searchItem(@RequestBody ItemSearchCriteria itemSearchCriteria,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "D") String orderBy) {
    return itemSearchService.search(itemSearchCriteria, pageNo, pageSize, orderBy);
  }

  @PostMapping("/itemrequestsearch")
  public Page<ItemRequestHistory> requestSearch(@RequestBody SearchCriteria searchCriteria,
                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "D") String orderBy) {
    return itemSearchService.requestSearch(searchCriteria, pageNo, pageSize, orderBy);
  }

  @PostMapping("/itemresponsesearch")
  public Page<ItemResponseHistory> responseSearch(@RequestBody SearchCriteria searchCriteria,
                                                  @RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "D") String orderBy) {
	  return itemSearchService.responseSearch(searchCriteria, pageNo, pageSize, orderBy);
  }

  /**
   * @param pageNo   - default is 0, can be overridden by caller.
   * @param pageSize - default is 10, can be overridden by caller.
   * @param sortBy   - default is lastUpdatedDateTime, can be overridden by caller.
   * @param orderBy  - default is descending, can be overridden by caller.
   * @return - on success, returns pages of all request histories related to the apiName.
   */
  @GetMapping("/requesthistorygetall/items")
  public Page<ItemRequestHistory> getAllRecords(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "last_updated_date_time") String sortBy,
                                                @RequestParam(defaultValue = "D") String orderBy) {
    return itemRequestHistoryService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  @GetMapping("/responsehistorygetall/items")
  public Page<ItemResponseHistory> getAllItemResponseHistory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "last_updated_date_time") String sortBy, @RequestParam(defaultValue = "D") String orderBy) {
    return itemResponseHistoryService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  @GetMapping("/itemrequesthistory/{transactionNumber}")
  public ItemRequestHistory getRequestHistory(@PathVariable String transactionNumber)  {
    return itemRequestHistoryRepository.findByTransactionNumber(transactionNumber);
  }

  @GetMapping("/itemresponsehistory/{transactionNumber}")
  public ItemResponseHistory getResponseHistory(@PathVariable String transactionNumber)  {
    return itemResponseHistoryRepository.findByTransactionNumber(transactionNumber);
  }
}
