package api.external.campaign.repo;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignRequestDetail;

/**
 * This is the repository for CampaignRequestDetail entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */
@Repository
public interface CampaignRequestDetailRepository extends PagingAndSortingRepository<CampaignRequestDetail, Long> {
	/**
	 * @param pageable - pageable object
	 * @return - page of campaignrequesthistory found
	 */
	Page<CampaignRequestDetail> findAll(Pageable pageable);

	Page<CampaignRequestDetail> findByTransactionNumber(String transactionNumber, Pageable pageable);

	Page<CampaignRequestDetail> findByTransactionNumberIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndCampaignCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
			Pageable pageable, String transactionNumber, String warehouse, String channel, String campaignCode,
			String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);
}