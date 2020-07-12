package api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import api.core.errors.KeyFieldCannotBeBlank;
import api.core.errors.KeyFieldMandatory;
import api.core.errors.TransactionNumberCannotBeBlank;
import api.core.validation.CodeValidationService;
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.entity.CampaignResponseHeader;
import api.external.campaign.repo.CampaignResponseHeaderRepository;
import api.external.campaign.service.CampaignCreateService;
import api.external.campaign.service.CampaignGetService;
import api.external.campaign.service.CampaignRequestHistoryService;
import api.external.campaign.service.CampaignResponseHistoryService;
import api.external.campaign.service.CampaignUpdateService;
import api.external.campaign.util.CampaignRequest;
import api.external.campaign.validation.CampaignValidationService;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.inventory.service.SkuInventoryService;
import api.external.item.repo.ItemRepository;

/**
 * Provides unit test cases for Campaign API.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-01-28
 */

@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CampaignServiceTest {
	@InjectMocks
	private CampaignGetService campaignGetService;

	@Mock
	private SkuInventoryService skuInventoryService;

	@Mock
	private CampaignCreateService campaignCreateService;

	@Mock
	private CampaignUpdateService campaignUpdateService;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private SkuInventoryRepository skuInventoryRepository;

	@Mock
	private CampaignResponseHeaderRepository campaignResponseHeaderRepository;

	@Mock
	private CodeValidationService codeValidationService;

	@Mock
	private CampaignRequest campaignRequest;

	@Mock
	private CampaignResponseHistoryService campaignResponseHistoryService;

	@Mock
	private CampaignRequestHistoryService campaignRequestHistoryService;

	@Mock
	private CampaignValidationService campaignValidationService;

	@Test
	public void performCreateCampaignForOneValidSku() throws Exception {
		CampaignRequestHeader campaignRequestHeader = createHeaderRequest();

	}

	@Test
	public void performActionForExistingTransaction() throws Exception {
		CampaignRequestHeader campaignRequestHeader = createHeaderRequest();
		when(campaignResponseHeaderRepository.findByTransactionNumber(campaignRequestHeader.getTransactionNumber()))
				.thenReturn(new CampaignResponseHeader());
		assertNotNull(
				campaignResponseHeaderRepository.findByTransactionNumber(campaignRequestHeader.getTransactionNumber()));
		when(campaignGetService.createOrUpdate(campaignRequestHeader)).thenReturn(new CampaignResponseHeader());
	}
	
	private CampaignRequestHeader createHeaderRequest() {
		CampaignRequestHeader campaignRequestHeader = new CampaignRequestHeader();
		campaignRequestHeader.setTransactionNumber("WM0000000001");
		campaignRequestHeader.setChannel("Catalog");
		campaignRequestHeader.setCampaignCode("C00001");
		campaignRequestHeader.setCampaignStartDate(LocalDate.now());
		campaignRequestHeader.setCampaignEndDate(LocalDate.now().plusDays(10));
		campaignRequestHeader.setCampaignActive("Y");
		campaignRequestHeader.setAction("C");
		campaignRequestHeader.setDateTimeStamp(LocalDateTime.now());
		campaignRequestHeader.setUser("USER");
		campaignRequestHeader.setCampaignDetails(createCampaignDetailList());
		
		return campaignRequestHeader;
	}

	private List<CampaignRequestDetail> createCampaignDetailList() {
		List<CampaignRequestDetail> campaignDetailList = new ArrayList<>();
		CampaignRequestDetail campaignRequestDetail = new CampaignRequestDetail();
		campaignRequestDetail.setCompany("LEONISA");
		campaignRequestDetail.setDivision("LEONISA");
		campaignRequestDetail.setWarehouse("CSB");
		campaignRequestDetail.setSkuBarcode("887921535411");
		campaignRequestDetail.setSeason("");
		campaignRequestDetail.setSeasonYear("");
		campaignRequestDetail.setStyle("A10000");
		campaignRequestDetail.setStyleSfx("6");
		campaignRequestDetail.setColor("403");
		campaignRequestDetail.setColorSfx("");
		campaignRequestDetail.setSecDimension("");
		campaignRequestDetail.setQuality("");
		campaignRequestDetail.setSizeRngeCode("1170");
		campaignRequestDetail.setSizeRelPosnIn("1");
		campaignRequestDetail.setInventoryType("F");
		campaignRequestDetail.setLotNumber("");
		campaignRequestDetail.setCountryOfOrigin("");
		campaignRequestDetail.setProductStatus("");
		campaignRequestDetail.setSkuAttribute1("");
		campaignRequestDetail.setSkuAttribute2("");
		campaignRequestDetail.setSkuAttribute3("");
		campaignRequestDetail.setSkuAttribute4("");
		campaignRequestDetail.setSkuAttribute5("");
		campaignRequestDetail.setMinQty(5);
		campaignRequestDetail.setMaxQty(10);
		campaignRequestDetail.setProtectQty(10);
		campaignRequestDetail.setAutoReplenish("Y");

		campaignDetailList.add(campaignRequestDetail);
		return campaignDetailList;
	}

	private SKUInventoryKey createSkuInventoryKey() {
		SKUInventoryKey skuInventoryKey = new SKUInventoryKey();
		skuInventoryKey.setCompany("LEONISA");
		skuInventoryKey.setDivision("LEONISA");
		skuInventoryKey.setWarehouse("CSB");
		skuInventoryKey.setSkuBarcode("887921535411");
		skuInventoryKey.setSeason("");
		skuInventoryKey.setSeasonYear("");
		skuInventoryKey.setStyle("A10000");
		skuInventoryKey.setStyleSfx("6");
		skuInventoryKey.setColor("403");
		skuInventoryKey.setColorSfx("");
		skuInventoryKey.setSecDimension("");
		skuInventoryKey.setQuality("");
		skuInventoryKey.setSizeRngeCode("1170");
		skuInventoryKey.setSizeRelPosnIn("1");
		skuInventoryKey.setInventoryType("F");
		skuInventoryKey.setLotNumber("");
		skuInventoryKey.setCountryOfOrigin("");
		skuInventoryKey.setProductStatus("");
		skuInventoryKey.setSkuAttribute1("");
		skuInventoryKey.setSkuAttribute2("");
		skuInventoryKey.setSkuAttribute3("");
		skuInventoryKey.setSkuAttribute4("");
		skuInventoryKey.setSkuAttribute5("");
		return skuInventoryKey;
	}
}
