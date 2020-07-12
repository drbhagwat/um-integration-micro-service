
package api.external.inventory.service;

import api.email.EmailService;
import api.external.channel.errors.AllocatedQtyIsLessThanRequestedQty;
import api.external.channel.errors.AllocatedQtyZero;
import api.external.channel.errors.AvailableQtyZero;
import api.external.errors.AvailableQtyLessThanRequestedQty;
import api.external.errors.SkuNotFound;
import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import api.external.inventory.error.AllocatedQtyLessThanRequestedQty;
import api.external.inventory.repo.ManufacturingInventoryRepository;
import api.external.mfg.entity.MfgInvRequest;
import api.external.mfg.entity.MfgInvSku;
import api.external.mfg.entity.MfgResponseDetail;
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
 * Provides Create, Read and Update operations of the manufacturing inventory
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-05
 */

@Service
@Slf4j
public class ManufacturingInventoryService {

    @Autowired
    private ManufacturingInventoryRepository manufacturingInventoryRepository;

    @Value("${AVAILABLE_QTY_LESS_THAN_REQUESTED}")
    private String availableQtyLessThanRequested;

    @Value("${MFG_SKU_NOT_FOUND}")
    private String mfgSkuNotFound;

    @Value("${AVAILABLE_QTY_IS_ZERO}")
    private String availableQtyIsZero;

    @Value("${ALLOCATED_QTY_LESS_THAN_REQUESTED_QTY}")
    private String allocatedQtyLessThanRequestedQty;

    @Autowired
    private EmailService emailService;

    @Value("${SUBJECT_AVAILABLE_ZERO_ALERT}")
    private String subjectAvailableZeroAlert;

    @Value("${INVALID_SALES_ORDER_NUMBER}")
    private String invalidSalesOrderNumber;

    @Value("${MFG_SKU_ADD_SUCCESSFUL}")
    private String mfgSkuAddSuccessful;

    @Value("${MFG_SKU_LOCK_SUCCESSFUL}")
    private String mfgSkuLockSuccessful;

    @Value("${MFG_SKU_UNLOCK_SUCCESSFUL}")
    private String mfgSkuUnLockSuccessful;

    @Value("${MFG_SKU_SUBTRACT_SUCCESSFUL}")
    private String mfgSkuSubtractSuccessful;

    @Value("${MFG_SKU_LOCK_PARTIAL_SUCCESS}")
    private String mfgSkuLockPartialSuccess;

    @Value("${MFG_SKU_UNLOCK_PARTIAL_SUCCESS}")
    private String mfgSkuUnlockPartialSuccess;

    @Value("${MFG_SKU_LOCK_FAILURE}")
    private String mfgSkuLockFailure;

    @Value("${MFG_SKU_UNLOCK_FAILURE}")
    private String mfgSkuUnLockFailure;

    @Value("${ALLOCATED_QTY_ZERO}")
    private String allocatedQtyZero;

    @Value("${SUCCESS_RESPONSE}")
    private char success;

    @Value("${PARTIAL_RESPONSE}")
    private char partialSuccess;

    @Value("${MFG_ACTIONS}")
    private String[] mfgActions;

    @Value("${MFG_ACTIONS}")
    public void setActions(String[] mfgActions) {
        this.mfgActions = mfgActions;
    }

    @Autowired
    private Environment env;

    /**
     * @param pageNo   - default is 0, can be overridden by caller.
     * @param pageSize - default is 10, can be overridden by caller.
     * @param sortBy   - default is lastUpdatedDateTime, can be overridden by
     *                 caller.
     * @param orderBy  - default is descending, can be overridden by caller.
     * @return - on success, returns a page of all manufacturingPlants.
     */
    public Page<ManufacturingInventory> getAllManufacturingInventory(Integer pageNo, Integer pageSize, String sortBy,
                                                                     String orderBy) {
        Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
                : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return (manufacturingInventoryRepository.findAll(paging));
    }

    /**
     * @param manufacturingInventoryKey - primary key by which you want to find the
     *                                  inventory in the db.
     * @return on success, it returns the manufacturingPlants found.
     * @throws Exception - on failure, an exception is thrown and a meaningful error
     *                   message is displayed.
     */
    public ManufacturingInventory getManufacturingInventoryById(ManufacturingInventoryKey manufacturingInventoryKey)
            throws Exception {
        return validate(manufacturingInventoryKey);
    }

    /**
     * @param id - primary key by which you want to find the
     *           inventory in the db.
     * @return on success, it returns the manufacturingPlants found.
     * @throws Exception - on failure, an exception is thrown and a meaningful error
     *                   message is displayed.
     *                   This method checks whether the sku is already present or not
     */
    public ManufacturingInventory validate(ManufacturingInventoryKey id) throws SkuNotFound {
        // finding the sku in the manufacturing inventory
        Optional<ManufacturingInventory> existingSku = manufacturingInventoryRepository.findById(id);

        if (existingSku.isEmpty()) {
            throw new SkuNotFound(mfgSkuNotFound);
        }
        return existingSku.get();
    }

    // This method is used to process which action can be done at the Mfg inventory API call
    public ManufacturingInventory perform(ManufacturingInventoryKey manufacturingInventoryKey, MfgInvRequest mfgInvRequest,
                                                       MfgInvSku sku, MfgResponseDetail mfgResponseDetail) throws Exception {
        log.info("Entered into perform : {} {} {} {} ", manufacturingInventoryKey, mfgInvRequest, sku, mfgResponseDetail);
        ManufacturingInventory savedSku = null;
        ManufacturingInventory existingSku = null;
        String action = sku.getAction();
        // check if it is an entry in the db already
        Optional<ManufacturingInventory> optionalSku = manufacturingInventoryRepository.findById(manufacturingInventoryKey);

        // if sku is present then assign it to existingSku
        if (!optionalSku.isEmpty()) {
            existingSku = optionalSku.get();
        } else if (!action.equalsIgnoreCase(mfgActions[0])) { // if sku is not present, check for action (should not be add)
            throw new SkuNotFound(mfgSkuNotFound);
        }

        if (action.equalsIgnoreCase(mfgActions[0])) {
            // brand new sku getting added for the first time.
            if (optionalSku.isEmpty()) {
                ManufacturingInventory newSku = new ManufacturingInventory();
                newSku.setId(manufacturingInventoryKey);
                savedSku = add(newSku, mfgInvRequest, sku);
            } else { // it is an existing sku - qty needs to be added
                savedSku = add(existingSku, mfgInvRequest, sku);
            }
            mfgResponseDetail.setResponseId(mfgSkuAddSuccessful);
            mfgResponseDetail.setResponseCode(success);
        } else if (action.equalsIgnoreCase(mfgActions[1])) {
            savedSku = subtract(existingSku, mfgInvRequest, sku);
            mfgResponseDetail.setResponseId(mfgSkuSubtractSuccessful);
            mfgResponseDetail.setResponseCode(success);
        } else if (action.equalsIgnoreCase(mfgActions[2])) {
            savedSku = lock(existingSku, mfgInvRequest, sku, mfgResponseDetail);
        } else if (action.equalsIgnoreCase(mfgActions[3])) {
            savedSku = unLock(existingSku, mfgInvRequest, sku, mfgResponseDetail);
        }
        return savedSku;
    }

    // This method adds qty to an existing sku or adds a brand new sku to mfg inventory
    private ManufacturingInventory add(ManufacturingInventory manufacturingInventory, MfgInvRequest mfgInvRequest,
                                                    MfgInvSku sku) {
        double qty = sku.getQty();
        double onHandQty = qty + manufacturingInventory.getOnHandQuantity();
        double allocatedQty = manufacturingInventory.getAllocatedQuantity();
        double protectedQty = manufacturingInventory.getProtectedQuantity();
        double lockedQty = manufacturingInventory.getLockedQuantity();
        // calculate availableQty
        double availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        manufacturingInventory.setOnHandQuantity(onHandQty);
        manufacturingInventory.setAvailableQuantity(availableQty);
        manufacturingInventory.setAllocatedQuantity(allocatedQty);
        manufacturingInventory.setProtectedQuantity(protectedQty);
        manufacturingInventory.setLockedQuantity(lockedQty);
        manufacturingInventory.setSerialNumber(sku.getSerialNumber());
        manufacturingInventory.setProductionOrderNumber(mfgInvRequest.getProductionOrderNumber());
        manufacturingInventory.setCustomerOrderNumber(mfgInvRequest.getCustomerOrderNumber());
        manufacturingInventory.setPurchaseOrderNumber(mfgInvRequest.getPurchaseOrderNumber());
        // save everything to the db in one go
        ManufacturingInventory savedSku = manufacturingInventoryRepository.save(manufacturingInventory);

        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null,
                    subjectAvailableZeroAlert, null);
        }
        return savedSku;
    }

    // This method subtracts qty from an existing sku of mfg inventory
    private ManufacturingInventory subtract(ManufacturingInventory manufacturingInventory,
                                                         MfgInvRequest mfgInvRequest, MfgInvSku sku) {
        double tobeShippedOutFromDC = sku.getQty();
        double onHandQty = manufacturingInventory.getOnHandQuantity();
        double allocatedQty = manufacturingInventory.getAllocatedQuantity();
        double protectedQty = manufacturingInventory.getProtectedQuantity();
        double lockedQty = manufacturingInventory.getLockedQuantity();

        // check if the allocated is less than the requested.
        if (allocatedQty < tobeShippedOutFromDC) {
            throw new AllocatedQtyLessThanRequestedQty(allocatedQtyLessThanRequestedQty);
        } else {
            allocatedQty -= tobeShippedOutFromDC;
            manufacturingInventory.setAllocatedQuantity(allocatedQty);
            onHandQty -= tobeShippedOutFromDC;
            manufacturingInventory.setOnHandQuantity(onHandQty);
            // calculate availableQty
            double availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
            manufacturingInventory.setAvailableQuantity(availableQty);
            manufacturingInventory.setProtectedQuantity(protectedQty);
            manufacturingInventory.setAllocatedQuantity(allocatedQty);
            manufacturingInventory.setLockedQuantity(lockedQty);
            manufacturingInventory.setProductionOrderNumber(mfgInvRequest.getProductionOrderNumber());
            manufacturingInventory.setCustomerOrderNumber(mfgInvRequest.getCustomerOrderNumber());
            manufacturingInventory.setPurchaseOrderNumber(mfgInvRequest.getPurchaseOrderNumber());
            // save everything to the db in one go
            ManufacturingInventory savedManufacturingInventory = manufacturingInventoryRepository.save(manufacturingInventory);

            if (availableQty == 0) {
                emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null,
                        subjectAvailableZeroAlert, null);
            }
            return savedManufacturingInventory;
        }
    }

    // This method locks qty to an existing sku to mfg inventory
    private ManufacturingInventory lock(ManufacturingInventory manufacturingInventory, MfgInvRequest mfgInvRequest,
                                                     MfgInvSku sku, MfgResponseDetail mfgResponseDetail) {
        double qty = sku.getQty();
        double onHandQty = manufacturingInventory.getOnHandQuantity();
        double allocatedQty = manufacturingInventory.getAllocatedQuantity();
        double protectedQty = manufacturingInventory.getProtectedQuantity();
        double lockedQty = manufacturingInventory.getLockedQuantity();
        double availableQty = manufacturingInventory.getAvailableQuantity();

        if (availableQty == 0) {
            mfgResponseDetail.setResponseId(mfgSkuLockFailure);
        } else {
            if (availableQty >= qty) {
                lockedQty += qty;
                mfgResponseDetail.setResponseId(mfgSkuLockSuccessful);
                mfgResponseDetail.setResponseCode(success);
            } else {
                lockedQty = availableQty;
                mfgResponseDetail.setResponseCode(partialSuccess);
                mfgResponseDetail.setResponseId(mfgSkuLockPartialSuccess);
            }
        }
        // calculate availableQty
        availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        manufacturingInventory.setAvailableQuantity(availableQty);
        manufacturingInventory.setLockedQuantity(lockedQty);
        manufacturingInventory.setProductionOrderNumber(mfgInvRequest.getProductionOrderNumber());
        manufacturingInventory.setCustomerOrderNumber(mfgInvRequest.getCustomerOrderNumber());
        manufacturingInventory.setPurchaseOrderNumber(mfgInvRequest.getPurchaseOrderNumber());
        // save everything to the db in one go
        ManufacturingInventory savedSku = manufacturingInventoryRepository.save(manufacturingInventory);

        if (availableQty == 0) {
            emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null,
                    subjectAvailableZeroAlert, null);
        }
        return savedSku;
    }

    // This method unlocks qty to an existing sku to mfg inventory
    private ManufacturingInventory unLock(ManufacturingInventory manufacturingInventory, MfgInvRequest mfgInvRequest,
                                                       MfgInvSku sku, MfgResponseDetail mfgResponseDetail) {
        double qty = sku.getQty();
        double onHandQty = manufacturingInventory.getOnHandQuantity();
        double allocatedQty = manufacturingInventory.getAllocatedQuantity();
        double protectedQty = manufacturingInventory.getProtectedQuantity();
        double lockedQty = manufacturingInventory.getLockedQuantity();

        if (lockedQty == 0) {
            mfgResponseDetail.setResponseId(mfgSkuUnLockFailure);
        } else {
            if (qty <= lockedQty) {
                lockedQty -= qty;
                mfgResponseDetail.setResponseCode(success);
                mfgResponseDetail.setResponseId(mfgSkuUnLockSuccessful);
            } else {
                lockedQty = 0;
                mfgResponseDetail.setResponseCode(partialSuccess);
                mfgResponseDetail.setResponseId(mfgSkuUnlockPartialSuccess);
            }
        }
        // calculate availableQty
        double availableQty = onHandQty - protectedQty - allocatedQty - lockedQty;
        manufacturingInventory.setAvailableQuantity(availableQty);
        manufacturingInventory.setLockedQuantity(lockedQty);
        manufacturingInventory.setProductionOrderNumber(mfgInvRequest.getProductionOrderNumber());
        manufacturingInventory.setCustomerOrderNumber(mfgInvRequest.getCustomerOrderNumber());
        manufacturingInventory.setPurchaseOrderNumber(mfgInvRequest.getPurchaseOrderNumber());
        // save everything to the db in one go
        return manufacturingInventoryRepository.save(manufacturingInventory);
    }

    /**
     * @param salesOrderNumber - order number, it comes from channelAPI
     *                         sku.
     * @param mode             - In which mode the customer wants to
     *                         process the request
     * @param requestedQty     - quantity, in which the customer would
     *                         request for quantity to be deducted
     * @return - on success it allocates or deallocates quantity of Manufacturing
     * inventory in the db.
     * @throws Exception - on failure, an exception is thrown and a meaningful error
     *                   message is displayed.
     *                   <p>
     *                   This method can be called at time of channel API request.
     */
    public ManufacturingInventory updateMfg(ManufacturingInventory manufacturingInventory,
                                                         String salesOrderNumber,
                                                         String mode, int requestedQty) throws Exception {
        // get the customer order number
        String customerOrderNumber = manufacturingInventory.getCustomerOrderNumber();

        // compare customerOrderNumber in the manufacturing inventory with the
        // salesOrderNumber in the channel API
        if (!customerOrderNumber.equals(salesOrderNumber)) {
            throw new Exception(invalidSalesOrderNumber);
        }
        // update manufacturing inventory
        return (update(manufacturingInventory, mode, requestedQty));
    }

    private ManufacturingInventory updateAllocate(ManufacturingInventory manufacturingInventory,
                                                               double allocateQty) {
        manufacturingInventory.setAllocatedQuantity(manufacturingInventory.getAllocatedQuantity() + allocateQty);
        return manufacturingInventoryRepository.save(manufacturingInventory);
    }

    private ManufacturingInventory updateAvailable(ManufacturingInventory manufacturingInventory) {
        double availableQty = manufacturingInventory.getOnHandQuantity() - manufacturingInventory.getProtectedQuantity()
                - manufacturingInventory.getAllocatedQuantity() - manufacturingInventory.getLockedQuantity();
        manufacturingInventory.setAvailableQuantity(availableQty);
        return manufacturingInventoryRepository.save(manufacturingInventory);
    }

    private ManufacturingInventory updateAllocateForDeallocation(ManufacturingInventory manufacturingInventory, int requestedQty) {

        double allocatedQty = manufacturingInventory.getAllocatedQuantity();

        if (allocatedQty < requestedQty) {
            manufacturingInventory.setAllocatedQuantity(0.0);
        } else {
            manufacturingInventory.setAllocatedQuantity(manufacturingInventory.getAllocatedQuantity() - requestedQty);
        }

        return manufacturingInventoryRepository.save(manufacturingInventory);
    }

    /**
     * @param manufacturingInventory
     * @param mode                   - In which mode the customer wants to process
     *                               the API request
     * @param requestedQty           - quantity, in which the customer would request
     *                               for quantity to be deducted
     * @return - on success it allocates or deallocates quantity of Manufacturing
     * inventory in the db.
     * @throws Exception - on failure, an exception is thrown and a meaningful error
     *                   message is displayed.
     *                   <p>
     *                   This method is used to update (allocate or deallocate)
     *                   quantity of each SKU in the manufacturing inventory based
     *                   on the mode from the channel API.
     */

    public ManufacturingInventory update(ManufacturingInventory manufacturingInventory, String mode,
                                                      int requestedQty) throws Exception {
        double dbOnhandQty = manufacturingInventory.getOnHandQuantity();
        double dbAllocatedQty = manufacturingInventory.getAllocatedQuantity();
        double dbProtectedQty = manufacturingInventory.getProtectedQuantity();
        double dbLockedQty = manufacturingInventory.getLockedQuantity();
        double dbAvailableQty = manufacturingInventory.getAvailableQuantity();

        String strDbOnhandQty = Double.toString(dbOnhandQty);
        String strDbAllocatedQty = Double.toString(dbAllocatedQty);
        String strDbProtectedQty = Double.toString(dbProtectedQty);
        String strDbLockedQty = Double.toString(dbLockedQty);
        String strDbAvailableQty = Double.toString(dbAvailableQty);
        String responseQty = null;

        switch (mode) {
            case "A": // allocate

                // nothing to allocate
                if (dbAvailableQty == 0) {
                    responseQty = Integer.toString(0);
                    emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null, subjectAvailableZeroAlert, null);
                    throw new AvailableQtyZero(availableQtyIsZero, responseQty, strDbOnhandQty, strDbAllocatedQty,
                            strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
                }

                if (dbAvailableQty < requestedQty) {
                    responseQty = Double.toString(dbAvailableQty);
                    manufacturingInventory = updateAllocate(manufacturingInventory, dbAvailableQty);
                    manufacturingInventory = updateAvailable(manufacturingInventory);

                    if (manufacturingInventory.getAvailableQuantity() == 0) {
                        emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null,
                                subjectAvailableZeroAlert, null);
                    }
                    throw new AvailableQtyLessThanRequestedQty(availableQtyLessThanRequested, responseQty, strDbOnhandQty,
                            strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
                } else {
                    manufacturingInventory = updateAllocate(manufacturingInventory, requestedQty);
                    manufacturingInventory = updateAvailable(manufacturingInventory);

                    if (manufacturingInventory.getAvailableQuantity() == 0) {
                        emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", null,
                                subjectAvailableZeroAlert, null);
                    }
                }
                break;

            case "D":// de-allocate

                if (dbAllocatedQty == 0) {
                    responseQty = Integer.toString(0);
                    throw new AllocatedQtyZero(allocatedQtyZero, responseQty, strDbOnhandQty, strDbAllocatedQty,
                            strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
                }

                manufacturingInventory = updateAllocateForDeallocation(manufacturingInventory, requestedQty);
                manufacturingInventory = updateAvailable(manufacturingInventory);

                if (dbAllocatedQty < requestedQty) {
                    responseQty = Double.toString(dbAllocatedQty);

                    throw new AllocatedQtyIsLessThanRequestedQty(allocatedQtyLessThanRequestedQty, responseQty, strDbOnhandQty,
                            strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
                }

                break;

            default:
                throw new Exception("Invalid Action");
        }
        // save to db
        return (manufacturingInventoryRepository.save(manufacturingInventory));
    }
}
