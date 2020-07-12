
package api.external.item.search;

import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.entity.ItemLogSearch;
import api.external.item.repo.ItemRepository;
import api.external.item.repo.ItemRequestHistoryRepository;
import api.external.item.repo.ItemResponseHistoryRepository;
import api.external.item.requesthistory.ItemRequestHistory;
import api.external.item.requesthistory.SearchCriteria;
import api.external.item.responsehistory.ItemResponseHistory;
import api.external.item.validation.SearchValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemSearchService {
  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemRequestHistoryRepository itemRequestHistoryRepository;

  @Autowired
  private ItemResponseHistoryRepository itemResponseHistoryRepository;

  @Autowired
  private SearchValidationService searchValidationService;

  private static final String sortBy = "last_updated_date_time";

  public Page<Item> search(ItemSearchCriteria itemSearchCriteria, Integer pageNo, Integer pageSize,
                           String orderBy) {
    String skuBarcode = itemSearchCriteria.getSkuBarcode();
    String style = itemSearchCriteria.getStyle();
    String styleSuffix = itemSearchCriteria.getStyleSfx();
    String color = itemSearchCriteria.getColor();
    String colorSfx = itemSearchCriteria.getColorSfx();
    String sizeRngeCode = itemSearchCriteria.getSizeRngeCode();

    // handle both wild card and when the search parameter is not present in search
    // criteria
    if ((skuBarcode == null) || skuBarcode.equals("*") || skuBarcode.trim().equals("")) {
      skuBarcode = "";
    }

    if ((style == null) || style.equals("*") || style.trim().equals("")) {
      style = "";
    }

    if ((styleSuffix == null) || styleSuffix.equals("*") || styleSuffix.trim().equals("")) {
      styleSuffix = "";
    }

    if ((color == null) || color.equals("*") || color.trim().equals("")) {
      color = "";
    }

    if ((colorSfx == null) || colorSfx.equals("*") || colorSfx.trim().equals("")) {
      colorSfx = "";
    }

    if ((sizeRngeCode == null) || sizeRngeCode.equals("*") || sizeRngeCode.trim().equals("")) {
      sizeRngeCode = "";
    }
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return itemRepository.search(paging, skuBarcode, style, styleSuffix, color, colorSfx, sizeRngeCode);
  }

  private ItemRequestHistory initItemRequestHistory(ItemRequestHistory itemRequestHistory, Item item) {
    ItemRequestHistory response = new ItemRequestHistory();
    response.setId(itemRequestHistory.getId());
    response.setTransactionNumber(itemRequestHistory.getTransactionNumber());
    response.setCreatedDateTime(itemRequestHistory.getCreatedDateTime());
    response.setLastUpdatedDateTime(itemRequestHistory.getLastUpdatedDateTime());
    response.setCreatedUser(itemRequestHistory.getCreatedUser());
    response.setLastUpdatedUser(itemRequestHistory.getLastUpdatedUser());
    response.setItem(item);
    return response;
  }

  private ItemResponseHistory initItemResponseHistory(ItemResponseHistory itemResponseHistory, Item item) {
    ItemResponseHistory response = new ItemResponseHistory();
    response.setId(itemResponseHistory.getId());
    response.setTransactionNumber(itemResponseHistory.getTransactionNumber());
    response.setCreatedDateTime(itemResponseHistory.getCreatedDateTime());
    response.setLastUpdatedDateTime(itemResponseHistory.getLastUpdatedDateTime());
    response.setCreatedUser(itemResponseHistory.getCreatedUser());
    response.setLastUpdatedUser(itemResponseHistory.getLastUpdatedUser());
    response.setItem(item);
    response.setResponseDetail(itemResponseHistory.getResponseDetail());
    return response;
  }

  Page<ItemRequestHistory> requestSearch(SearchCriteria searchCriteria, Integer pageNo, Integer pageSize,
                                                String orderBy) {
    String transactionNumber = searchCriteria.getTransactionNumber();
    ItemLogSearch itemLogSearch = searchValidationService.getRefinedSearch(searchCriteria);
    String userSuppliedDivision = itemLogSearch.getDivision();
    String userSuppliedWarehouse = itemLogSearch.getWarehouse();
    String skuBarcode = itemLogSearch.getSkuBarcode();
    LocalDate ldtUserSuppliedStartDate = itemLogSearch.getLdtUserSuppliedStartDate();
    LocalDate ldtUserSuppliedEndDate = itemLogSearch.getLdtUserSuppliedEndDate();
    List<ItemRequestHistory> itemRequestHistoryList = null;

    if ((transactionNumber == null) || transactionNumber.equals("*") || (transactionNumber.trim().equals(""))) {
      itemRequestHistoryList = itemRequestHistoryRepository.findAll();
    } else {
      itemRequestHistoryList = itemRequestHistoryRepository.findListByTransactionNumber(transactionNumber);
    }
    List<ItemRequestHistory> responseList = new ArrayList<>();

    for (ItemRequestHistory itemRequestHistory : itemRequestHistoryList) {
      Item item = itemRequestHistory.getItem();
      ItemKey itemkey = item.getId();
      LocalDate date = item.getDateTimeStamp().toLocalDate();
      boolean isDateInBetweenAndInclusive = (ldtUserSuppliedStartDate.isBefore(date) || ldtUserSuppliedStartDate.isEqual(date)) &&
          (ldtUserSuppliedEndDate.isAfter(date) || ldtUserSuppliedEndDate.isEqual(date)) ;
      String division = itemkey.getDivision();
      String warehouse = itemkey.getWarehouse();

      switch (itemLogSearch.getFinalCompare()) {
        case 1:
          if (division.equalsIgnoreCase(userSuppliedDivision) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 2:
          if (warehouse.equalsIgnoreCase(userSuppliedWarehouse) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 3:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (warehouse.equalsIgnoreCase(userSuppliedWarehouse)) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 4:
          if ((itemkey.getSkuBarcode().contains(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 5:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (itemkey.getSkuBarcode().equalsIgnoreCase(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 6:
          if ((warehouse.equalsIgnoreCase(userSuppliedWarehouse)) &&
              (itemkey.getSkuBarcode().equalsIgnoreCase(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        case 7:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (warehouse.equalsIgnoreCase(userSuppliedWarehouse)) &&
              ((itemkey.getSkuBarcode().contains(skuBarcode)) && isDateInBetweenAndInclusive)) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
        default:
          if (isDateInBetweenAndInclusive) {
            ItemRequestHistory response = initItemRequestHistory(itemRequestHistory, item);
            responseList.add(response);
          }
          break;
      }
    }
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return new PageImpl<>(responseList, paging, responseList.size());
  }

  Page<ItemResponseHistory> responseSearch(SearchCriteria searchCriteria, Integer pageNo,
                                                  Integer pageSize, String orderBy) {
    String transactionNumber = searchCriteria.getTransactionNumber();
    ItemLogSearch itemLogSearch = searchValidationService.getRefinedSearch(searchCriteria);
    String userSuppliedDivision = itemLogSearch.getDivision();
    String userSuppliedWarehouse = itemLogSearch.getWarehouse();
    String skuBarcode = itemLogSearch.getSkuBarcode();
    LocalDate ldtUserSuppliedStartDate = itemLogSearch.getLdtUserSuppliedStartDate();
    LocalDate ldtUserSuppliedEndDate = itemLogSearch.getLdtUserSuppliedEndDate();

    List<ItemResponseHistory> itemResponseHistoryList = null;

    if ((transactionNumber == null) || transactionNumber.equals("*") || (transactionNumber.trim().equals(""))) {
      itemResponseHistoryList = itemResponseHistoryRepository.findAll();
    } else {
      itemResponseHistoryList = itemResponseHistoryRepository.findListByTransactionNumber(transactionNumber);
    }
    List<ItemResponseHistory> responseList = new ArrayList<>();

    for (ItemResponseHistory itemResponseHistory : itemResponseHistoryList) {
      Item item = itemResponseHistory.getItem();
      ItemKey itemkey = item.getId();
      LocalDate date = item.getDateTimeStamp().toLocalDate();
      String division = itemkey.getDivision();
      String warehouse = itemkey.getWarehouse();

      boolean isDateInBetweenAndInclusive = (ldtUserSuppliedStartDate.isBefore(date) || ldtUserSuppliedStartDate.isEqual(date)) &&
          (ldtUserSuppliedEndDate.isAfter(date) || ldtUserSuppliedEndDate.isEqual(date)) ;

      switch (itemLogSearch.getFinalCompare()) {
        case 1:
          if (division.equalsIgnoreCase(userSuppliedDivision) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 2:
          if (warehouse.equalsIgnoreCase(userSuppliedWarehouse) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 3:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (warehouse.equalsIgnoreCase(userSuppliedWarehouse)) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 4:
          if ((itemkey.getSkuBarcode().contains(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 5:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (itemkey.getSkuBarcode().equalsIgnoreCase(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 6:
          if ((warehouse.equalsIgnoreCase(userSuppliedWarehouse)) &&
              (itemkey.getSkuBarcode().equalsIgnoreCase(skuBarcode)) && isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        case 7:
          if ((division.equalsIgnoreCase(userSuppliedDivision)) &&
              (warehouse.equalsIgnoreCase(userSuppliedWarehouse)) &&
              ((itemkey.getSkuBarcode().contains(skuBarcode)) && isDateInBetweenAndInclusive)) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
        default:
          if (isDateInBetweenAndInclusive) {
            ItemResponseHistory response = initItemResponseHistory(itemResponseHistory, item);
            responseList.add(response);
          }
          break;
      }
    }
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return new PageImpl<>(responseList, paging, responseList.size());
  }
}