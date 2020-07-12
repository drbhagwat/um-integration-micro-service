package api.external.inventory.service;

import api.external.errors.SkuNotFound;
import api.external.inventory.entity.InvTransaction;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.repo.InvTransactionRepository;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Provides operations of Inv Transaction
 *
 * @author Anghulakshmi B
 * @version 1.0
 * @since 2019-10-23
 */

@Service
public class InvTransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvTransactionService.class);

	@Autowired
	private InvTransactionRepository invTransactionRepository;

	public InvTransaction updateProtectQtyForCampaign(SKUInventoryKey skuInventoryKey, String campaignCode,
			String channel, double protectQty, String reasonCode, String user, LocalDateTime dateTimeStamp) {

		LOGGER.info("Entered into InvTransaction for campaign call.");

		InvTransaction invTransaction = new InvTransaction();

		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);
		invTransaction.setProtectedQuantity(protectQty);
		invTransaction.setReasonCode(reasonCode);
		invTransaction.setUser(user);
		invTransaction.setDateTimeStamp(dateTimeStamp);

		LOGGER.info("Exit from InvTransaction for campaign call.");
		return invTransactionRepository.save(invTransaction);
	}

	Page<InvTransaction> getAllSkuInventory(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

		return (invTransactionRepository.findAll(paging));
	}

	public InvTransaction allocateForChannel(SKUInventoryKey skuInventoryKey, int requestedQty, String channel,
			String campaignCode, String user, LocalDateTime dateTimeStamp, String action, int protectedQty)
			throws SkuNotFound {

		LOGGER.info("Entered into InvTransaction for Allocation.");

		InvTransaction invTransaction = new InvTransaction();
		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setAllocatedQuantity(requestedQty);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);
		invTransaction.setUser(user);
		invTransaction.setReasonCode(action);
		invTransaction.setDateTimeStamp(dateTimeStamp);

		if (protectedQty == 0) {
			invTransaction.setProtectedQuantity(0);
		} else {

			if (requestedQty == 0) {
				invTransaction.setProtectedQuantity(-protectedQty);
				return invTransactionRepository.save(invTransaction);
			}

			if (requestedQty <= protectedQty) {
				invTransaction.setProtectedQuantity(-requestedQty);
			} else {
				invTransaction.setProtectedQuantity(-protectedQty);
			}
		}
		LOGGER.info("Exit from InvTransaction for Allocation");
		return invTransactionRepository.save(invTransaction);
	}

	public InvTransaction deAllocateForChannel(SKUInventoryKey skuInventoryKey, int requestedQty, String channel,
			String campaignCode, String user, LocalDateTime dateTimeStamp, String action, int protectedQty) {

		LOGGER.info("Entered into InvTransaction for De-allocation or subtraction.");

		InvTransaction invTransaction = new InvTransaction();
		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);
		invTransaction.setUser(user);
		invTransaction.setReasonCode(action);
		invTransaction.setDateTimeStamp(dateTimeStamp);
		invTransaction.setAllocatedQuantity(-requestedQty);

		if (protectedQty == 0) {
			invTransaction.setProtectedQuantity(0);
		} else {
			invTransaction.setProtectedQuantity(protectedQty);
		}
		LOGGER.info("Exit from InvTransaction for De-allocation or subtraction");
		return invTransactionRepository.save(invTransaction);
	}

	public InvTransaction updateDuringReplenishment(SKUInventoryKey skuInventoryKey, String campaignCode,
			String channel, String user, LocalDateTime dateTimeStamp, String action, double protectQty) {

		LOGGER.info("Entered into InvTransaction for Replenishment.");
		InvTransaction invTransaction = new InvTransaction();

		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);
		invTransaction.setUser(user);
		invTransaction.setDateTimeStamp(dateTimeStamp);
		invTransaction.setReasonCode(action);
		invTransaction.setProtectedQuantity(protectQty);
		LOGGER.info("Exit from InvTransaction for Replenishment");

		return invTransactionRepository.save(invTransaction);
	}

	public InvTransaction updateProtectQtyForEOD(SKUInventoryKey skuInventoryKey, String campaignCode, String channel,
			double protectQty, String reasonCode, String user, LocalDateTime dateTimeStamp) {

		LOGGER.info("Entered into InvTransaction for EOD process.");
		InvTransaction invTransaction = new InvTransaction();

		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);

		if (protectQty == 0) {
			invTransaction.setProtectedQuantity(0);
		} else {
			invTransaction.setProtectedQuantity(-protectQty);
		}
		invTransaction.setReasonCode(reasonCode);
		invTransaction.setUser(user);
		invTransaction.setDateTimeStamp(dateTimeStamp);

		LOGGER.info("Exit from InvTransaction for EOD process.");
		return invTransactionRepository.save(invTransaction);
	}

	public InvTransaction updateAvailFromProtect(SKUInventoryKey skuInventoryKey, String campaignCode, String channel,
			String user, LocalDateTime dateTimeStamp, String action, double protectQty) {

		LOGGER.info("Entered into InvTransaction for Replenishment.");
		InvTransaction invTransaction = new InvTransaction();

		invTransaction.setSkuInventoryKey(skuInventoryKey);
		invTransaction.setCampaignCode(campaignCode);
		invTransaction.setChannel(channel);
		invTransaction.setUser(user);
		invTransaction.setDateTimeStamp(dateTimeStamp);
		invTransaction.setReasonCode(action);
		
		if (protectQty == 0) {
			invTransaction.setProtectedQuantity(0);
		} else {
			invTransaction.setProtectedQuantity(-protectQty);
		}
		LOGGER.info("Exit from InvTransaction for Replenishment");

		return invTransactionRepository.save(invTransaction);
	}

}