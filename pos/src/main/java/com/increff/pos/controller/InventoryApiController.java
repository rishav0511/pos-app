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
@RequestMapping(value = "/api/inventory")
public class InventoryApiController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Gets a product's inventory by barcode")
    @RequestMapping(value = "/{barcode}", method = RequestMethod.GET)
    public InventoryData getQuantityByBarcode(@PathVariable String barcode) throws ApiException {
        return inventoryDto.getInventory(barcode);
    }

    @ApiOperation(value = "Gets list of all products in inventory")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getInventory() throws ApiException {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates a product in inventory")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public InventoryData update(@RequestBody InventoryForm inventoryForm) throws ApiException {
        return inventoryDto.updateInventory(inventoryForm);
    }
}
