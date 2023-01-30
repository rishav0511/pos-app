package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public InventoryData getInventory(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
        return ConvertUtil.convertPojotoData(inventoryPojo, productPojo);
    }


    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojos = inventoryService.getAll();
        List<InventoryData> inventoryData = new ArrayList<>();
        for (InventoryPojo pojo : inventoryPojos) {
            ProductPojo productPojo = productService.getProduct(pojo.getProductId());
            inventoryData.add(ConvertUtil.convertPojotoData(pojo, productPojo));
        }
        return inventoryData;
    }


    public InventoryData updateInventory(InventoryForm inventoryForm) throws ApiException {
        ValidationUtils.validateForm(inventoryForm);
        ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryService.updateInventory(inventoryPojo);
        return ConvertUtil.convertPojotoData(inventoryPojo, productPojo);
    }
}
