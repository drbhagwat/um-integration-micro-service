package api.external.campaign.repo;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignResponseDetail;

/**
 * This is the repository for CampaignResponseDetail entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */
@Repository
public interface CampaignResponseDetailRepository extends PagingAndSortingRepository<CampaignResponseDetail, Long> {
	/**
	 * @param pageable - pageable object
	 * @return - page of campaignresponsehistory found
	 */
	Page<CampaignResponseDetail> findAll(Pageable pageable);

	Page<CampaignResponseDetail> findByTransactionNumber(String transactionNumber, Pageable pageable);

	Page<CampaignResponseDetail> findByTransactionNumberIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndCampaignCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
			Pageable pageable, String transactionNumber, String warehouse, String channel, String campaignCode,
			String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);
}