
package api.external.item.service;

import api.core.errors.KeyFieldCannotBeBlank;
import api.core.errors.KeyFieldMandatory;
import api.core.validation.CodeValidationService;
import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.entity.ItemRequest;
import api.external.item.entity.ResponseDetail;
import api.external.item.errors.ItemNotFound;
import api.external.item.repo.ItemRepository;
import api.external.item.repo.ItemRequestHistoryRepository;
import api.external.item.repo.ItemResponseHistoryRepository;
import api.external.item.requesthistory.ItemRequestHistory;
import api.external.item.responsehistory.ItemResponseHistory;
import api.external.item.validation.ItemValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This class provides various services for item
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-12
 */
@Service
@Transactional
public class ItemService {
  @Value("${ADD_REQUEST}")
  private String addRequest;

  @Value("${ITEM_NOT_FOUND}")
  private String itemNotFound;

  @Value("${ITEM_ADDED}")
  private String itemAdded;

  @Value("${ITEM_UPDATED}")
  private String itemUpdated;

  @Value("${TRANSACTION_NUMBER_MANDATORY}")
  private String transactionNumberMandatory;

  @Value("${TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
  private String transactionNumberCannotBeBlank;

  @Autowired
  private ItemRequestHistoryRepository itemRequestHistoryRepository;

  @Autowired
  private ItemResponseHistoryRepository itemResponseHistoryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemValidationService itemValidationService;

  @Autowired
  private CodeValidationService codeValidationService;

  /**
   * This method is used to get all items.
   *
   * @param pageNo   - pageNo to display. Default is 0, can be overridden by
   *                 caller.
   * @param pageSize - pageSize to display. Default is 10, can be overridden by
   *                 caller.
   * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be
   *                 overridden by caller.
   * @param orderBy  - orderBy - Default is descending, can be overridden by
   *                 caller.
   * @return Page<Item> - on success, returns a page of items (could be empty).
   * Otherwise, a global rest exception handler is automatically called
   * and a context-sensitive error message is displayed.
   */
  public Page<Item> getAllItems(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
      : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return itemRepository.findAll(paging);
  }

  /**
   * This method adds a new item or updates an existing item.
   *
   * @param itemRequest - request given by the user
   * @return ItemResponse - the response to be displayed to the end-user and used
   * by API Caller.
   */
  public ItemResponseHistory addOrUpdate(ItemRequest itemRequest) throws Exception {
    Item item = null;
    ResponseDetail responseDetail = new ResponseDetail();
    String transactionNumber = itemRequest.getTransactionNumber();

    try {
      ItemResponseHistory existingItemResponseHistory = itemResponseHistoryRepository.findByTransactionNumber(transactionNumber);

      if (existingItemResponseHistory != null) {
        return existingItemResponseHistory;
      }

      // rightTrim company, division and warehouse fields from the input
      ItemKey itemKey = sanitizeInput(itemRequest);

      item = new Item(itemKey,
        itemRequest.getSizeDesc(),
        itemRequest.getProductGroup(),
        itemRequest.getProductSubgroup(),
        itemRequest.getProductType(),
        itemRequest.getProductLine(),
        itemRequest.getSalesGroup(),
        itemRequest.getShelfDays(),
        itemRequest.getShipAlone(),
        itemRequest.getUnitOfMeasure(),
        itemRequest.getStockingUM(),
        itemRequest.getPurchasingUM(),
        itemRequest.getSellingUM(),
        itemRequest.getItemDesc(),
        itemRequest.getStyleDesc(),
        itemRequest.getColorDesc(),
        itemRequest.getLotControlUsed(),
        itemRequest.getSerialNumberUsed(),
        itemRequest.getPrice(),
        itemRequest.getRetlPrice(),
        itemRequest.getTicketType(),
        itemRequest.getSensorTagType(),
        itemRequest.getCountryOfOrigin(),
        itemRequest.getAcceptSkuByIdCodes(),
        itemRequest.getAcceptSkuByLotNumber(),
        itemRequest.getAcceptSkuByProductStatus(),
        itemRequest.getAcceptSkuByCountryOfOrigin(),
        itemRequest.getDateTimeStamp(),
        itemRequest.getUser());

      // store that item in request log
      itemRequestHistoryRepository.save(new ItemRequestHistory(0, transactionNumber, item));
      String action = itemRequest.getAction();
      responseDetail.setRequest(action);

      // validate item. If it already exists return it, otherwise, return null
      Item existingItem = itemValidationService.validate(item, action);
      String upperCaseAction = action.trim().toUpperCase();
      Item savedItem = null;

      String responseMessageId = null;
      // take necessary steps based on action
      if (upperCaseAction.equals(addRequest)) {
        responseMessageId = itemAdded;
        savedItem = itemRepository.save(item);
      } else {
        update(existingItem, item);
        responseMessageId = itemUpdated;
        savedItem = itemRepository.save(existingItem);
      }
      responseDetail.setResponseCode('S');
      responseDetail.setResponseId(responseMessageId);
      // store the saved item in the response log
      return itemResponseHistoryRepository.save(new ItemResponseHistory(0, transactionNumber, savedItem, responseDetail));
    } catch (Exception exception) {
      responseDetail.setResponseCode('F');
      responseDetail.setResponseId(exception.getMessage());
      return itemResponseHistoryRepository.save(new ItemResponseHistory(0, transactionNumber, item, responseDetail));
    }
  }

  private ItemKey sanitizeInput(ItemRequest itemRequest) {
    ItemKey itemKey = itemRequest.getId();
    String company = itemKey.getCompany();

    if (company != null) {
      itemKey.setCompany(company.stripTrailing());
    }
    String division = itemKey.getDivision();

    if (division != null) {
      itemKey.setDivision(division.stripTrailing());
    }
    String warehouse = itemKey.getWarehouse();

    if (warehouse != null) {
      itemKey.setWarehouse(warehouse.stripTrailing());
    }
    return itemKey;
  }

  /**
   * Updates an existing item with fields the user wants to update
   *
   * @param existingItem - item should be present
   * @param updatedItem  - updated item (with the fields the end-user wants to
   *                     update).
   */
  private void update(Item updatedItem, Item existingItem) {
    updatedItem.setSizeDesc(existingItem.getSizeDesc());
    updatedItem.setProductGroup(existingItem.getProductGroup());
    updatedItem.setProductSubgroup(existingItem.getProductSubgroup());
    updatedItem.setProductType(existingItem.getProductType());
    updatedItem.setProductLine(existingItem.getProductLine());
    updatedItem.setSalesGroup(existingItem.getSalesGroup());
    updatedItem.setShelfDays(existingItem.getShelfDays());
    updatedItem.setShipAlone(existingItem.getShipAlone());
    updatedItem.setUnitOfMeasure(existingItem.getUnitOfMeasure());
    updatedItem.setStockingUM(existingItem.getStockingUM());
    updatedItem.setPurchasingUM(existingItem.getPurchasingUM());
    updatedItem.setSellingUM(existingItem.getSellingUM());
    updatedItem.setItemDesc(existingItem.getItemDesc());
    updatedItem.setStyleDesc(existingItem.getStyleDesc());
    updatedItem.setColorDesc(existingItem.getColorDesc());
    updatedItem.setLotControlUsed(existingItem.getLotControlUsed());
    updatedItem.setSerialNumberUsed(existingItem.getSerialNumberUsed());
    updatedItem.setPrice(existingItem.getPrice());
    updatedItem.setRetlPrice(existingItem.getRetlPrice());
    updatedItem.setTicketType(existingItem.getTicketType());
    updatedItem.setSensorTagType(existingItem.getSensorTagType());
    updatedItem.setCountryOfOrigin(existingItem.getCountryOfOrigin());
    updatedItem.setAcceptSkuByIdCodes(existingItem.getAcceptSkuByIdCodes());
    updatedItem.setAcceptSkuByLotNumber(existingItem.getAcceptSkuByLotNumber());
    updatedItem.setAcceptSkuByProductStatus(existingItem.getAcceptSkuByProductStatus());
    updatedItem.setAcceptSkuByCountryOfOrigin(existingItem.getAcceptSkuByCountryOfOrigin());
  }

  /**
   * @param itemKey - itemKey based on which you want to retrieve the item from
   *                the db.
   * @return - the item found.
   * @throws Exception - suitable exception if the item is not found.
   */
  public Item getItem(ItemKey itemKey) throws Exception {
    Optional<Item> item = itemRepository.findById(itemKey);

    if (item.isEmpty()) {
      throw new ItemNotFound(itemNotFound);
    }
    return item.get();
  }
}