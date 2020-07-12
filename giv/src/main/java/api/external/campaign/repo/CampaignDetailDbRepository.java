package api.external.campaign.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;

/**
 * This is the repository for CampaignDetailDb entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Repository
public interface CampaignDetailDbRepository extends PagingAndSortingRepository<CampaignDetailDb, Integer> {
	/**
	 * @param pageable - pageable object
	 * @return - page of campaigndetails found
	 */
	Page<CampaignDetailDb> findAll(Pageable pageable);
	
	/**
	 * @param id -primary key(company, division,
				warehouse, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality,
				sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1,
				skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5), channel, campaignCode) by which you want to find the campaigndetail in the db
	 * @return - particular campaigndetail record found
	 */
	public CampaignDetailDb findById(CampaignDetailKey id);

	CampaignDetailDb findByIdAndAutoReplenish(CampaignDetailKey id, String autoReplenish);
}