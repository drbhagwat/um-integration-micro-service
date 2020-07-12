package api.external.mfg.validation;

import api.external.mfg.errors.CustomerOrderNumberCannotBeBlank;
import api.external.mfg.errors.CustomerOrderNumberMandatory;
import api.external.mfg.errors.ProductionOrderNumberMandatory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : Sachin Kulkarni
 * @date : 04-12-2019
 */
@Component
public class MfgValidationService {
    @Value("${PRODUCTION_ORDER_NUMBER_MANDATORY}")
    private String productionOrderNumberMandatory;

    @Value("${CUSTOMER_ORDER_NUMBER_MANDATORY}")
    private String customerOrderNumberMandatory;

    @Value("${CUSTOMER_ORDER_NUMBER_CANNOT_BLANK}")
    private String customerOrderNumberCannotBlank;

    public void validate(String productionOrderNumber, String customerOrderNumber) {

        if (productionOrderNumber == null) {
            throw new ProductionOrderNumberMandatory(productionOrderNumberMandatory);
        }

        if (customerOrderNumber == null) {
            throw new CustomerOrderNumberMandatory(customerOrderNumberMandatory);
        }

        if (customerOrderNumber.isBlank()) {
            throw new CustomerOrderNumberCannotBeBlank(customerOrderNumberCannotBlank);
        }
    }
}
