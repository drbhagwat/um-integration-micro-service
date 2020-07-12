package api.external.util;

import api.core.errors.MaxLengthExceeded;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * Validates null and length checks of skuAttribute 1 to 5 fields in all
 * external APIs.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2020-07-22
 *
 */
@Component
public class SKUAttributeLength {
	@Value("${MAX_SKU_ATTRIBUTE_LENGTH}")
	private int maxSKUAttributeLength;

	@Value("${INVALID_SKU_ATTRIBUTE_LENGTH}")
	private String invalidSKUAttributeLength;

	@Value("${SKU_ATTRIBUTE_MANDATORY}")
	private String skuAttributeMandatory;

	public void validate(String skuAttribute) throws Exception {
		Predicate<Integer> pi = i -> (i > maxSKUAttributeLength);

		if (Predicate.isEqual(null).test(skuAttribute)) {
			throw new Exception(skuAttributeMandatory);
		}

		if (pi.test(skuAttribute.length())) {
			throw new MaxLengthExceeded(invalidSKUAttributeLength);
		}
	}
}
