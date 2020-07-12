package api.external.channel.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import api.core.validation.CodeValidationService;
import api.email.EmailService;
import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;
import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.entity.CampaignHeaderKey;
import api.external.campaign.errors.CampaignIsNotAvailable;
import api.external.campaign.repo.CampaignDetailDbRepository;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.campaign.validation.CampaignValidationService;
import api.external.channel.errors.AllocatedQtyIsLessThanRequestedQty;
import api.external.channel.errors.AllocatedQtyZero;
import api.external.channel.errors.AvailableQtyAndProtectQtyZero;
import api.external.errors.AvailableQtyLessThanRequestedQty;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.InvTransactionService;
import api.external.inventory.service.SkuInventoryService;
import api.external.util.DateParser;

/**
 * This class represents to process allocation and deallocation requests of the
 * channelAPI
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-05-21
 */

@Service
public class ChannelAPIValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelAPIValidationService.class);

	@Autowired
	CodeValidationService codeValidationService;

	@Autowired
	private CampaignValidationService campaignValidationService;

	@Autowired
	private SkuInventoryService skuInventoryService;

	@Autowired
	private CampaignDetailDbRepository campaignDetailDbRepository;

	@Autowired
	private InvTransactionService invTransactionService;

	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private Environment env;

	@Value("${CHANNEL_MANDATORY}")
	private String channelMandatory;

	@Value("${CHANNEL_CANNOT_BE_BLANK}")
	private String channelCannotBeBlank;

	@Value("${CHANNEL_MAX_LENGTH}")
	private int channelMaxLength;

	@Value("${CHANNEL_INVALID_LENGTH}")
	private String channelInvalidLength;

	@Value("${CHANNEL_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String channelCannotContainSpecialCharacters;

	@Value("${DATETIME_IS_NOT_SPECIFIED_OR_EMPTY}")
	private String dateTimeIsNotSpecifiedOrEmpty;

	@Value("${CAMPAIGN_IS_NOT_AVAILABLE}")
	private String campaignIsNotAvailable;

	@Value("${AUTO_REPLENISH_NO}")
	private String autoReplenishNo;

	@Value("${CAMPAIGN_ACTIVE_YES}")
	private String campaignActiveYes;

	@Value("${AVAILABLE_QTY_LESS_THAN_REQUESTED}")
	private String availableQtyLessThanRequested;

	@Value("${INVALID_SHIP_DATE}")
	private String invalidShipDate;

	@Value("${AVAILABLE_QTY_IS_ZERO}")
	private String availableQtyIsZero;

	@Value("${AVAILABLE_QTY_AND_PROTECT_QTY_ARE_ZERO}")
	private String availableQtyAndProtectQtyAreZero;

	@Value("${AVAILABLE_QTY_AND_PROTECT_QTY_LESS_THAN_REQUESTED}")
	private String availableQtyAndProtectQtyLessThanRequested;

	@Value("${ALLOCATED_QTY_LESS_THAN_REQUESTED_QTY}")
	private String allocatedQtyLessThanRequestedQty;

	@Value("${ALLOCATED_QTY_ZERO}")
	private String allocatedQtyZero;

	@Value("${SUBJECT_AVAILABLE_ZERO_ALERT}")
	private String subjectAvailableZeroAlert;

	/*
	 * This method is used to process deallocation request and updates sku_inventory
	 * table and campaign_detail_db, and performs replenishment logic
	 */
	public SkuInventory deallocateAndUpdateForChannel(String channel, String campaignCode, LocalDateTime dateTimeStamp,
			String user, int requestedQty, LocalDate shipDate, String action, String company, String division,
			String warehouse, String skuBarcode, String season, String seasonYear, String style, String styleSfx,
			String color, String colorSfx, String secDimension, String quality, String sizeRngeCode,
			String sizeRelPosnIn, String inventoryType, String lotNumber, String countryOfOrigin, String productStatus,
			String skuAttribute1, String skuAttribute2, String skuAttribute3, String skuAttribute4,
			String skuAttribute5) throws Exception {

		SKUInventoryKey skuInventoryKey = new SKUInventoryKey(company, division, warehouse, skuBarcode, season,
				seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn,
				inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3,
				skuAttribute4, skuAttribute5);

		SkuInventory skuInventory = skuInventoryService.validateSku(skuInventoryKey);
		double dbAllocatedQty = skuInventory.getAllocatedQuantity();
		double dbOnhandQty = skuInventory.getOnHandQuantity();
		double dbProtectedQty = skuInventory.getProtectedQuantity();
		double dbLockedQty = skuInventory.getLockedQuantity();
		double dbAvailableQty = skuInventory.getAvailableQuantity();

		if (dbAllocatedQty == 0) {
			String responseQty = Integer.toString((int) dbAllocatedQty);
			dbOnhandQty = skuInventory.getOnHandQuantity();
			dbAllocatedQty = skuInventory.getAllocatedQuantity();
			dbProtectedQty = skuInventory.getProtectedQuantity();
			dbLockedQty = skuInventory.getLockedQuantity();
			dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);

			throw new AllocatedQtyZero(allocatedQtyZero, responseQty, strDbOnhandQty, strDbAllocatedQty,
					strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		}

		if (campaignCode.isBlank()) {
			return skuInventory = deallocateAndUpdateChannelWithCampaign(channel, campaignCode, dateTimeStamp, user,
					requestedQty, action, skuInventory, skuInventoryKey);
		}

// validate campaign header
		CampaignHeaderDb tempCampaignHeaderDb = campaignValidationService
				.validateHeaderId(new CampaignHeaderKey(channel, campaignCode));

		CampaignDetailKey campaignDetailKey = new CampaignDetailKey(new SKUInventoryKey(company, division, warehouse,
				skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode,
				sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2,
				skuAttribute3, skuAttribute4, skuAttribute5), channel, campaignCode);

// validate campaign detail
		CampaignDetailDb campaignDetailDb = campaignValidationService.validateDetailId(campaignDetailKey);

		String campaignActive = tempCampaignHeaderDb.getActive();
		LocalDate campaignEndDate = tempCampaignHeaderDb.getEndDate();

// get original protect quantity
		int originalProtectQty = campaignDetailDb.getOriginalProtectQuantity();
		int minQty = campaignDetailDb.getMinimumQuantity();

// get current protect quantity
		int currentProtectQty = campaignDetailDb.getCurrentProtectQty();

// validate whether the campaign status is active and dateTimeStamp of
// channelAPI is within the campaignStartDate and campaignEndDate
// if both cases are valid, deallocation process has to be done
		if (campaignActive.equals(campaignActiveYes)) {

			LOGGER.info("Entered into Deallocation function with running campaign. OnhandQty:" + dbOnhandQty
					+ " AllocatedQty:" + dbAllocatedQty + " ProtectQty:" + dbProtectedQty + " LockedQty:" + dbLockedQty
					+ " AvailableQty:" + dbAvailableQty);

			if (requestedQty <= dbAllocatedQty) {
				skuInventory = skuInventoryService.updateAllocateForDeallocation(skuInventory, requestedQty);
				skuInventory = skuInventoryService.updateAvailable(skuInventory);
				invTransactionService.deAllocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
						dateTimeStamp, action, 0);

				if (!campaignDetailDb.getAutoReplenish().equals(autoReplenishNo)) {
					if ((currentProtectQty < minQty) && (currentProtectQty < originalProtectQty)) { // going below

						double availableQty = skuInventory.getAvailableQuantity();
						int intAvailableQty = (int) availableQty;
						int replenishmentQty = originalProtectQty - currentProtectQty;

						String replenishmnetAction = "R";

						if (availableQty > 0) {

							LOGGER.info(
									"Entered into Replenishment Logic. OnhandQty:" + skuInventory.getOnHandQuantity()
											+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
											+ skuInventory.getProtectedQuantity() + " LockedQty:"
											+ skuInventory.getLockedQuantity() + " AvailableQty:"
											+ skuInventory.getAvailableQuantity());

// current protect qty always goes below minimum qty in this case
							if (availableQty >= replenishmentQty) {
// store new value
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
// save to db
								campaignDetailDbRepository.save(campaignDetailDb);

								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);

							} else {
								replenishmentQty = intAvailableQty;
// store new value, increase current protect qty with whatever in the
// availableQty
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
								campaignDetailDbRepository.save(campaignDetailDb);

// update available qty with replenishment qty
								skuInventoryService.updateProtectDuringReplenishment(skuInventory, replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);
							}

							LOGGER.info("Exit from Replenishment Logic. OnhandQty:" + skuInventory.getOnHandQuantity()
									+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
									+ skuInventory.getProtectedQuantity() + " LockedQty:"
									+ skuInventory.getLockedQuantity() + " AvailableQty:"
									+ skuInventory.getAvailableQuantity());
						}
					}
				}

			} else { // everything can't be deallocated from allocated

				skuInventory = skuInventoryService.updateAllocateForDeallocation(skuInventory, requestedQty);
				skuInventory = skuInventoryService.updateAvailable(skuInventory);
				invTransactionService.deAllocateForChannel(skuInventoryKey, (int) dbAllocatedQty, channel, campaignCode,
						user, dateTimeStamp, action, 0);

				if (!campaignDetailDb.getAutoReplenish().equals(autoReplenishNo)) {
					if ((currentProtectQty < minQty) && (currentProtectQty < originalProtectQty)) { // going below

						double availableQty = skuInventory.getAvailableQuantity();
						int intAvailableQty = (int) availableQty;
						int replenishmentQty = originalProtectQty - currentProtectQty;

						String replenishmnetAction = "R";

						if (availableQty > 0) {
							LOGGER.info(
									"Entered into Replenishment Logic. OnhandQty:" + skuInventory.getOnHandQuantity()
											+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
											+ skuInventory.getProtectedQuantity() + " LockedQty:"
											+ skuInventory.getLockedQuantity() + " AvailableQty:"
											+ skuInventory.getAvailableQuantity());
// current protect qty always goes below minimum qty in this case
							if (availableQty >= replenishmentQty) {
// store new value
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
// save to db
								campaignDetailDbRepository.save(campaignDetailDb);

								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);

							} else {
								replenishmentQty = intAvailableQty;
// store new value, increase current protect qty with whatever in the
// availableQty
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
								campaignDetailDbRepository.save(campaignDetailDb);

// update available qty with replenishment qty
								skuInventoryService.updateProtectDuringReplenishment(skuInventory, replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);
							}

							LOGGER.info("Exit from Replenishment Logic. OnhandQty:" + skuInventory.getOnHandQuantity()
									+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
									+ skuInventory.getProtectedQuantity() + " LockedQty:"
									+ skuInventory.getLockedQuantity() + " AvailableQty:"
									+ skuInventory.getAvailableQuantity());
						}
					}
				}

				String responseQty = Integer.toString((int) dbAllocatedQty);
				dbOnhandQty = skuInventory.getOnHandQuantity();
				dbAllocatedQty = skuInventory.getAllocatedQuantity();
				dbProtectedQty = skuInventory.getProtectedQuantity();
				dbLockedQty = skuInventory.getLockedQuantity();
				dbAvailableQty = skuInventory.getAvailableQuantity();

				String strDbOnhandQty = Double.toString(dbOnhandQty);
				String strDbAllocatedQty = Double.toString(dbAllocatedQty);
				String strDbProtectedQty = Double.toString(dbProtectedQty);
				String strDbLockedQty = Double.toString(dbLockedQty);
				String strDbAvailableQty = Double.toString(dbAvailableQty);

				LOGGER.info("Exit from Deallocation function with running campaign and partial success. OnhandQty:"
						+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
						+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
						+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

				throw new AllocatedQtyIsLessThanRequestedQty(allocatedQtyLessThanRequestedQty, responseQty,
						strDbOnhandQty, strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);

			}

			LOGGER.info("Exit from Deallocation function with running campaign. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

		} else {// campaign gets ended, deallocation process to be done without campaign

			if (campaignActive.equals("N") && DateParser.isBefore(campaignEndDate, LocalDate.now())) {
				skuInventory = deallocateAndUpdateChannelWithEndedCampaign(channel, campaignCode, dateTimeStamp, user,
						requestedQty, action, skuInventory, skuInventoryKey);
			} else {
// if the campaign is not yet started, there is no deallocation process and it
// throws an exception
				throw new CampaignIsNotAvailable(campaignIsNotAvailable);
			}
		}
		return skuInventory;
	}

	/*
	 * This method is used to process allocate request and updates the sku_inventory
	 * table and the campaign_detail_db table, and performs replenishment logic
	 */
	public SkuInventory allocateForChannel(String channel, String campaignCode, LocalDateTime dateTimeStamp,
			String user, int requestedQty, LocalDate shipDate, String action, String company, String division,
			String warehouse, String skuBarcode, String season, String seasonYear, String style, String styleSfx,
			String color, String colorSfx, String secDimension, String quality, String sizeRngeCode,
			String sizeRelPosnIn, String inventoryType, String lotNumber, String countryOfOrigin, String productStatus,
			String skuAttribute1, String skuAttribute2, String skuAttribute3, String skuAttribute4,
			String skuAttribute5) throws Exception {

		SKUInventoryKey skuInventoryKey = new SKUInventoryKey(company, division, warehouse, skuBarcode, season,
				seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn,
				inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3,
				skuAttribute4, skuAttribute5);

// validate the sku in the sku_inventory based on primary keys
		SkuInventory skuInventory = skuInventoryService.validateSku(skuInventoryKey);

		campaignCode = campaignCode.stripTrailing();

		if (campaignCode.isBlank()) {
// re visit
			LOGGER.info("Entered into Allocation function without campaign. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());
			skuInventory = allocateAndUpdateChannelWithoutCampaign(channel, campaignCode, dateTimeStamp, user,
					requestedQty, shipDate, action, skuInventory, skuInventoryKey);
			LOGGER.info("Exit from Allocation function without campaign. OnhandQty:" + skuInventory.getOnHandQuantity()
					+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
					+ skuInventory.getProtectedQuantity() + " LockedQty:" + skuInventory.getLockedQuantity()
					+ " AvailableQty:" + skuInventory.getAvailableQuantity());
			return skuInventory;
		}

// validate campaign header
		CampaignHeaderDb tempCampaignHeaderDb = campaignValidationService
				.validateHeaderId(new CampaignHeaderKey(channel, campaignCode));

		CampaignDetailKey campaignDetailKey = new CampaignDetailKey(new SKUInventoryKey(company, division, warehouse,
				skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode,
				sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2,
				skuAttribute3, skuAttribute4, skuAttribute5), channel, campaignCode);

// validate campaign detail
		CampaignDetailDb campaignDetailDb = campaignValidationService.validateDetailId(campaignDetailKey);

		LocalDate campaignStartDate = tempCampaignHeaderDb.getStartDate();
		LocalDate campaignEndDate = tempCampaignHeaderDb.getEndDate();
		String campaignActive = tempCampaignHeaderDb.getActive();

// validate shipDate with campaign start date
		if (DateParser.isBeforeOrEqual(shipDate, campaignStartDate)) {
			throw new Exception(invalidShipDate);
		}

// validate active status and running status of the campaign
// if both cases are valid, allocation process has to be done
		if (campaignActive.equals(campaignActiveYes)) {

			LOGGER.info("Entered into Allocation function with running campaign. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

// get original protect quantity
			int originalProtectQty = campaignDetailDb.getOriginalProtectQuantity();

// get current protect quantity
			int currentProtectQty = campaignDetailDb.getCurrentProtectQty();

// get minimum quantity
			int minQty = campaignDetailDb.getMinimumQuantity();

// everything can be allocated from protect qty itself
			if (requestedQty <= currentProtectQty) {

// reduce current protect
				currentProtectQty -= requestedQty;

// store new value
				campaignDetailDb.setCurrentProtectQty(currentProtectQty);
				campaignDetailDbRepository.save(campaignDetailDb);

// update SKU inventory
				skuInventoryService.updateAllocate(skuInventory, requestedQty);
				skuInventory = skuInventoryService.updateProtect(skuInventory, requestedQty);
				skuInventory = skuInventoryService.updateAvailable(skuInventory);

				invTransactionService.allocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
						dateTimeStamp, action, requestedQty);

				if (!campaignDetailDb.getAutoReplenish().toUpperCase().equals(autoReplenishNo)) {

					double availableQty = skuInventory.getAvailableQuantity();
					int intAvailableQty = (int) availableQty;

					if (currentProtectQty < minQty) { // going below minimum
						String replenishmnetAction = "R";
						int replenishmentQty = originalProtectQty - currentProtectQty;

						if (availableQty > 0) {
							LOGGER.info(
									"Entered into Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
											+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
											+ skuInventory.getProtectedQuantity() + " LockedQty:"
											+ skuInventory.getLockedQuantity() + " AvailableQty:"
											+ skuInventory.getAvailableQuantity());
							if (availableQty >= replenishmentQty) {
// store new value
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
// save to db
								campaignDetailDbRepository.save(campaignDetailDb);

								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);

							} else {
								replenishmentQty = intAvailableQty;
// store new value, increase current protect qty with whatever in the
// availableQty
								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
								campaignDetailDbRepository.save(campaignDetailDb);

// update available qty with replenishment qty, in this case available qty
// becomes zero
								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishmentQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishmnetAction, replenishmentQty);
							}

							LOGGER.info("Exit from Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
									+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
									+ skuInventory.getProtectedQuantity() + " LockedQty:"
									+ skuInventory.getLockedQuantity() + " AvailableQty:"
									+ skuInventory.getAvailableQuantity());
						}
					}
				}
				double availableQty = skuInventory.getAvailableQuantity();

				List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

				if (availableQty == 0) {
					emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
							subjectAvailableZeroAlert, null);
				}

			} else { // everything cannot be allocated from protect

				double availableQty = skuInventory.getAvailableQuantity();
				double existingDbAllocatedQty = skuInventory.getAllocatedQuantity();

				int totalQtyForAllocating = (int) (currentProtectQty + availableQty);

				if (availableQty == 0 && currentProtectQty == 0) { // failure

					List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

					if (availableQty == 0) {
						emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
								subjectAvailableZeroAlert, null);
					}

					String responseQty = Integer.toString(0);
					double dbOnhandQty = skuInventory.getOnHandQuantity();
					double dbAllocatedQty = skuInventory.getAllocatedQuantity();
					double dbProtectedQty = skuInventory.getProtectedQuantity();
					double dbLockedQty = skuInventory.getLockedQuantity();
					double dbAvailableQty = skuInventory.getAvailableQuantity();

					String strDbOnhandQty = Double.toString(dbOnhandQty);
					String strDbAllocatedQty = Double.toString(dbAllocatedQty);
					String strDbProtectedQty = Double.toString(dbProtectedQty);
					String strDbLockedQty = Double.toString(dbLockedQty);
					String strDbAvailableQty = Double.toString(dbAvailableQty);
					throw new AvailableQtyAndProtectQtyZero(availableQtyAndProtectQtyAreZero, responseQty,
							strDbOnhandQty, strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
				}

				campaignDetailDb.setCurrentProtectQty(0);
// save to the db
				campaignDetailDbRepository.save(campaignDetailDb);

				if (totalQtyForAllocating < requestedQty && channel.equals("WEB")) {
					int remainingQtyTobeAllocated = requestedQty - totalQtyForAllocating;

					skuInventoryService.updateProtect(skuInventory, currentProtectQty);
					skuInventoryService.updateAllocate(skuInventory, totalQtyForAllocating);
					skuInventory = skuInventoryService.updateAvailable(skuInventory);

					skuInventory = allocateAndUpdateForWebChannel(skuInventory, channel, campaignCode, dateTimeStamp,
							user, remainingQtyTobeAllocated, requestedQty, (int) existingDbAllocatedQty, action,
							skuInventoryKey, availableQty);

					availableQty = skuInventory.getAvailableQuantity();

					List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

					if (availableQty == 0) {
						emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
								subjectAvailableZeroAlert, null);
					}
					return skuInventory;
				}

				if (totalQtyForAllocating < requestedQty) {// partial success

					skuInventoryService.updateProtect(skuInventory, currentProtectQty);
					skuInventoryService.updateAllocate(skuInventory, totalQtyForAllocating);
					skuInventory = skuInventoryService.updateAvailable(skuInventory);
					invTransactionService.allocateForChannel(skuInventoryKey, totalQtyForAllocating, channel,
							campaignCode, user, dateTimeStamp, action, currentProtectQty);

					String responseQty = Integer.toString(totalQtyForAllocating);
					double dbOnhandQty = skuInventory.getOnHandQuantity();
					double dbAllocatedQty = skuInventory.getAllocatedQuantity();
					double dbProtectedQty = skuInventory.getProtectedQuantity();
					double dbLockedQty = skuInventory.getLockedQuantity();
					double dbAvailableQty = skuInventory.getAvailableQuantity();

					String strDbOnhandQty = Double.toString(dbOnhandQty);
					String strDbAllocatedQty = Double.toString(dbAllocatedQty);
					String strDbProtectedQty = Double.toString(dbProtectedQty);
					String strDbLockedQty = Double.toString(dbLockedQty);
					String strDbAvailableQty = Double.toString(dbAvailableQty);

					List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

					if (dbAvailableQty == 0) {
						emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
								subjectAvailableZeroAlert, null);
					}

					LOGGER.info("Exit from allocation function of running campaign with partial success. OnhandQty:"
							+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
							+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
							+ skuInventory.getLockedQuantity() + " AvailableQty:"
							+ skuInventory.getAvailableQuantity());

					throw new AvailableQtyLessThanRequestedQty(availableQtyAndProtectQtyLessThanRequested, responseQty,
							strDbOnhandQty, strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
				} else {// success
					skuInventoryService.updateProtect(skuInventory, currentProtectQty);
					skuInventoryService.updateAllocate(skuInventory, requestedQty);
					skuInventory = skuInventoryService.updateAvailable(skuInventory);
					invTransactionService.allocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
							dateTimeStamp, action, currentProtectQty);
				}

				if (!campaignDetailDb.getAutoReplenish().toUpperCase().equals(autoReplenishNo)) {

					availableQty = skuInventory.getAvailableQuantity();
					int intAvailableQty = (int) availableQty;
					currentProtectQty = campaignDetailDb.getCurrentProtectQty();
					int replenishmentQty = originalProtectQty - currentProtectQty;

					String replenishmnetAction = "R";

					if (availableQty > 0) {

						LOGGER.info("Entered into Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
								+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
								+ skuInventory.getProtectedQuantity() + " LockedQty:" + skuInventory.getLockedQuantity()
								+ " AvailableQty:" + skuInventory.getAvailableQuantity());

// current protect qty always goes below minimum qty in this case
						if (availableQty >= replenishmentQty) {
// store new value
							campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
// save to db
							campaignDetailDbRepository.save(campaignDetailDb);

							skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
									replenishmentQty);
							skuInventory = skuInventoryService.updateAvailable(skuInventory);

							invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
									user, dateTimeStamp, replenishmnetAction, replenishmentQty);

						} else {
							replenishmentQty = intAvailableQty;
// store new value, increase current protect qty with whatever in the
// availableQty
							campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishmentQty);
							campaignDetailDbRepository.save(campaignDetailDb);

// update available qty with replenishment qty
							skuInventoryService.updateProtectDuringReplenishment(skuInventory, replenishmentQty);
							skuInventory = skuInventoryService.updateAvailable(skuInventory);

							invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
									user, dateTimeStamp, replenishmnetAction, replenishmentQty);

						}

						LOGGER.info("Exit from Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
								+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
								+ skuInventory.getProtectedQuantity() + " LockedQty:" + skuInventory.getLockedQuantity()
								+ " AvailableQty:" + skuInventory.getAvailableQuantity());
					}
				}
				List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

				if (skuInventory.getAvailableQuantity() == 0) {
					emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
							subjectAvailableZeroAlert, null);
				}
			}
			LOGGER.info("Exit from Allocation function with running campaign and success. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());
		} else {
// if the campaign gets ended, allocation process has to be done without
// updating the campaign_detail_db
			if (campaignActive.equals("N") && DateParser.isBefore(campaignEndDate, LocalDate.now())) {
				LOGGER.info("Entered into Allocation function with ended campaign. OnhandQty:"
						+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
						+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
						+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());
				skuInventory = allocateAndUpdateChannelWithoutCampaign(channel, campaignCode, dateTimeStamp, user,
						requestedQty, shipDate, action, skuInventory, skuInventoryKey);
				LOGGER.info("Exit from Allocation function with ended campaign. OnhandQty:"
						+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
						+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
						+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

			} else {
// if the campaign is not yet started, there is no allocation process and throws
// an exception
				throw new CampaignIsNotAvailable(campaignIsNotAvailable);
			}
		}
		return skuInventory;
	}

	/*
	 * This method is called by deallocateAndUpdateForChannel method to process the
	 * deallocation request without specifying the campaign code
	 */
	public SkuInventory deallocateAndUpdateChannelWithCampaign(String channel, String campaignCode,
			LocalDateTime dateTimeStamp, String user, int requestedQty, String action, SkuInventory skuInventory,
			SKUInventoryKey skuInventoryKey) throws Exception {

		double dbAllocatedQty = skuInventory.getAllocatedQuantity();
		String responseQty = Double.toString(dbAllocatedQty);

		LOGGER.info("Entered into Deallocation function without campaign. OnhandQty:" + skuInventory.getOnHandQuantity()
				+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
				+ skuInventory.getProtectedQuantity() + " LockedQty:" + skuInventory.getLockedQuantity()
				+ " AvailableQty:" + skuInventory.getAvailableQuantity());

		if (dbAllocatedQty < requestedQty) {
			skuInventory = skuInventoryService.updateAllocateForDeallocation(skuInventory, requestedQty);
			skuInventory = skuInventoryService.updateAvailable(skuInventory);

			invTransactionService.deAllocateForChannel(skuInventoryKey, (int) dbAllocatedQty, channel, campaignCode,
					user, dateTimeStamp, action, 0);
			skuInventory = deAllocateReplenishment(skuInventory, skuInventoryKey, dateTimeStamp, user,
					(int) dbAllocatedQty);

			double dbOnhandQty = skuInventory.getOnHandQuantity();
			dbAllocatedQty = skuInventory.getAllocatedQuantity();
			double dbProtectedQty = skuInventory.getProtectedQuantity();
			double dbLockedQty = skuInventory.getLockedQuantity();
			double dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);

			LOGGER.info("Exit from Deallocation function without campaign and with partial success. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

			throw new AllocatedQtyIsLessThanRequestedQty(allocatedQtyLessThanRequestedQty, responseQty, strDbOnhandQty,
					strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		} else {
			skuInventory = skuInventoryService.updateAllocateForDeallocation(skuInventory, requestedQty);
			skuInventory = skuInventoryService.updateAvailable(skuInventory);

			invTransactionService.deAllocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
					dateTimeStamp, action, 0);
			skuInventory = deAllocateReplenishment(skuInventory, skuInventoryKey, dateTimeStamp, user, requestedQty);
		}

		LOGGER.info("Exit from Deallocation function without campaignCode and with complete success. OnhandQty:"
				+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
				+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
				+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

		return skuInventory;
	}

	/*
	 * This method is called by deallocateAndUpdateForChannel method to process the
	 * deallocation request for ended campaign
	 */
	public SkuInventory deallocateAndUpdateChannelWithEndedCampaign(String channel, String campaignCode,
			LocalDateTime dateTimeStamp, String user, int requestedQty, String action, SkuInventory skuInventory,
			SKUInventoryKey skuInventoryKey) throws Exception {

		double dbAllocatedQty = skuInventory.getAllocatedQuantity();
		String responseQty = Double.toString(dbAllocatedQty);

		LOGGER.info("Entered into Deallocation function with ended campaign. OnhandQty:"
				+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
				+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
				+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

		skuInventory = skuInventoryService.updateAllocateForDeallocation(skuInventory, requestedQty);
		skuInventory = skuInventoryService.updateAvailable(skuInventory);

		if (dbAllocatedQty < requestedQty) {

			double dbOnhandQty = skuInventory.getOnHandQuantity();
			dbAllocatedQty = skuInventory.getAllocatedQuantity();
			double dbProtectedQty = skuInventory.getProtectedQuantity();
			double dbLockedQty = skuInventory.getLockedQuantity();
			double dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);

			invTransactionService.deAllocateForChannel(skuInventoryKey, (int) dbAllocatedQty, channel, campaignCode,
					user, dateTimeStamp, action, 0);

			LOGGER.info("Exit from Deallocation function with ended campaign and partial success. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

			throw new AllocatedQtyIsLessThanRequestedQty(allocatedQtyLessThanRequestedQty, responseQty, strDbOnhandQty,
					strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		} else {
			invTransactionService.deAllocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
					dateTimeStamp, action, 0);
		}

		LOGGER.info("Exit from Deallocation function with ended campaign and omplete success. OnhandQty:"
				+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
				+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
				+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());
		return skuInventory;
	}

	/*
	 * This method is called by the allocateForChannel method to process the
	 * allocation request without campaign
	 */
	public SkuInventory allocateAndUpdateChannelWithoutCampaign(String channel, String campaignCode,
			LocalDateTime dateTimeStamp, String user, int requestedQty, LocalDate shipDate, String action,
			SkuInventory skuInventory, SKUInventoryKey skuInventoryKey) throws Exception {

		double availableQuantity = skuInventory.getAvailableQuantity();
		String responseQty = Double.toString(availableQuantity);
		double existingDbAllocatedQty = skuInventory.getAllocatedQuantity();
		String style = skuInventory.getId().getStyle();
		String styleSfx = skuInventory.getId().getStyleSfx();
		String color = skuInventory.getId().getColor();
		String colorSfx = skuInventory.getId().getColorSfx();
		String skuBarcode = skuInventory.getId().getSkuBarcode();

		List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

		if (campaignCode.isBlank()) {
			if (availableQuantity < requestedQty && channel.toUpperCase().equals("WEB")) {

				int remainingQtyTobeAllocated = (int) (requestedQty - availableQuantity);

				if (availableQuantity != 0) {
					skuInventoryService.updateAllocate(skuInventory, (int) availableQuantity);
					skuInventory = skuInventoryService.updateAvailable(skuInventory);
				}
// re visit
				skuInventory = allocateAndUpdateForWebChannel(skuInventory, channel, campaignCode, dateTimeStamp, user,
						remainingQtyTobeAllocated, requestedQty, (int) existingDbAllocatedQty, action, skuInventoryKey,
						availableQuantity);

				availableQuantity = skuInventory.getAvailableQuantity();

				if (skuInventory.getAvailableQuantity() == 0) {
					emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
							subjectAvailableZeroAlert, null);
				}

				return skuInventory;
			}
		}

		skuInventory = skuInventoryService.updateAllocateAndAvailable(skuInventory, requestedQty);

		if (availableQuantity < requestedQty) {

			double dbOnhandQty = skuInventory.getOnHandQuantity();
			double dbAllocatedQty = skuInventory.getAllocatedQuantity();
			double dbProtectedQty = skuInventory.getProtectedQuantity();
			double dbLockedQty = skuInventory.getLockedQuantity();
			double dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);

			invTransactionService.allocateForChannel(skuInventoryKey, (int) availableQuantity, channel, campaignCode,
					user, dateTimeStamp, action, 0);

			if (dbAvailableQty == 0) {
				emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
						subjectAvailableZeroAlert, null);
			}

			LOGGER.info(
					"Exit from Allocation function with partial success either be ended campaign or without campaign. OnhandQty:"
							+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
							+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
							+ skuInventory.getLockedQuantity() + " AvailableQty:"
							+ skuInventory.getAvailableQuantity());

			throw new AvailableQtyLessThanRequestedQty(availableQtyLessThanRequested, responseQty, strDbOnhandQty,
					strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		} else {
			invTransactionService.allocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
					dateTimeStamp, action, 0);
		}

		return skuInventory;
	}

	/*
	 * This method is used to process replenishment logic of the running and active
	 * campaign based on the replenish flag when processing the deallocation request
	 */
	public SkuInventory deAllocateReplenishment(SkuInventory skuInventory, SKUInventoryKey skuInventoryKey,
			LocalDateTime dateTimeStamp, String user, int requestedQty) throws Exception {
// check if the campaign is active and running
		List<CampaignHeaderDb> campaignHeaderDbs = (List<CampaignHeaderDb>) campaignHeaderDbRepository
				.findByActive(campaignActiveYes);

		for (CampaignHeaderDb campaignHeaderDb : campaignHeaderDbs) {

			String campaignCode = campaignHeaderDb.getId().getCampaignCode();
			String channel = campaignHeaderDb.getId().getChannel();

			CampaignDetailKey campaignDetailKey = new CampaignDetailKey(skuInventoryKey,
					campaignHeaderDb.getId().getChannel(), campaignHeaderDb.getId().getCampaignCode());
			String replenish = "Y";

// get all campaigndetails from campaign_detail_db
			CampaignDetailDb campaignDetailDb = campaignDetailDbRepository.findByIdAndAutoReplenish(campaignDetailKey,
					replenish);

			if (campaignDetailDb != null) {

				int currentProtectQty = campaignDetailDb.getCurrentProtectQty();
				int originalProtectQty = campaignDetailDb.getOriginalProtectQuantity();
				int minQty = campaignDetailDb.getMinimumQuantity();

				if ((currentProtectQty < originalProtectQty) && (currentProtectQty < minQty)) {

					int replenishQty = originalProtectQty - currentProtectQty;
					double availableQty = skuInventory.getAvailableQuantity();

					if (requestedQty != 0) {
						if (availableQty > 0) {
							LOGGER.info(
									"Entered into Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
											+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
											+ skuInventory.getProtectedQuantity() + " LockedQty:"
											+ skuInventory.getLockedQuantity() + " AvailableQty:"
											+ skuInventory.getAvailableQuantity());
							if (availableQty >= replenishQty) {

								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishQty);
								campaignDetailDbRepository.save(campaignDetailDb);

								String replenishAction = "R";

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishAction, replenishQty);
							} else {
								replenishQty = (int) availableQty;
								skuInventory = skuInventoryService.updateProtectDuringReplenishment(skuInventory,
										replenishQty);
								skuInventory = skuInventoryService.updateAvailable(skuInventory);

								campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishQty);
								campaignDetailDbRepository.save(campaignDetailDb);

								String replenishAction = "R";

								invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel,
										user, dateTimeStamp, replenishAction, replenishQty);
							}

							LOGGER.info("Exit from Replenishment logic. OnhandQty:" + skuInventory.getOnHandQuantity()
									+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
									+ skuInventory.getProtectedQuantity() + " LockedQty:"
									+ skuInventory.getLockedQuantity() + " AvailableQty:"
									+ skuInventory.getAvailableQuantity());
						}
						requestedQty = requestedQty - replenishQty;
					} else {
						return skuInventory;
					}
				}
			}
		}
		return skuInventory;
	}

	/*
	 * This method is called by the allocateForChannel method to process allocation
	 * request for the web channel
	 */
	public SkuInventory allocateAndUpdateForWebChannel(SkuInventory skuInventory, String channel, String campaignCode,
			LocalDateTime dateTimeStamp, String user, int remainingQtyTobeAllocated, int requestedQty,
			int existingDbAllocatedQty, String action, SKUInventoryKey skuInventoryKey, double availableQuantity)
			throws Exception {

		LOGGER.info("Entered into web channel allocation function. OnhandQty:" + skuInventory.getOnHandQuantity()
				+ " AllocatedQty:" + skuInventory.getAllocatedQuantity() + " ProtectQty:"
				+ skuInventory.getProtectedQuantity() + " LockedQty:" + skuInventory.getLockedQuantity()
				+ " AvailableQty:" + skuInventory.getAvailableQuantity());

		List<CampaignHeaderDb> campaignHeaderDbs = (List<CampaignHeaderDb>) campaignHeaderDbRepository
				.findByActive(campaignActiveYes);

		double dbProtectQty = skuInventory.getProtectedQuantity();

		if (availableQuantity == 0 && dbProtectQty == 0) { // failure

			String style = skuInventoryKey.getStyle();
			String styleSfx = skuInventoryKey.getStyleSfx();
			String color = skuInventoryKey.getColor();
			String colorSfx = skuInventoryKey.getColorSfx();
			String skuBarcode = skuInventoryKey.getSkuBarcode();

			List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

			if (availableQuantity == 0) {
				emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
						subjectAvailableZeroAlert, null);
			}

			String responseQty = Integer.toString(0);
			double dbOnhandQty = skuInventory.getOnHandQuantity();
			double dbAllocatedQty = skuInventory.getAllocatedQuantity();
			double dbProtectedQty = skuInventory.getProtectedQuantity();
			double dbLockedQty = skuInventory.getLockedQuantity();
			double dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);
			throw new AvailableQtyAndProtectQtyZero(availableQtyAndProtectQtyAreZero, responseQty, strDbOnhandQty,
					strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		}

		int remainingQtyTobeAllocatedFinal = remainingQtyTobeAllocated;

		for (CampaignHeaderDb campaignHeaderDb : campaignHeaderDbs) {

			CampaignDetailKey campaignDetailKey = new CampaignDetailKey(skuInventoryKey,
					campaignHeaderDb.getId().getChannel(), campaignHeaderDb.getId().getCampaignCode());

// get al campaigndetails from campaign_detail_db
			CampaignDetailDb tempcampaignDetailDb = campaignDetailDbRepository.findById(campaignDetailKey);

// for (CampaignDetailDb tempcampaignDetailDb : campaignDetailDbList) {

			if (tempcampaignDetailDb != null) {
				int currentProtectQty = tempcampaignDetailDb.getCurrentProtectQty();

				if (remainingQtyTobeAllocated <= currentProtectQty) {
					tempcampaignDetailDb.setCurrentProtectQty(currentProtectQty - remainingQtyTobeAllocated);
					campaignDetailDbRepository.save(tempcampaignDetailDb);
// re work
					skuInventory = skuInventoryService.updateProtect(skuInventory, remainingQtyTobeAllocated);
					skuInventory = skuInventoryService.updateAllocate(skuInventory, remainingQtyTobeAllocated);
					skuInventory = skuInventoryService.updateAvailable(skuInventory);

// re work
					invTransactionService.allocateForChannel(skuInventoryKey, 0, channel, campaignCode, user,
							dateTimeStamp, action, currentProtectQty);
					return skuInventory;
				} else {

					if (currentProtectQty != 0) {
						tempcampaignDetailDb.setCurrentProtectQty(0);
						campaignDetailDbRepository.save(tempcampaignDetailDb);

// re work
						skuInventory = skuInventoryService.updateProtect(skuInventory, currentProtectQty);
						skuInventory = skuInventoryService.updateAllocate(skuInventory, currentProtectQty);
						skuInventory = skuInventoryService.updateAvailable(skuInventory);
						invTransactionService.allocateForChannel(skuInventoryKey, 0, channel, campaignCode, user,
								dateTimeStamp, action, currentProtectQty);
					}
					remainingQtyTobeAllocated = remainingQtyTobeAllocated - currentProtectQty;
				}
			}
		}

		if (dbProtectQty >= remainingQtyTobeAllocatedFinal) {
			invTransactionService.allocateForChannel(skuInventoryKey, requestedQty, channel, campaignCode, user,
					dateTimeStamp, action, 0);
		} else {

			int allocatedQty = (int) (availableQuantity + dbProtectQty);
			invTransactionService.allocateForChannel(skuInventoryKey, allocatedQty, channel, campaignCode, user,
					dateTimeStamp, action, 0);
		}

		double updatedAllocatedQty = skuInventory.getAllocatedQuantity();
		int differenceAllocatedQty = (int) +(updatedAllocatedQty - existingDbAllocatedQty);

		if (requestedQty == differenceAllocatedQty) {
			LOGGER.info("Exit from Web Channel Allocation function with success. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());
			return skuInventory;
		} else {
// partial success

			String responseQty = Integer.toString(differenceAllocatedQty);
			double dbOnhandQty = skuInventory.getOnHandQuantity();
			double dbAllocatedQty = skuInventory.getAllocatedQuantity();
			double dbProtectedQty = skuInventory.getProtectedQuantity();
			double dbLockedQty = skuInventory.getLockedQuantity();
			double dbAvailableQty = skuInventory.getAvailableQuantity();

			String strDbOnhandQty = Double.toString(dbOnhandQty);
			String strDbAllocatedQty = Double.toString(dbAllocatedQty);
			String strDbProtectedQty = Double.toString(dbProtectedQty);
			String strDbLockedQty = Double.toString(dbLockedQty);
			String strDbAvailableQty = Double.toString(dbAvailableQty);

			String style = skuInventory.getId().getStyle();
			String styleSfx = skuInventory.getId().getStyleSfx();
			String color = skuInventory.getId().getColor();
			String colorSfx = skuInventory.getId().getColorSfx();
			String skuBarcode = skuInventory.getId().getSkuBarcode();

			List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

			if (dbAvailableQty == 0) {
				emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
						subjectAvailableZeroAlert, null);
			}
			LOGGER.info("Exit from Web Channel Allocation function with partial success. OnhandQty:"
					+ skuInventory.getOnHandQuantity() + " AllocatedQty:" + skuInventory.getAllocatedQuantity()
					+ " ProtectQty:" + skuInventory.getProtectedQuantity() + " LockedQty:"
					+ skuInventory.getLockedQuantity() + " AvailableQty:" + skuInventory.getAvailableQuantity());

			throw new AvailableQtyLessThanRequestedQty(availableQtyAndProtectQtyLessThanRequested, responseQty,
					strDbOnhandQty, strDbAllocatedQty, strDbProtectedQty, strDbLockedQty, strDbAvailableQty);
		}
	}
}
