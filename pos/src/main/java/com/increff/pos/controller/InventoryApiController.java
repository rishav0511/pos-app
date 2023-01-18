package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryApiController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Gets a product's inventory by Id")
    @RequestMapping(value = "/api/inventory/{barcode}", method = RequestMethod.GET)
    public InventoryData getQuantitybyBarcode(@PathVariable String barcode) throws ApiException {
        return inventoryDto.get(barcode);
    }

    @ApiOperation(value = "Gets list of all products in inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getInventory() throws ApiException {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates a product in inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.PUT)
    public void update(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.update(inventoryForm);
    }
}
