package api.external.campaign.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignResponseHeader;

/**
 * This is the repository for CampaignResponseHeader entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */
@Repository
public interface CampaignResponseHeaderRepository
		extends PagingAndSortingRepository<CampaignResponseHeader, Long> {
	Page<CampaignResponseHeader> findAll(Pageable pageable);

	Page<CampaignResponseHeader> findByTransactionNumber(String transactionNumber, Pageable pageable);

	CampaignResponseHeader findByTransactionNumber(String transactionNumber);
}