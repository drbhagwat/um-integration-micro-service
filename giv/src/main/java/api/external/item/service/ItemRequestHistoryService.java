package api.external.item.service;

import api.external.item.repo.ItemRequestHistoryRepository;
import api.external.item.requesthistory.ItemRequestHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author : Dinesh Bhagwat
 * @date : 11-10-2019
 */
@Service
public class ItemRequestHistoryService {
  @Autowired
  private ItemRequestHistoryRepository itemRequestHistoryRepository;

  public Page<ItemRequestHistory> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return itemRequestHistoryRepository.findAll(pageable);
  }
}
