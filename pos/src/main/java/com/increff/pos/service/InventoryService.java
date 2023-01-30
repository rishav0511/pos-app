package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public void insert(InventoryPojo inventoryPojo) {
        inventoryDao.insert(inventoryPojo);
    }

    public InventoryPojo getInventory(Integer productId) throws ApiException {
        return inventoryExists(productId);
    }

    public InventoryPojo inventoryExists(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (inventoryPojo == null) {
            throw new ApiException("Inventory doesn't exist");
        }
        return inventoryPojo;
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional
    public InventoryPojo updateInventory(InventoryPojo inventoryPojo) {
        InventoryPojo existing = inventoryDao.select(inventoryPojo.getProductId());
        existing.setQuantity(inventoryPojo.getQuantity());
        return inventoryDao.update(inventoryPojo);
    }

    // Reduce inventory quantity
    @Transactional(rollbackOn = ApiException.class)
    public void reduce(String barcode, int id, int quantity) throws ApiException {
        barcode = StringUtil.toLowerCase(barcode);
        InventoryPojo existing = inventoryDao.select(id);
        if (existing.getQuantity() < quantity) {
            throw new ApiException("Quantity not available for product, barcode:" + barcode);
        }
        existing.setQuantity(existing.getQuantity() - quantity);
        inventoryDao.update(existing);
    }

    // Increase inventory quantity
    @Transactional
    public void increase(int productId, int quantity) {
        InventoryPojo p = inventoryDao.select(productId);
        p.setQuantity(p.getQuantity() + quantity);
        inventoryDao.update(p);
    }
}
