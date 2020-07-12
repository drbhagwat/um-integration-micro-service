package api.external.campaign.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.entity.CampaignHeaderKey;

/**
 * This is the repository for CampaignHeaderDb entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Repository
public interface CampaignHeaderDbRepository extends PagingAndSortingRepository<CampaignHeaderDb, Integer> {
/**
* @param pageable - pageable object
* @return - page of campaignheaders found
*/
Page<CampaignHeaderDb> findAll(Pageable pageable);

//@Query(value = "SELECT * FROM campaign_header_db WHERE running_active ='Y'", nativeQuery = true)
List<CampaignHeaderDb> findByActive(String campaignActive);

@Query(value = "SELECT * FROM campaign_header_db WHERE running_active ='Y'", nativeQuery = true)
List<CampaignHeaderDb> findByRunningActive();

List<CampaignHeaderDb> findAll();

@Query("select c from CampaignHeaderDb c where c.id.campaignCode = ?1")
public CampaignHeaderDb findByCampaignCode(String campaignCode);

/**
* @param id - primary key - (channel and campaignCode) by which you want to find the campaignheader in the db
* @return - particular campaignheader record found
*/
public CampaignHeaderDb findById(CampaignHeaderKey id);

/**
* @param pageable - pageable object
* @param campaignCode
* @param startDate
* @param isActive
* @return  - on success, it returns to search the page of campaign_header_db found
*/
@Query(value = "SELECT * FROM campaign_header_db WHERE campaign_code ILIKE %?1% AND start_date >= ?2 AND is_active ILIKE %?3%", nativeQuery = true)
Page<CampaignHeaderDb> searchCampaign(Pageable pageable, String campaignCode, LocalDate startDate, String isActive);
}