package api.external.item.service;

import api.external.entity.BasicLogger;
import api.external.item.repo.ItemResponseHistoryRepository;
import api.external.item.responsehistory.ItemResponseHistory;
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
public class ItemResponseHistoryService extends BasicLogger<String> {
  @Autowired
  private ItemResponseHistoryRepository itemResponseHistoryRepository;

  public Page<ItemResponseHistory> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
    return itemResponseHistoryRepository.findAll(pageable);
  }
}
