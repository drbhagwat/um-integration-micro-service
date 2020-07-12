package api.external.campaign.validation;

import api.core.errors.CodeCannotBeBlank;
import api.core.errors.CodeCannotContainSpecialCharacters;
import api.core.errors.CodeMandatory;
import api.core.errors.MaxLengthExceeded;
import api.core.validation.CodeValidationService;
import api.email.EmailService;
import api.external.campaign.entity.*;
import api.external.campaign.errors.*;
import api.external.campaign.repo.CampaignDetailDbRepository;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.campaign.util.CampaignRequest;
import api.external.common.key.validation.WmsInventoryKeyValidationService;
import api.external.errors.*;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.InvTransactionService;
import api.external.inventory.service.SkuInventoryService;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.errors.ItemNotFound;
import api.external.item.repo.ItemRepository;
import api.external.util.DateParser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * This class describes the campaign validation service.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Service
public class CampaignValidationService {
	private static final Logger logger = LoggerFactory.getLogger(CampaignValidationService.class);

	@Value("${CAMPAIGN_CODE_MAX_LENGTH}")
	private int campaignCodeMaxLength;

	@Value("${CAMPAIGN_CODE_INVALID_LENGTH}")
	private String campaignCodeInvalidLength;

	@Value("${CAMPAIGN_CODE_MANDATORY}")
	private String campaignCodeIsMandatory;

	@Value("${CAMPAIGN_CODE_CANNOT_BE_BLANK}")
	private String campaignCodeCannotBeBlank;

	@Value("${UPDATE_ACTION}")
	private String updateAction;

	@Value("${CAMPAIGN_CODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String campaignCodeCannotContainSpecialCharacters;

	@Value("${ITEM_NOT_FOUND}")
	private String itemNotFound;

	@Value("${INVALID_CAMPAIGN_ACTIVE}")
	private String invalidCampaignActive;

	@Value("${INVALID_CAMPAIGN_START_DATE}")
	private String invalidCampaignStartDate;

	@Value("${INVALID_CAMPAIGN_END_DATE}")
	private String invalidCampaignEndDate;

	@Value("${CREATE_ACTION}")
	private String createAction;

	@Value("${AVAILABLE_QTY_IS_ZERO}")
	private String availableQtyIsZero;

	@Value("${CAMPAIGN_HEADER_ALREADY_EXISTS}")
	private String campaignHeaderAlreadyExists;

	@Value("${CAMPAIGN_ACTIVE_FIELD_IS_REQUIRED}")
	private String campaignActiveFieldIsRequired;

	@Value("${START_DATE_FIELD_IS_NOT_SPECIFIED_OR_EMPTY}")
	private String startDateFieldIsNotSpecifiedOrEmpty;

	@Value("${END_DATE_FIELD_IS_NOT_SPECIFIED_OR_EMPTY}")
	private String endDateFieldIsNotSpecifiedOrEmpty;

	@Value("${DATETIME_IS_NOT_SPECIFIED_OR_EMPTY}")
	private String dateTimeIsNotSpecifiedOrEmpty;

	@Value("${INVALID_CAMPAIGN_HEADER}")
	private String invalidCampaignHeader;

	@Value("${INVALID_START_DATE_IN_UPDATE}")
	private String invalidStartDateInUpdate;

	@Value("${AVAILABLE_QTY_LESS_THAN_REQUESTED}")
	private String availableQtyLessThanRequested;

	@Value("${CAMPAIGN_HEADER_NOT_FOUND}")
	private String campaignHeaderNotFound;

	@Value("${CAMPAIGN_DETAIL_NOT_FOUND}")
	private String campaignDetailNotFound;

	@Value("${SKU_NOT_FOUND}")
	private String skuNotFound;

	@Value("${SUBJECT_AVAILABLE_ZERO_ALERT}")
	private String subjectAvailableZeroAlert;

	@Value("${CAMPAIGN_ACTIVE_NO}")
	private String campaignActiveNo;

	@Value("${CAMPAIGN_ACTIVE_YES}")
	private String campaignActiveYes;

	@Autowired
	private EmailService emailService;

	@Autowired
	private Environment env;

	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	@Autowired
	private CampaignDetailDbRepository campaignDetailDbRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	CodeValidationService codeValidationService;

	@Autowired
	private CampaignRequest campaignRequest;

	@Autowired
	private SkuInventoryService skuInventoryService;

	@Autowired
	private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

	@Autowired
	private WmsInventoryKeyValidationService wmsInventoryKeyValidationService;

	@Autowired
	private InvTransactionService invTransactionService;

	private CampaignHeaderDb campaignHeaderDb = null;
	List<CampaignDetailDb> existingCampignsList = new ArrayList<>();

	/**
	 * This method is to validate the campaignCode field
	 *
	 * @param campaignCode - Primary Key i.e., the campaignCode - validate empty,
	 *                     blank, special characters and length check
	 * @throws Exception - on any validation error, it throws an appropriate
	 *                   exception.
	 */

	public String validate(String campaignCode) throws Exception {
		String savedCampaignCode = null;
		try {
			// validate campaignCode parameter - cannot not contain null, blank and special
			// characters
			codeValidationService.validateAll(campaignCode);
			// validate campaignCode length should be 20 characters
			savedCampaignCode = campaignCode.stripTrailing();
			codeValidationService.validate(savedCampaignCode.length(), campaignCodeMaxLength);
		} catch (CodeMandatory exception) {
			throw new CampaignCodeIsMandatory(campaignCodeIsMandatory);
		} catch (CodeCannotBeBlank exception) {
			throw new CampaignCodeCannotBeBlank(campaignCodeCannotBeBlank);
		} catch (CodeCannotContainSpecialCharacters exception) {
			throw new CampaignCodeCannotContainSpecialCharacters(campaignCodeCannotContainSpecialCharacters);
		} catch (MaxLengthExceeded exception) {
			throw new CampaignCodeMaxLengthExceeded(campaignCodeInvalidLength);
		}
		return savedCampaignCode;
	}

	/**
	 * This method is to find the campaignheaderkeys is exists in campaign_header_db
	 * or not
	 * 
	 * @param id - primary key (channel and campaignCode) by which you want to find
	 *           the campaignheader in the db.
	 * @return campaignheaderdb - on success, returns key fields with campaignheader
	 *         found
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios.
	 */

	public CampaignHeaderDb validateHeaderId(CampaignHeaderKey id) throws Exception {
		CampaignHeaderDb campaignHeaderDb = campaignHeaderDbRepository
				.findById(new CampaignHeaderKey(id.getChannel(), id.getCampaignCode()));

		if (campaignHeaderDb == null) {
			throw new CampaignHeaderNotFound(campaignHeaderNotFound);
		}
		return campaignHeaderDb;
	}

	/**
	 * This method is to find the campaigndetailkeys is exists in campaign_detail_db
	 * or not
	 * 
	 * @param id - id comprises of SKUInventorykey(23), channel and campaignCode by
	 *           which you want to find the campaigndetail in the db.
	 * @return campaigndetaildb - on success, returns key fields with campaigndetail
	 *         found
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios.
	 */

	public CampaignDetailDb validateDetailId(CampaignDetailKey id) throws Exception {

		CampaignDetailDb campaignDetailDb = campaignDetailDbRepository
				.findById(new CampaignDetailKey(
						new SKUInventoryKey(id.getSkuInventoryKey().getCompany(), id.getSkuInventoryKey().getDivision(),
								id.getSkuInventoryKey().getWarehouse(), id.getSkuInventoryKey().getSkuBarcode(),
								id.getSkuInventoryKey().getSeason(), id.getSkuInventoryKey().getSeasonYear(),
								id.getSkuInventoryKey().getStyle(), id.getSkuInventoryKey().getStyleSfx(),
								id.getSkuInventoryKey().getColor(), id.getSkuInventoryKey().getColorSfx(),
								id.getSkuInventoryKey().getSecDimension(), id.getSkuInventoryKey().getQuality(),
								id.getSkuInventoryKey().getSizeRngeCode(), id.getSkuInventoryKey().getSizeRelPosnIn(),
								id.getSkuInventoryKey().getInventoryType(), id.getSkuInventoryKey().getLotNumber(),
								id.getSkuInventoryKey().getCountryOfOrigin(),
								id.getSkuInventoryKey().getProductStatus(), id.getSkuInventoryKey().getSkuAttribute1(),
								id.getSkuInventoryKey().getSkuAttribute2(), id.getSkuInventoryKey().getSkuAttribute3(),
								id.getSkuInventoryKey().getSkuAttribute4(), id.getSkuInventoryKey().getSkuAttribute5()),
						id.getChannel(), id.getCampaignCode()));

		// validate campaigndetail keys with campaigndetaildb or not
		if (campaignDetailDb == null) {
			throw new CampaignDetailNotFound(campaignDetailNotFound);
		}
		return campaignDetailDb;
	}

	/**
	 * This method is to validate all campaignheader fields in campaign API
	 * 
	 * @param campaignHeader - the campaignHeader you want to process and validate
	 *                       the fields in header
	 * @return - on success, it returns the campaignheaderdb with key fields.
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios
	 */
	public CampaignHeaderDb validateCampaignHeader(CampaignRequestHeader campaignHeader) throws Exception {
		// validate channel and campaignCode
		CampaignHeaderKey campaignHeaderKey = validateCampaignHeaderKeys(campaignHeader);

		String action = campaignHeader.getAction();
		String campaignActiveDb = null;
		LocalDate dbStartDate = null;
		String existingChannel = null;

		campaignHeaderDb = campaignHeaderDbRepository.findByCampaignCode(campaignHeaderKey.getCampaignCode());
		//validate campaignheader if already exists or not in the create request
		if ((campaignHeaderDb != null) && (action.equalsIgnoreCase(createAction))) {
		throw new CampaignHeaderAlreadyExists(campaignHeaderAlreadyExists);
		}
		// if (Optional.ofNullable(campaignHeaderDb).isPresent()) {
		// 	throw new CampaignHeaderAlreadyExists(campaignHeaderAlreadyExists);
		// }
		// if (!Optional.ofNullable(campaignHeaderDb).isPresent()) {
		// 	throw new CampaignHeaderNotFound(invalidCampaignHeader);
		// }

		if ((campaignHeaderDb == null) && (action.equalsIgnoreCase(updateAction))) {
		throw new CampaignHeaderNotFound(invalidCampaignHeader);
		}

		if ((campaignHeaderDb != null) && (action.equalsIgnoreCase(updateAction))) {
			campaignActiveDb = campaignHeaderDb.getActive();
			dbStartDate = campaignHeaderDb.getStartDate();
			existingChannel = campaignHeaderDb.getId().getChannel();
		}

		LocalDate ldtStartDate = campaignHeader.getCampaignStartDate();
		// validate campaignStartDate parameter should be mandatory
		if (ldtStartDate == null) {
			throw new StartDateFieldIsMandatory(startDateFieldIsNotSpecifiedOrEmpty);
		}

		LocalDate currentDate = LocalDate.now();
		if (!DateParser.isBeforeOrEqual(currentDate, ldtStartDate)) {
			throw new InvalidCampaignStartDate(invalidCampaignStartDate);
		}

		LocalDate ldtEndDate = campaignHeader.getCampaignEndDate();
		// validate campaignEndDate parameter should be mandatory
		if (ldtEndDate == null) {
			throw new EndDateFieldIsMandatory(endDateFieldIsNotSpecifiedOrEmpty);
		}
		// validate campaignStartDate should be less than or equal to campaignEndDate
		if (!DateParser.isBeforeOrEqual(ldtStartDate, ldtEndDate)) {
			throw new InvalidCampaignEndDate(invalidCampaignEndDate);
		}

		String campaignActive = campaignHeader.getCampaignActive();
		// validate campaignActive parameter should be mandatory
		if (campaignActive == null) {
			throw new CampaignActiveFieldIsRequired(campaignActiveFieldIsRequired);
		}
		// validate campaignActive - should be Y or N
		if (!campaignRequest.validateCampaignActive(campaignActive)) {
			throw new InvalidCampaignActive(invalidCampaignActive);
		}

		LocalDateTime dateTimeStamp = campaignHeader.getDateTimeStamp();
		// validate dateTimeStamp parameter should be mandatory
		if (dateTimeStamp == null) {
			throw new DateTimeStampFieldIsRequired(dateTimeIsNotSpecifiedOrEmpty);
		}
		// validate
		if ((ldtStartDate.compareTo(ldtEndDate) == 0) && (campaignActive.equalsIgnoreCase(campaignActiveNo))
				&& (action.equalsIgnoreCase(createAction))) {
			throw new Exception("Cannot create campaign with same start date and end date");
		}

		if (ldtStartDate.compareTo(currentDate) == 0) {
			if (action.equalsIgnoreCase(createAction)) {
				campaignHeaderDb = new CampaignHeaderDb();
			}
			campaignHeaderDb.setActive(campaignActiveYes);

		} else {
			if (action.equalsIgnoreCase(createAction)) {
				campaignHeaderDb = new CampaignHeaderDb();
			}
			campaignHeaderDb.setActive(campaignActiveNo);
		}

		if (action.equalsIgnoreCase(updateAction)) {

			if (campaignActiveDb.equalsIgnoreCase(campaignActiveNo)) {
				if (!DateParser.isBefore(currentDate, ldtEndDate)) {
					throw new Exception("Campaign is already ended, so cannot change campaign end date again.");
				}
			}
			if (campaignActiveDb.equalsIgnoreCase(campaignActiveYes)) {
				if (!(existingChannel.equalsIgnoreCase(campaignHeader.getChannel()))) {
					throw new Exception(
							"Campaign is currently running and active, so cannot change campaign channel name again.");
				} else {
					if (ldtStartDate.compareTo(dbStartDate) != 0) {
						throw new InvalidStartDateInUpdate(invalidStartDateInUpdate);
					}
					if (!DateParser.isBeforeOrEqual(currentDate, ldtEndDate)) {
						throw new Exception(
								"Campaign is currently running and active, so cannot change campaign end date as earlier than today.");
					}
				}
			}

			if (currentDate.compareTo(ldtStartDate) == 0) {

				if ((currentDate.compareTo(ldtEndDate) == 0) && (campaignActive.equalsIgnoreCase(campaignActiveNo))
						&& (campaignActiveDb.equalsIgnoreCase(campaignActiveNo))) {
					throw new Exception("Campaign not yet started");
				} else {
					if (((campaignActiveDb.equalsIgnoreCase(campaignActiveYes)))
							&& (ldtEndDate.compareTo(currentDate) == 0)
							&& (campaignActive.equalsIgnoreCase(campaignActiveNo))) {

						// //check inventory release
						// campaignHeaderDb = campaignHeaderDbRepository.findById(campaignHeaderKey);
						// if (campaignHeaderDb == null) {
						// throw new CampaignHeaderNotFound(invalidCampaignHeader);
						// }
						List<CampaignDetailDb> campaignDetailDbsList = campaignHeaderDb.getCampaignDbDetails();
						campaignHeaderDb.setActive(campaignActiveNo);
						for (CampaignDetailDb campaignDetailDb : campaignDetailDbsList) {
							//SKUInventoryKey skuInventoryKey = campaignDetailDb.getId().getSkuInventoryKey();
							//int currentProtectQty = campaignDetailDb.getCurrentProtectQty();
							updateAvailFromProtect(campaignHeader, campaignDetailDb);

						}
					} else {
						List<CampaignDetailDb> campaignDetailDbsList = campaignHeaderDb.getCampaignDbDetails();

						// campaignDetailDbsList.stream()
						// 		.map(CampaignDetailDb -> {
						// 			SKUInventoryKey skuInventoryKey = CampaignDetailDb.getId().getSkuInventoryKey();

						// 		}).forEach();

						for (CampaignDetailDb campaignDetailDb : campaignDetailDbsList) {
							int currentProtectQty = campaignDetailDb.getCurrentProtectQty();
							if (campaignDetailDb.getOriginalProtectQuantity() != campaignDetailDb.getCurrentProtectQty()) {
								inventoryUpdation(campaignHeader, currentProtectQty, campaignDetailDb);
							}
						}
					}
				}
			}
			
			if((!(campaignHeaderDb.getId().getChannel().equalsIgnoreCase(campaignHeader.getChannel()))) && (campaignActiveDb.equalsIgnoreCase(campaignActiveNo))) {
				existingCampignsList = campaignHeaderDb.getCampaignDbDetails();
				
				List<CampaignDetailDb> campaignDetailList = new ArrayList<>();
			
				campaignHeaderDbRepository.delete(campaignHeaderDb);		
				campaignHeaderDb = new CampaignHeaderDb();	
				campaignHeaderDb = initHeaderKeys(campaignHeaderKey);

				for (CampaignDetailDb campaignDetailDb : existingCampignsList) {
					campaignDetailDb.setCampaignHeaderDb(campaignHeaderDb);
					campaignDetailList.add(campaignDetailDb);
				}
				campaignHeaderDb.setActive(campaignActiveNo);

				campaignHeaderDb.setCampaignDbDetails(campaignDetailList);
				}
		
		}
		campaignHeaderDb = initHeaderKeys(campaignHeaderKey);

		campaignHeaderDb.setStartDate(ldtStartDate);
		campaignHeaderDb.setEndDate(ldtEndDate);
		campaignHeaderDb.setAction(action);
		campaignHeaderDb.setUser(campaignHeader.getUser());
		campaignHeaderDb.setDateTimeStamp(dateTimeStamp);
		return campaignHeaderDb;
	}

	/**
	 * This method is to validate campaignheaderkeys(channel and campaignCode)
	 * 
	 * @param campaignHeader - primary key(channel and campaignCode)by which you
	 *                       want to validate fields in the campaignHeader
	 * @return
	 * @return campaignHeaderdb - on success, return store key fields with
	 *         campaignheaderdb.
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios.
	 */
	public CampaignHeaderKey validateCampaignHeaderKeys(CampaignRequestHeader campaignHeader) throws Exception {
		String campaignCode = campaignHeader.getCampaignCode();
		// validate campaignCode parameter with null, empty, special characters and
		// length check
		campaignCode = validate(campaignCode);

		String channel = campaignHeader.getChannel();
		// validate channel parameter with null, empty, special characters and length
		// check
		wmsInventoryKeyValidationService.validate(channel);
		return new CampaignHeaderKey(channel, campaignCode);
	}

	private CampaignHeaderDb initHeaderKeys(CampaignHeaderKey campaignHeaderKey) {
		campaignHeaderDb.setId(campaignHeaderKey);
		return campaignHeaderDb;
	}

	/**
	 * This method is to validate campaigndetailkeys(company, division, warehouse,
	 * skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx,
	 * secDimension, quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber,
	 * countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3,
	 * skuAttribute4, skuAttribute5, channel, campaignCode)
	 * 
	 * @param campaignHeader - primary key(channel, campaignCode)by which you want
	 *                       to validate key fields in the campaignHeader
	 * @param campaignDetail - primary key(company, division, warehouse, skuBarcode,
	 *                       season, seasonYear, style, styleSfx, color, colorSfx,
	 *                       secDimension, quality, sizeRngeCode, sizeRelPosnIn,
	 *                       inventoryType, lotNumber, countryOfOrigin,
	 *                       productStatus, skuAttribute1, skuAttribute2,
	 *                       skuAttribute3, skuAttribute4, skuAttribute5)by which
	 *                       you want to validate key fields in the campaignDetail
	 * @return campaigndetaildb - on success, return to store key fields with
	 *         campaigndetaildb.
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios
	 */
	public CampaignDetailDb validateCampaignDetailKeys(CampaignRequestHeader campaignHeader,
			CampaignRequestDetail campaignDetail) throws Exception {
		String company = null;
		String division = null;
		String warehouse = null;
		SKUInventoryKey skuInventoryKey = null;
		String channel = campaignHeader.getChannel();
		String campaignCode = campaignHeader.getCampaignCode();
		company = campaignDetail.getCompany();
		division = campaignDetail.getDivision();
		warehouse = campaignDetail.getWarehouse();
		String skuBarcode = campaignDetail.getSkuBarcode();
		String productStatus = campaignDetail.getProductStatus();
		String skuAttribute1 = campaignDetail.getSkuAttribute1();
		String skuAttribute2 = campaignDetail.getSkuAttribute2();
		String skuAttribute3 = campaignDetail.getSkuAttribute3();
		String skuAttribute4 = campaignDetail.getSkuAttribute4();
		String skuAttribute5 = campaignDetail.getSkuAttribute5();
		String season = campaignDetail.getSeason();
		String seasonYear = campaignDetail.getSeasonYear();
		String style = campaignDetail.getStyle();
		String styleSfx = campaignDetail.getStyleSfx();
		String color = campaignDetail.getColor();
		String colorSfx = campaignDetail.getColorSfx();
		String secDimension = campaignDetail.getSecDimension();
		String quality = campaignDetail.getQuality();
		String sizeRngeCode = campaignDetail.getSizeRngeCode();
		String sizeRelPosnIn = campaignDetail.getSizeRelPosnIn();
		String inventoryType = campaignDetail.getInventoryType();
		String lotNumber = campaignDetail.getLotNumber();
		String countryOfOrigin = campaignDetail.getCountryOfOrigin();

		skuInventoryKey = new SKUInventoryKey(company, division, warehouse, skuBarcode, season, seasonYear, style,
				styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber,
				countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4,
				skuAttribute5);
		// validating sku inventory key fields
		wmsInventoryKeyValidationService.validate(skuInventoryKey);

		company = company.stripTrailing();
		division = division.stripTrailing();
		warehouse = warehouse.stripTrailing();

		Item item = null;
		// if the warehouse is blank in the campaign request, check against the item
		// with 13 keys to get warehouse
		if (warehouse.isBlank()) {
			item = itemRepository
					.findByCompanyAndDivisionAndSkuBarcodeAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn(
							company, division, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx,
							secDimension, quality, sizeRngeCode, sizeRelPosnIn);

			if (item == null) {
				throw new ItemNotFound(itemNotFound);
			} else {
				warehouse = item.getId().getWarehouse();
			}
		} else {
			// if the warehouse is non blank in the campaign request, check against the item
			// with 14 keys
			ItemKey itemKey = new ItemKey(company, division, warehouse, skuBarcode, season, seasonYear, style, styleSfx,
					color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);

			Optional<Item> existingItem = itemRepository.findById(itemKey);

			if (existingItem.isEmpty()) {
				throw new ItemNotFound(itemNotFound);
			}
		}
		skuInventoryKey = new SKUInventoryKey(company, division, warehouse, skuBarcode, season, seasonYear, style,
				styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber,
				countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4,
				skuAttribute5);

		CampaignDetailDb campaignDetailDb = new CampaignDetailDb();

		CampaignDetailKey tempCampaignDetailKey = new CampaignDetailKey(skuInventoryKey, channel,
				campaignCode.stripTrailing());

		campaignDetailDb.setId(tempCampaignDetailKey);

		return campaignDetailDb;
	}

	/**
	 * This method is to validate campaignCode exists in campaign_header_db or not
	 * 
	 * @param campaignCode - of the campaign you want to validate the campaignCode
	 *                     field
	 * @return campaignHeaderDb - on success, returns the campaignheaderdb found
	 * @throws Exception - on any failure, throws different exceptions based on
	 *                   different scenarios.
	 */

	public void campaignValidate(String campaignCode) throws Exception {
		try {
			if (campaignCode == null) {
				throw new CampaignCodeIsMandatory(campaignCodeIsMandatory);
			}

			if (!campaignCode.matches("^[ a-zA-Z0-9]*$")) {
				throw new CampaignCodeCannotContainSpecialCharacters(campaignCodeCannotContainSpecialCharacters);
			}
			campaignCode = campaignCode.stripTrailing();
			codeValidationService.validate(campaignCode.length(), campaignCodeMaxLength);
		} catch (MaxLengthExceeded exception) {
			throw new CampaignCodeMaxLengthExceeded(campaignCodeInvalidLength);
		}
	}

	public void inventoryUpdation(CampaignRequestHeader campaignHeader, int protectQty, CampaignDetailDb campaignDetailDb) throws Exception {
		String campaignCode = campaignHeader.getCampaignCode();
		String channel = campaignHeader.getChannel();
		String user = campaignHeader.getUser();
		String action = campaignHeader.getAction();
		LocalDateTime dateTimeStamp = campaignHeader.getDateTimeStamp();
		SKUInventoryKey skuInventoryKey = campaignDetailDb.getId().getSkuInventoryKey();
							String style = campaignDetailDb.getId().getSkuInventoryKey().getStyle();
							String styleSfx = campaignDetailDb.getId().getSkuInventoryKey().getStyleSfx();
							String color = campaignDetailDb.getId().getSkuInventoryKey().getColor();
							String colorSfx = campaignDetailDb.getId().getSkuInventoryKey().getColorSfx();
							String skuBarcode = campaignDetailDb.getId().getSkuInventoryKey().getSkuBarcode();

		SkuInventory skuInventory = skuInventoryKeyValidationService.findSkuInventory(skuInventoryKey);

		double availableQty = skuInventory.getAvailableQuantity();
		int intAvailableQty = (int) availableQty;

		logger.info("Entered into Inventory updation for campaign create - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + availableQty + ", ProtectQty:"
				+ skuInventory.getProtectedQuantity() + ", AllocatedQty:" + skuInventory.getAllocatedQuantity()
				+ ", LockedQty:" + skuInventory.getLockedQuantity());
		if (availableQty >= protectQty) {
			// update available_qty with original_protect_qty
			skuInventoryService.subtract(skuInventory, protectQty);
			invTransactionService.updateProtectQtyForCampaign(skuInventoryKey, campaignCode, channel, protectQty,
					action, user, dateTimeStamp);
			campaignDetailDb.setCurrentProtectQty(campaignDetailDb.getCurrentProtectQty() + protectQty);
		} else {
			if (availableQty == 0) {
				campaignHeaderDb.setRunningActive("Y");
				List<String> paramsToBeReplaced = Arrays.asList(style, styleSfx, color, colorSfx, skuBarcode);

				emailService.sendMail(env.getProperty("CONF_DIR") + "/zeroAvailableQty.txt", paramsToBeReplaced,
						subjectAvailableZeroAlert, null);
			} else {
				// update available_qty with original_protect_qty, in this case available_qty
				// becomes zero
				skuInventoryService.subtract(skuInventory, protectQty);
				invTransactionService.updateProtectQtyForCampaign(skuInventoryKey, campaignCode, channel, availableQty,
						action, user, dateTimeStamp);
				campaignDetailDb.setCurrentProtectQty(intAvailableQty);
				campaignHeaderDbRepository.save(campaignHeaderDb);
				campaignDetailDbRepository.save(campaignDetailDb);
				logger.info("Exit from Inventory updation for campaign create with partial success - OnHandQuantity:"
						+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
						+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
						+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());
				throw new AvailableQtyLessThanProtectQty(availableQtyLessThanRequested);
			}
		}
		logger.info("Exit from Inventory updation for campaign create - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
				+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
				+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());
	}

	public void updateAvailFromProtect(CampaignRequestHeader campaignHeader, CampaignDetailDb campaignDetailDb) throws Exception {
		String campaignCode = campaignHeader.getCampaignCode();
		String channel = campaignHeader.getChannel();
		String user = campaignHeader.getUser();
		String action = campaignHeader.getAction();
		LocalDateTime dateTimeStamp = campaignHeader.getDateTimeStamp();
		SKUInventoryKey skuInventoryKey = campaignDetailDb.getId().getSkuInventoryKey();
							int currentProtectQty = campaignDetailDb.getCurrentProtectQty();

		SkuInventory skuInventory = skuInventoryKeyValidationService.findSkuInventory(skuInventoryKey);

		logger.info("Entered into Inventory updation for campaign update - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
				+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
				+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());

		skuInventory = skuInventoryService.addProtectQtyToAvailableQty(skuInventory, currentProtectQty);
		invTransactionService.updateAvailFromProtect(skuInventoryKey, campaignCode, channel, user, dateTimeStamp,
				action, Double.valueOf(currentProtectQty));
		campaignDetailDb.setCurrentProtectQty(0);
		logger.info("Exit from Inventory updation for campaign update - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
				+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
				+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());
	}

	public void inventoryReplenishment(CampaignRequestHeader campaignHeader,
			int replenishQty, int currentProtectQty, CampaignDetailDb campaignDetailDb) throws Exception {
		String campaignCode = campaignHeader.getCampaignCode();
		String channel = campaignHeader.getChannel();
		String user = campaignHeader.getUser();
		String action = campaignHeader.getAction();
		LocalDateTime dateTimeStamp = campaignHeader.getDateTimeStamp();
		SKUInventoryKey skuInventoryKey = campaignDetailDb.getId().getSkuInventoryKey();

		SkuInventory skuInventory = skuInventoryKeyValidationService.findSkuInventory(skuInventoryKey);

		logger.info("Entered into Inventory updation for campaign update - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
				+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
				+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());

		skuInventory = skuInventoryService.addProtectQtyToAvailableQty(skuInventory, replenishQty);
		invTransactionService.updateAvailFromProtect(skuInventoryKey, campaignCode, channel, user, dateTimeStamp,
				action, Double.valueOf(replenishQty));
		campaignDetailDb.setCurrentProtectQty(currentProtectQty);
		logger.info("Exit from Inventory updation for campaign update - OnHandQuantity:"
				+ skuInventory.getOnHandQuantity() + ", AvailableQty:" + skuInventory.getAvailableQuantity()
				+ ", ProtectQty:" + skuInventory.getProtectedQuantity() + ", AllocatedQty:"
				+ skuInventory.getAllocatedQuantity() + ", LockedQty:" + skuInventory.getLockedQuantity());
	}
}