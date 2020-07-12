package api.external.common.key.validation;

import api.core.entities.DivisionKey;
import api.core.errors.*;
import api.core.validation.CodeValidationService;
import api.core.validation.DivisionValidationService;
import api.external.errors.*;
import api.external.item.entity.ItemKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * This class describes the ItemKeyValidator forchannel and campaign service.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-11-25
 */
@Component
public class ItemKeyValidatorForChannelAndCampaign {
	@Value("${COMPANY_MANDATORY}")
	private String companyMandatory;

	@Value("${COMPANY_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String companyCannotContainSpecialCharacters;

	@Value("${COMPANY_INVALID_LENGTH}")
	private String companyInvalidLength;

	@Value("${DIVISION_MANDATORY}")
	private String divisionMandatory;

	@Value("${DIVISION_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String divisionCannotContainSpecialCharacters;

	@Value("${DIVISION_INVALID_LENGTH}")
	private String divisionInvalidLength;

	@Value("${WAREHOUSE_MANDATORY}")
	private String warehouseMandatory;

	@Value("${WAREHOUSE_CANNOT_BE_BLANK}")
	private String warehouseCannotBeBlank;

	@Value("${WAREHOUSE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String warehouseCannotContainSpecialCharacters;

	@Value("${WAREHOUSE_INVALID_LENGTH}")
	private String warehouseInvalidLength;

	@Value("${ITEM_KEY_MANDATORY}")
	private String itemKeyMandatory;

	@Value("${SKUBARCODE_MANDATORY}")
	private String skuBarcodeMandatory;

	@Value("${SKUBARCODE_CANNOT_BE_BLANK}")
	private String skuBarcodeCannotBeBlank;

	@Value("${SKUBARCODE_MAX_LENGTH}")
	private int skuBarcodeMaxLength;

	@Value("${SKUBARCODE_INVALID_LENGTH}")
	private String skuBarcodeInvalidLength;

	@Value("${SKUBARCODE_CANNOT_CONTAIN_SPECIAL_CHARACTERS}")
	private String skuBarcodeCannotContainSpecialCharacters;

	@Value("${SEASON_MANDATORY}")
	private String seasonMandatory;

	@Value("${SEASON_YEAR_MANDATORY}")
	private String seasonYearMandatory;

	@Value("${STYLE_MANDATORY}")
	private String styleMandatory;

	@Value("${STYLE_SFX_MANDATORY}")
	private String styleSfxMandatory;

	@Value("${COLOR_MANDATORY}")
	private String colorMandatory;

	@Value("${COLOR_SFX_MANDATORY}")
	private String colorSfxMandatory;

	@Value("${SEC_DIMENSION_MANDATORY}")
	private String secDimensionMandatory;

	@Value("${QUALITY_MANDATORY}")
	private String qualityMandatory;

	@Value("${SIZE_RNGE_CODE_MANDATORY}")
	private String sizeRngeCodeMandatory;

	@Value("${SIZE_REL_POSN_IN_MANDATORY}")
	private String sizeRelPosnInMandatory;

	@Value("${WAREHOUSE_CODE_MAX_LENGTH}")
	private int warehouseCodeMaxLength;

	@Autowired
	private CodeValidationService codeValidationService;

	@Autowired
	private DivisionValidationService divisionValidationService;

	/*
	 * This method is to validate the Item with 14 keys
	 */
	public void validate(ItemKey itemKey) throws Exception {
		if (itemKey == null) {
			throw new ItemKeyIsMandatory(itemKeyMandatory);
		}
		String company = itemKey.getCompany();
		String division = itemKey.getDivision();
		String warehouse = itemKey.getWarehouse();
		String skuBarcode = itemKey.getSkuBarcode();
		String season = itemKey.getSeason();
		String seasonYear = itemKey.getSeasonYear();
		String style = itemKey.getStyle();
		String styleSfx = itemKey.getStyleSfx();
		String color = itemKey.getColor();
		String colorSfx = itemKey.getColorSfx();
		String secDimension = itemKey.getSecDimension();
		String quality = itemKey.getQuality();
		String sizeRngeCode = itemKey.getSizeRngeCode();
		String sizeRelPosnIn = itemKey.getSizeRelPosnIn();

		try {
			DivisionKey divisionKey = new DivisionKey(company, division);
			// validate company, division with empty, special characters and length check
			divisionValidationService.validate(divisionKey);
			// validate warehouse with empty, special characters and length check
			codeValidationService.validate(warehouse);
			warehouse = warehouse.stripTrailing();
			codeValidationService.validate(warehouse.length(), warehouseCodeMaxLength);
		} catch (CompanyCodeCannotContainSpecialCharacters exception) {
			throw new CompanyCodeCannotContainSpecialCharacters(companyCannotContainSpecialCharacters);
		} catch (CompanyMaxLengthExceeded exception) {
			throw new CompanyMaxLengthExceeded(companyInvalidLength);
		} catch (DivisionCodeCannotContainSpecialCharacters exception) {
			throw new DivisionCodeCannotContainSpecialCharacters(divisionCannotContainSpecialCharacters);
		} catch (DivisionMaxLengthExceeded exception) {
			throw new DivisionMaxLengthExceeded(divisionInvalidLength);
		} catch (CodeCannotContainSpecialCharacters exception) {
			throw new WarehouseCodeCannotContainSpecialCharacters(warehouseCannotContainSpecialCharacters);
		} catch (MaxLengthExceeded exception) {
			throw new WarehouseMaxLengthExceeded(warehouseInvalidLength);
		}

		try {

			if (Predicate.isEqual(null).test(season)) {
				throw new SeasonMandatory(seasonMandatory);
			}

			if (Predicate.isEqual(null).test(seasonYear)) {
				throw new SeasonYearMandatory(seasonYearMandatory);
			}

			if (Predicate.isEqual(null).test(style)) {
				throw new StyleMandatory(styleMandatory);
			}

			if (Predicate.isEqual(null).test(styleSfx)) {
				throw new StyleSfxMandatory(styleSfxMandatory);
			}

			if (Predicate.isEqual(null).test(color)) {
				throw new ColorMandatory(colorMandatory);
			}

			if (Predicate.isEqual(null).test(colorSfx)) {
				throw new ColorSfxMandatory(colorSfxMandatory);
			}

			if (Predicate.isEqual(null).test(secDimension)) {
				throw new SecDimensionMandatory(secDimensionMandatory);
			}

			if (Predicate.isEqual(null).test(quality)) {
				throw new QualityMandatory(qualityMandatory);
			}

			if (Predicate.isEqual(null).test(sizeRngeCode)) {
				throw new SizeRngeCodeMandatory(sizeRngeCodeMandatory);
			}

			if (Predicate.isEqual(null).test(sizeRelPosnIn)) {
				throw new SizeRelPosnInMandatory(sizeRelPosnInMandatory);
			}
			// validate skuBarcode with empty, blank, specialcharacters and length check
			codeValidationService.validateAll(skuBarcode);
			codeValidationService.validate(skuBarcode.length(), skuBarcodeMaxLength);
		} catch (CodeMandatory exception) {
			throw new SkuBarcodeMandatory(skuBarcodeMandatory);
		} catch (CodeCannotBeBlank exception) {
			throw new SkuBarcodeCannotBeBlank(skuBarcodeCannotBeBlank);
		} catch (CodeCannotContainSpecialCharacters exception) {
			throw new SkuBarcodeCannotContainSpecialCharacters(skuBarcodeCannotContainSpecialCharacters);
		} catch (MaxLengthExceeded exception) {
			throw new SkuBarcodeMaxLengthExceeded(skuBarcodeInvalidLength);
		}
	}
}