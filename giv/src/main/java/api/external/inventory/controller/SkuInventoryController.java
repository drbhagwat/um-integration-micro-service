package api.external.inventory.controller;

import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.inventory.service.SkuInventoryService;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages Read operations of SKU inventory
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-01
 *
 */
@RestController
public class SkuInventoryController {
	@Autowired
	private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

	@Autowired
	private SkuInventoryService skuInventoryService;

	@Autowired
	private SkuInventoryRepository skuInventoryRepository;

	@GetMapping("/skus")
	public Page<SkuInventory> getAllWmsInv(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy, @RequestParam(defaultValue = "D") String orderBy){
		return skuInventoryService.getAllWmsInv(pageNo, pageSize, sortBy, orderBy);
	}

	@GetMapping("/skus/{skuInventoryKey}")
	public SkuInventory getWmsInv(@PathVariable SKUInventoryKey skuInventoryKey) throws Exception {
		skuInventoryKeyValidationService.validate(skuInventoryKey);
		return skuInventoryRepository.findById(skuInventoryKey).get();
	}
}
