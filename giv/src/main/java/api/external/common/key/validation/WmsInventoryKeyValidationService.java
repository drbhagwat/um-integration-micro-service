package api.external.common.key.validation;

import api.core.entities.WarehouseKey;
import api.core.errors.*;
import api.core.services.ChannelService;
import api.core.services.WarehouseService;
import api.core.validation.CodeValidationService;
import api.external.errors.*;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.item.entity.ItemKey;
import api.external.util.SKUAttributeLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Provides WmsInventoryKeyValidationService.
 *
 * @author :Thamilarasi
 * @version : 1.0
 * @Version 2.0
 * @since : 2019-11-24
 * @since : 2019-11-27
 */
@Service
public class WmsInventoryKeyValidationService {
	@Autowired
	private ItemKeyValidatorForChannelAndCampaign itemKeyValidatorForChannelAndCampaign;

	@Autowired
	private SKUAttributeLength skuAttributeLength;

	@Autowired
	private CodeValidationService codeValidationService;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private ChannelService channelService;

	@Value("${DATETIME_IS_NOT_SPECIFIED_OR_EMPTY}")
	private String dateTimeIsNotSpecifiedOrEmpty;

	@Value("${INVALID_INVENTORY_TYPE}")
	private String invalidInventoryType;

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

	@Value("${CHANNEL_NOT_FOUND}")
	private String channelNotFound;

	@Value("${INVENTORY_TYPE_MANDATORY}")
	private String inventoryTypeMandatory;

	@Value("${LOT_NUMBER_MANDATORY}")
	private String lotNumberMandatory;

	@Value("${COUNTRY_OF_ORIGIN_MANDATORY}")
	private String countryOfOrigin;

	@Value("${PRODUCT_STATUS_MANDATORY}")
	private String productStatusMandatory;

	/**
	 * This method is to validate the channelCode field
	 *
	 * @param channelCode - Primary Key i.e., the channelCode - validate empty, blank, special characters and length check
	 * @throws Exception - on any validation error, it throws an appropriate exception.
	 */
	public void validate(String channelCode) throws Exception {
		try {
			codeValidationService.validateNullAndBlank(channelCode);

			if (!channelCode.matches("^[a-zA-Z0-9]*$")) {
				throw new ChannelCodeCannotContainSpecialCharacters(channelCannotContainSpecialCharacters);
			}
			channelCode = channelCode.stripTrailing();
			codeValidationService.validate(channelCode.length(), channelMaxLength);
			channelService.get(channelCode);
		} catch (MaxLengthExceeded exception) {
			throw new ChannelCodeMaxLengthExceeded(channelInvalidLength);
		} catch (ChannelNotFound exception) {
			throw new ChannelNotFound(channelNotFound);
		}
	}


	/**
	 * This method is to validate the dateTimeStamp field
	 *
	 * @param dateTimeStamp - validate empty check of dateTimeStamp parameter
	 * @throws Exception - on any validation error, it throws an appropriate exception.
	 */
	public void validate(LocalDateTime dateTimeStamp) throws Exception {

		if (dateTimeStamp == null) {
			throw new DateTimeIsMandatoryAndCannotBeBlank(dateTimeIsNotSpecifiedOrEmpty);
		}
	}

	/**
	 * Validates given code - for null, empty, invalid characters, and max length.
	 *
	 * @param skuInventoryKey - key for the sku inventory.
	 * @return none - on success, returns nothing.
	 * @throws Exception - on different failures, throws different exceptions.
	 */

	public void validate(SKUInventoryKey skuInventoryKey) throws Exception {
		String company = skuInventoryKey.getCompany();
		String division = skuInventoryKey.getDivision();
		String warehouse = skuInventoryKey.getWarehouse();
		String skuBarcode = skuInventoryKey.getSkuBarcode();
		String season = skuInventoryKey.getSeason();
		String seasonYear = skuInventoryKey.getSeasonYear();
		String style = skuInventoryKey.getStyle();
		String styleSfx = skuInventoryKey.getStyleSfx();
		String color = skuInventoryKey.getColor();
		String colorSfx = skuInventoryKey.getColorSfx();
		String secDimension = skuInventoryKey.getSecDimension();
		String quality = skuInventoryKey.getQuality();
		String sizeRngeCode = skuInventoryKey.getSizeRngeCode();
		String sizeRelPosnIn = skuInventoryKey.getSizeRelPosnIn();
		ItemKey itemKey = new ItemKey(company, division, warehouse, skuBarcode, season, seasonYear, style, styleSfx,
				color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn);
		// validate against item with 14 keys
		itemKeyValidatorForChannelAndCampaign.validate(itemKey);

		company = company.stripTrailing();
		division = division.stripTrailing();
		warehouse = warehouse.stripTrailing();

		if (!warehouse.isBlank()) {
			warehouseService.get(new WarehouseKey(company, division, warehouse));
		}
		String inventoryType = skuInventoryKey.getInventoryType();

		if (Predicate.isEqual(null).test(inventoryType)) {
			throw new InventoryTypeMandatory(inventoryTypeMandatory);
		}

		if (Predicate.isEqual("F").negate().test(inventoryType)) {
			throw new InvalidInventoryType(invalidInventoryType);
		}

		if (Predicate.isEqual(null).test(skuInventoryKey.getLotNumber())) {
			throw new LotNumberMandatory(lotNumberMandatory);
		}

		if (Predicate.isEqual(null).test(skuInventoryKey.getCountryOfOrigin())) {
			throw new CountryOfOriginMandatory(countryOfOrigin);
		}

		if (skuInventoryKey.getProductStatus() == null) {
			throw new ProductStatusMandatory(productStatusMandatory);
		}
		skuAttributeLength.validate(skuInventoryKey.getSkuAttribute1());
		skuAttributeLength.validate(skuInventoryKey.getSkuAttribute2());
		skuAttributeLength.validate(skuInventoryKey.getSkuAttribute3());
		skuAttributeLength.validate(skuInventoryKey.getSkuAttribute4());
		skuAttributeLength.validate(skuInventoryKey.getSkuAttribute5());
	}
}