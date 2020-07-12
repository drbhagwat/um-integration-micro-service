package api.external.campaign.service;

import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;
import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.repo.CampaignDetailDbRepository;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.service.InvTransactionService;
import api.external.inventory.service.SkuInventoryService;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides operations for updateCurrentProtectQty via wmsInventory Add
 *
 * @author Anghulakshmi B
 * @version 1.0
 * @since 2019-10-14
 */

@Service
@Transactional
public class UpdateProtectQtyInCampaign {
	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	@Autowired
	private CampaignDetailDbRepository campaignDetailDbRepository;

	@Autowired
	private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

	@Autowired
	private SkuInventoryService skuInventoryService;

	@Autowired
	private InvTransactionService invTransactionService;

	@Value("${CAMPAIGN_ACTIVE_YES}")
	private String campaignActiveYes;

	/*
	 * This method perfroms updateProtectQty in campaign via wms inventory add
	 */

	public void updateProtectQtyInCampaign(SKUInventoryKey skuInventoryKey) throws Exception {
		// get all campaignheaders - check if the campaign is currently active and running
		List<CampaignHeaderDb> campaignHeaderDbs = (List<CampaignHeaderDb>) campaignHeaderDbRepository
				.findByActive(campaignActiveYes);

		for (CampaignHeaderDb campaignHeaderDb : campaignHeaderDbs) {
			// initialize the campaignheader fields
			String campaignCode = campaignHeaderDb.getId().getCampaignCode();
			String channel = campaignHeaderDb.getId().getChannel();
			String user = campaignHeaderDb.getUser();
			LocalDateTime dateTimeStamp = campaignHeaderDb.getDateTimeStamp();
			String action = "WMS Inventory Add";
			// get the campaigndetails
			CampaignDetailKey campaignDetailKey = new CampaignDetailKey(skuInventoryKey,
					campaignHeaderDb.getId().getChannel(), campaignHeaderDb.getId().getCampaignCode());
			String replenish = "Y";

			CampaignDetailDb campaignDetailDb = campaignDetailDbRepository.findByIdAndAutoReplenish(campaignDetailKey,
					replenish);

			if (campaignDetailDb != null) {
				// initialize the campaigndetail fields
				int currentProtectQty = campaignDetailDb.getCurrentProtectQty();
				int originalProtectQty = campaignDetailDb.getOriginalProtectQuantity();
				int minQty = campaignDetailDb.getMinimumQuantity();
				// validate sku with campaigndetail keys
				SkuInventory skuInventory = skuInventoryKeyValidationService.findSkuInventory(skuInventoryKey);
				// check currentProtectQty is less than minQty
				if ((currentProtectQty < originalProtectQty) && (currentProtectQty < minQty)) {
					double availableQty = skuInventory.getAvailableQuantity();
					int intAvailableQty = (int) availableQty;

					if (availableQty > 0) {
						int replenishQty = originalProtectQty - currentProtectQty;

						if (availableQty < replenishQty) {
							replenishQty = intAvailableQty;
						} // update sku_inventory table with available and protect qty
						skuInventoryService.subtractReplenishFromAvailable(skuInventory, replenishQty);
						// update invtransaction table
						invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode, channel, user,
								dateTimeStamp, action, replenishQty);

						campaignDetailDb.setCurrentProtectQty(currentProtectQty + replenishQty);
						// save to db
						campaignHeaderDbRepository.save(campaignHeaderDb);
					}
				}
			}
		}
	}
}