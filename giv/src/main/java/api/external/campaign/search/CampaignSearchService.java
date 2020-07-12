package api.external.campaign.search;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.util.Converter;

/**
 * This class provides search services for Campaign Controller.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Service
public class CampaignSearchService {
	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	/**
	 * @param campaignSearchCriteria - which you want to process the search fields
	 *                               in the campaignSearchCriteria
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, return a page of particular campaign record existing in
	 *         db. otherwise, the global rest exception handler is automatically
	 *         called and a meaningful error message is displayed.
	 */
	public Page<CampaignHeaderDb> searchCampaign(CampaignSearchCriteria campaignSearchCriteria, Integer pageNo,
			Integer pageSize, String orderBy) throws Exception {
		String campaignCode = campaignSearchCriteria.getCampaignCode();
		String isActive = campaignSearchCriteria.getIsActive();
		String sortBy = campaignSearchCriteria.getSortBy();

		// handle both wild card and when the search parameter is not present in
		// campaign search criteria
		if ((sortBy == null) || (sortBy.equals("*")) || (sortBy.trim().equals(""))) {
			sortBy = "last_updated_date_time";
		} else { // convert the user supplied JSON parameter to equivalent db parameter
			sortBy = Converter.toDatabaseColumnName(sortBy);
		}

		if ((campaignCode == null) || campaignCode.equals("*") || campaignCode.equals("")) {
			campaignCode = "";
		} else {
			campaignCode = campaignCode.trim();
		}

		if ((isActive == null) || isActive.equals("*")) {
			isActive = "";
		} else {
			isActive = isActive.trim();
		}

		// campaignStartDate should be in the format of MM/dd/yyyy
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String startDate = campaignSearchCriteria.getStartDate();
		LocalDate ldtStartDate = null;

		if ((startDate == null) || startDate.equals("*") || startDate.equals("")) {
			startDate = "";
			ldtStartDate = LocalDate.of(2019, 01, 01);

		} else {
			startDate = startDate.trim();
			ldtStartDate = LocalDate.parse(startDate, dateTimeFormatter);
		}
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignHeaderDbRepository.searchCampaign(paging, campaignCode, ldtStartDate, isActive);
	}
}