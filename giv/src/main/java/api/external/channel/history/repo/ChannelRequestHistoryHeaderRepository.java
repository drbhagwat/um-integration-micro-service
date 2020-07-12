package api.external.channel.history.repo;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.channel.history.entity.ChannelRequestHistoryHeader;

/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-17
 * 
 * This class represents repository for ChannelRequestHistoryHeader entity
 *
 */

@Repository
public interface ChannelRequestHistoryHeaderRepository extends PagingAndSortingRepository<ChannelRequestHistoryHeader, Long> {
	
}
