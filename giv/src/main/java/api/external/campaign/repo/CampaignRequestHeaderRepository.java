package api.external.campaign.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignRequestHeader;

/**
 * This is the repository for CampaignRequestHeader entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */
@Repository
public interface CampaignRequestHeaderRepository
		extends PagingAndSortingRepository<CampaignRequestHeader, Long> {
	Page<CampaignRequestHeader> findAll(Pageable pageable);
}