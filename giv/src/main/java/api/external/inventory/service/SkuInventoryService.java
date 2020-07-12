package api.external.inventory.service;

import api.email.EmailService;
import api.external.campaign.service.UpdateProtectQtyInCampaign;
import api.external.errors.AvailableQtyIsZero;
import api.external.errors.SkuNotFound;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.error.AllocatedQtyLessThanRequestedQty;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.wms.entity.WmsInvRequest;
import api.external.wms.entity.WmsResponseDetail;
import api.external.wms.entity.WmsSku;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class provides various services for Sku Inventory
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2019-05-02
 */

@Slf4j
@Service
public class SkuInventoryService {
    @Autowired
    private SkuInventoryRepository skuInventoryRepository;

    @Autowired
    private UpdateProtectQtyInCampaign updateProtectQtyInCampaign;

    @Autowired
    private Environment env;

    @Autowired
    private InvTransactionService invTransactionService;

    @Autowired
    private EmailService emailService;

    @Value("${ALLOCATED_QTY_LESS_THAN_REQUESTED_QTY}")
    private String allocatedQtyLessThanRequestedQty;

    @Value("${SKU_NOT_FOUND}")
    private String skuNotFound;

    @Value("${AVAILABLE_QTY_IS_ZERO}")
    private String availableQtyIsZero;

    @Value("${SUBJECT_AVAILABLE_ZERO_ALERT}")
    private String subjectAvailableZeroAlert;

    @Value("${WMS_SKU_ADD_SUCCESSFUL}")
    private String wmsSkuAddSuccessful;

    @Value("${WMS_SKU_LOCK_SUCCESSFUL}")
    private String wmsSkuLockSuccessful;

    @Value("${WMS_SKU_UNLOCK_SUCCESSFUL}")
    private String wmsSkuUnLockSuccessful;

    @Value("${WMS_SKU_SUBTRACT_SUCCESSFUL}")
    private String wmsSkuSubtractSuccessful;

    @Value("${WMS_SKU_LOCK_PARTIAL_SUCCESS}")
    private String wmsSkuLockPartialSuccess;

    @Value("${WMS_SKU_UNLOCK_PARTIAL_SUCCESS}")
    private String wmsSkuUnlockPartialSuccess;

    @Value("${WMS_SKU_LOCK_FAILURE}")
    private String wmsSkuLockFailure;

    @Value("${WMS_SKU_UNLOCK_FAILURE}")
    private String wmsSkuUnLockFailure;

    @Value("${SUCCESS_RESPONSE}")
    private char success;

    @Value("${PARTIAL_RESPONSE}")
    private char partialSuccess;

    @Value("${INVALID_WMS_ACTION}")
    private String invalidWmsAction;

    @Value("${WMS_ACTIONS}")
    private String[] wmsActions;

    @Value("${WMS_SKU_CONSUME_SUCCESSFUL}")
    private String wmsSkuConsumeSuccessful;

    @Value("${WMS_SKU_CONSUME_PARTIAL_SUCCESS}")
    private String wmsSkuConsumePartialSuccess;

    @Value("${WMS_SKU_CONSUME_FAILURE}")
    private String wmsSkuConsumeFailure;

    @Value("${CONSUME}")
    private String consume;

    public Page<SkuInventory> getAllWmsInv(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
                : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return (skuInventoryRepository.findAll(paging));
    }

    // This method checks whether the sku is already present or not
    public SkuInventory validateSku(SKUInventoryKey id) throws SkuNotFound {
        Optional<SkuInventory> existingSku = skuInventoryRepository.findById(id);

        if (existingSku.isEmpty()) {
            throw new SkuNotFound(skuNotFound);
        }
        return existingSku.get();
    }

    // performs the action (add/subtract/lock/unlock)
    public SkuInventory perform(SKUInventoryKey skuInventoryKey, WmsInvRequest wmsInvRequest, WmsSku sku,
                                WmsResponseDetail wmsResponseDetail) throws Exception {
        log.info("Entered into perform : {} {} {} {} ", skuInventoryKey, wmsInvRequest, sku, wmsResponseDetail);
        Optional<SkuInventory> optionalSku = skuInventoryRepository.findById(skuInventoryKey);
        SkuInventory existingSku = null;
        String action = sku.getAction();
        SkuInventory savedSku = null;

        // if sku is present then assign it to existingSku
        if (!optionalSku.isEmpty()) {
            existingSku = optionalSku.get();
        } else if (!action.equalsIgnoreCase(wmsActions[0])) { // if sku is not present, check for action (should not be add)
            throw new SkuNotFound(skuNotFound);
        }

        if (action.equalsIgnoreCase(wmsActions[0])) {
            // it is a new sku
            if (existingSku == null) {
                SkuInventory newSku = new SkuInventory();
                newSku.setId(skuInventoryKey);
                savedSku = add(newSku, sku);
            } else {
                // it is an existing sku - qty needs to be added
                savedSku = add(existingSku, sku);
                // check and update protect qty if campaign is running for current sku
                updateProtectQtyInCampaign.updateProtectQtyInCampaign(skuInventoryKey);
            }
            wmsResponseDetail.getResponseDetail().setResponseCode(success);
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuAddSuccessful);
        } else if (action.equalsIgnoreCase(wmsActions[1])) {
            savedSku = subtract(existingSku, wmsInvRequest, sku);
            wmsResponseDetail.getResponseDetail().setResponseCode(success);
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuSubtractSuccessful);
        } else if (action.equalsIgnoreCase(wmsActions[2])) {
            savedSku = lock(existingSku, sku, wmsResponseDetail);
        } else if (action.equalsIgnoreCase(wmsActions[3])) {
            savedSku = unLock(existingSku, sku, wmsResponseDetail);
        } else if (action.equalsIgnoreCase(wmsActions[4])) {
            savedSku = consume(existingSku, wmsInvRequest, sku, wmsResponseDetail);
        }

        log.info("Exit from perform : {} ", savedSku);
        return savedSku;
    }

    // This method adds qty to an existing sku or adds a brand new sku to wms inventory
    private SkuInventory add(SkuInventory skuInventory, WmsSku sku) {
        log.info("Entered into add : {} {} ", skuInventory, sku);
        double qty = sku.getQty();
        double onHandQty = qty + skuInventory.getOnHandQuantity();
        double allocatedQty = skuInventory.getAllocatedQuantity();
        double protectedQty = skuInventory.getProtectedQuantity();
        double lockedQty = skuInventory.getLockedQuantity();
        String style = skuInventory.getId().getStyle();
        String styleSfx = skuInventory.getId().getStyleSfx();
        String color = skuInventory.getId().getColor();
        String colorSfx = skuInventory.getId().getColorSfx();
        String skuBarcode = skuInventory.getId().getSkuBarcode();
        List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);
        // calculate availableQty
        double availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        skuInventory.setOnHandQuantity(onHandQty);
        skuInventory.setAvailableQuantity(availableQty);
        skuInventory.setSerialNumber(sku.getSerialNumber());
        // save everything to the db in one go
        SkuInventory savedSku = skuInventoryRepository.save(skuInventory);

        // if available qty goes to zero then a alert mail will be sent to respective team.
        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
                    subjectAvailableZeroAlert, null);
        }
        log.info("Exit from add : {} ", savedSku);
        return savedSku;
    }

    // This method subtracts from an existing sku in wms inventory
    private SkuInventory subtract(SkuInventory skuInventory, WmsInvRequest wmsInvRequest, WmsSku sku) {
        log.info("Entered into subtract : {} {} {} ", skuInventory, wmsInvRequest, sku);
        long tobeShippedOutFromDC = sku.getQty();
        double onHandQty = skuInventory.getOnHandQuantity();
        double allocatedQty = skuInventory.getAllocatedQuantity();
        double protectedQty = skuInventory.getProtectedQuantity();
        double lockedQty = skuInventory.getLockedQuantity();

        // check if the allocated is less than the requested.
        if (allocatedQty < tobeShippedOutFromDC) {
            throw new AllocatedQtyLessThanRequestedQty(allocatedQtyLessThanRequestedQty);
        } else {
            // reduce the allocated qty
            allocatedQty -= tobeShippedOutFromDC;
            invTransactionService.deAllocateForChannel(skuInventory.getId(), (int) sku.getQty(), "", "",
                    wmsInvRequest.getUser(), wmsInvRequest.getDateTimeStamp(), sku.getAction(), 0);
            // save it
            skuInventory.setAllocatedQuantity(allocatedQty);
            // get the onHandQty & reduce it
            onHandQty -= tobeShippedOutFromDC;
            // save it
            skuInventory.setOnHandQuantity(onHandQty);
            // calculate the availableQty
            double availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
            skuInventory.setAvailableQuantity(availableQty);
            // save everything to the db in one go
            SkuInventory savedSku = skuInventoryRepository.save(skuInventory);

            SKUInventoryKey skuInventoryKey = skuInventory.getId();
            String style = skuInventoryKey.getStyle();
            String styleSfx = skuInventoryKey.getStyleSfx();
            String color = skuInventoryKey.getColor();
            String colorSfx = skuInventoryKey.getColorSfx();
            String skuBarcode = skuInventoryKey.getSkuBarcode();
            List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

            // if available qty goes to zero then a alert mail will be sent to respective team.
            if (availableQty == 0) {
                emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
                        subjectAvailableZeroAlert, null);
            }
            log.info("Exit from subtract : {} ", savedSku);
            return savedSku;
        }
    }

    // This method locks qty to an existing sku to wms inventory
    private SkuInventory lock(SkuInventory skuInventory, WmsSku sku, WmsResponseDetail wmsResponseDetail) {
        log.info("Entered into lock : {} {} {} ", skuInventory, sku, wmsResponseDetail);
        double qty = sku.getQty();
        double onHandQty = skuInventory.getOnHandQuantity();
        double allocatedQty = skuInventory.getAllocatedQuantity();
        double protectedQty = skuInventory.getProtectedQuantity();
        double lockedQty = skuInventory.getLockedQuantity();
        double availableQty = skuInventory.getAvailableQuantity();

        if (availableQty >= qty) { // sku quantity can be locked if available qty is greater than or equal to requested qty.
            lockedQty += qty;
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuLockSuccessful);
            wmsResponseDetail.getResponseDetail().setResponseCode(success);
        } else if (availableQty == 0) { // sku quantity cannot be locked if available qty is equal to zero.
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuLockFailure);
        } else { // if available quantity is less than requested qty then remaining skus in available will be locked.
            lockedQty += availableQty;
            wmsResponseDetail.setResponseQty(availableQty);
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuLockPartialSuccess);
            wmsResponseDetail.getResponseDetail().setResponseCode(partialSuccess);
        }
        // calculate availableQty
        availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        skuInventory.setAvailableQuantity(availableQty);
        skuInventory.setLockedQuantity(lockedQty);
        // save everything to the db in one go
        SkuInventory savedSku = skuInventoryRepository.save(skuInventory);

        SKUInventoryKey skuInventoryKey = skuInventory.getId();
        String style = skuInventoryKey.getStyle();
        String styleSfx = skuInventoryKey.getStyleSfx();
        String color = skuInventoryKey.getColor();
        String colorSfx = skuInventoryKey.getColorSfx();
        String skuBarcode = skuInventoryKey.getSkuBarcode();
        List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

        // if available qty goes to zero then a alert mail will be sent to respective team.
        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
                    subjectAvailableZeroAlert, null);
        }
        log.info("Exit from lock : {} ", savedSku);
        return savedSku;
    }

    // This method unlocks qty to an existing sku to wms inventory
    private SkuInventory unLock(SkuInventory skuInventory, WmsSku sku, WmsResponseDetail wmsResponseDetail) {
        log.info("Entered into unLock : {} {} {} ", skuInventory, sku, wmsResponseDetail);
        double qty = sku.getQty();
        double onHandQty = skuInventory.getOnHandQuantity();
        double allocatedQty = skuInventory.getAllocatedQuantity();
        double protectedQty = skuInventory.getProtectedQuantity();
        double lockedQty = skuInventory.getLockedQuantity();
        double availableQty;

        // if requested qty is less than or equal to already locked qty, then sku quantity can be unlocked.
        if (qty <= lockedQty) {
            lockedQty -= qty;
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuUnLockSuccessful);
            wmsResponseDetail.getResponseDetail().setResponseCode(success);
        } else if (lockedQty == 0) { // sku quantity cannot be unlocked if locked qty is equal to zero.
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuUnLockFailure);
        } else { // if locked qty is less than requested qty then remaining locked skus will be unlocked.
            wmsResponseDetail.setResponseQty(lockedQty);
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuUnlockPartialSuccess);
            wmsResponseDetail.getResponseDetail().setResponseCode(partialSuccess);
            lockedQty = 0;
        }
        // calculate availableQty
        availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        skuInventory.setAvailableQuantity(availableQty);
        skuInventory.setLockedQuantity(lockedQty);
        log.info("Exit from unLock : {} ", skuInventory);
        return skuInventoryRepository.save(skuInventory);
    }

    // This method consume qty to an existing sku to wms inventory
    private SkuInventory consume(SkuInventory skuInventory, WmsInvRequest wmsInvRequest, WmsSku sku, WmsResponseDetail wmsResponseDetail) {
        log.info("Entered into consume : {} {} {} ", skuInventory, sku, wmsResponseDetail);
        double qty = sku.getQty();
        double onHandQty = skuInventory.getOnHandQuantity();
        double availableQty = skuInventory.getAvailableQuantity();

        if ((onHandQty >= qty) && (availableQty >= qty)) {
            // reduce both onhand and available qty
            skuInventory.setOnHandQuantity(onHandQty - qty);
            skuInventory.setAvailableQuantity(availableQty - qty);
            wmsResponseDetail.setResponseQty(qty);
            wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuConsumeSuccessful);
            wmsResponseDetail.getResponseDetail().setResponseCode(success);

            invTransactionService.deAllocateForChannel(skuInventory.getId(), (int) qty, "", "",
                    wmsInvRequest.getUser(), wmsInvRequest.getDateTimeStamp(), "consume", 0);
        } else {
            if ((onHandQty < qty)) {
                if (availableQty == 0) {
                    // failure transaction
                    wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuConsumeFailure);
                } else {
                    skuInventory.setOnHandQuantity(onHandQty - availableQty);
                    skuInventory.setAvailableQuantity(0);
                    wmsResponseDetail.setResponseQty(availableQty);
                    wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuConsumePartialSuccess);
                    wmsResponseDetail.getResponseDetail().setResponseCode(partialSuccess);

                    invTransactionService.deAllocateForChannel(skuInventory.getId(), (int) availableQty, "", "",
                            wmsInvRequest.getUser(), wmsInvRequest.getDateTimeStamp(), consume, 0);
                }
            } else {
                if (availableQty == 0) {
                    // failure transaction
                    wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuConsumeFailure);
                } else {
                    skuInventory.setOnHandQuantity(onHandQty - availableQty);
                    skuInventory.setAvailableQuantity(0);
                    wmsResponseDetail.setResponseQty(availableQty);
                    wmsResponseDetail.getResponseDetail().setResponseId(wmsSkuConsumePartialSuccess);
                    wmsResponseDetail.getResponseDetail().setResponseCode(partialSuccess);

                    invTransactionService.deAllocateForChannel(skuInventory.getId(), (int) availableQty, "", "",
                            wmsInvRequest.getUser(), wmsInvRequest.getDateTimeStamp(), "consume", 0);
                }
            }
        }
        SkuInventory savedSku = skuInventoryRepository.save(skuInventory);

        SKUInventoryKey skuInventoryKey = skuInventory.getId();
        String style = skuInventoryKey.getStyle();
        String styleSfx = skuInventoryKey.getStyleSfx();
        String color = skuInventoryKey.getColor();
        String colorSfx = skuInventoryKey.getColorSfx();
        String skuBarcode = skuInventoryKey.getSkuBarcode();
        List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

        // if available qty goes to zero then a alert mail will be sent to respective team.
        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
                    subjectAvailableZeroAlert, null);
        }
        log.info("Exit from consume : {} ", savedSku);
        return savedSku;
    }

    public SkuInventory subtractReplenishFromAvailable(SkuInventory skuInventory, int replenishQty) {
        // update currentProtectQty
        skuInventory = updateProtectDuringReplenishment(skuInventory, replenishQty);
        // update availableQty
        return updateAvailable(skuInventory);
    }

    // This method subtracts protected qty from available qty
    public SkuInventory subtract(SkuInventory skuInventory, int protectiveQty) {
        double availableQty = skuInventory.getAvailableQuantity();

        if (availableQty >= protectiveQty) {
            skuInventory = updateProtectDuringReplenishment(skuInventory, protectiveQty);
        } else {
            skuInventory = updateProtectDuringReplenishment(skuInventory, (int) availableQty);
        }
        return updateAvailable(skuInventory);
    }

    // This method updates available skus with requested quantity to wms inventory
    public SkuInventory updateAllocateAndAvailable(SkuInventory skuInventory, int requestedQty) throws Exception {
        double availableQty = skuInventory.getAvailableQuantity();
        String style = skuInventory.getId().getStyle();
        String styleSfx = skuInventory.getId().getStyleSfx();
        String color = skuInventory.getId().getColor();
        String colorSfx = skuInventory.getId().getColorSfx();
        String skuBarcode = skuInventory.getId().getSkuBarcode();
        List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

        // if available qty goes to zero then a alert mail will be sent to respective team.
        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
                    subjectAvailableZeroAlert, null);
            throw new AvailableQtyIsZero(availableQtyIsZero);
        }

        if (availableQty < requestedQty) {
            skuInventory = updateAllocate(skuInventory, (int) availableQty);
        } else {
            skuInventory = updateAllocate(skuInventory, requestedQty);
        }
        return updateAvailable(skuInventory);
    }

    public SkuInventory updateAllocate(SkuInventory skuInventory, int allocateQty) {
        skuInventory.setAllocatedQuantity(skuInventory.getAllocatedQuantity() + allocateQty);
        return skuInventoryRepository.save(skuInventory);
    }

    public SkuInventory updateProtect(SkuInventory skuInventory, int requestedQty) {
        double protectedQuantity = skuInventory.getProtectedQuantity();
        skuInventory.setProtectedQuantity(protectedQuantity - requestedQty);
        return skuInventoryRepository.save(skuInventory);
    }

    public SkuInventory updateAvailable(SkuInventory skuInventory) {
        double availableQty = skuInventory.getOnHandQuantity() - skuInventory.getProtectedQuantity()
                - skuInventory.getAllocatedQuantity() - skuInventory.getLockedQuantity();
        skuInventory.setAvailableQuantity(availableQty);
        return skuInventoryRepository.save(skuInventory);
    }

    public SkuInventory updateAllocateForDeallocation(SkuInventory skuInventory, int requestedQty) {
        double dbAllocatedQty = skuInventory.getAllocatedQuantity();

        if (dbAllocatedQty < requestedQty) {
            skuInventory.setAllocatedQuantity(dbAllocatedQty - dbAllocatedQty);
        } else {
            skuInventory.setAllocatedQuantity(dbAllocatedQty - requestedQty);
        }
        return skuInventoryRepository.save(skuInventory);
    }

    public SkuInventory updateProtectDuringReplenishment(SkuInventory skuInventory, int replenishQty) {
        double protectedQuantity = skuInventory.getProtectedQuantity();
        skuInventory.setProtectedQuantity(protectedQuantity + replenishQty);
        return skuInventoryRepository.save(skuInventory);
    }

    public SkuInventory addProtectQtyToAvailableQty(SkuInventory skuInventory, int currentProtectQty) {
        // update currentProtectQty
        skuInventory = updateProtect(skuInventory, currentProtectQty);
        // update availableQty
        return updateAvailable(skuInventory);
    }
}