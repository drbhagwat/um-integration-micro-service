package api.core.repo;

import api.core.entities.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for Channel entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Repository
public interface ChannelRepository extends PagingAndSortingRepository<Channel, String> {
  Page<Channel> findAll(Pageable pageable);
}
