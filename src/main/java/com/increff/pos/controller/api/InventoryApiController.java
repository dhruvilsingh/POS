package com.increff.pos.controller.api;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Api
public class InventoryApiController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Uploads inventory from TSV")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.PUT)
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.increaseInventory(inventoryForm);
    }

    @ApiOperation(value = "Adds inventory for multiple products")
    @RequestMapping(path = "/api/inventory/upload", method = RequestMethod.POST)
    public void processData(@RequestBody List<InventoryForm> fileData) throws ApiException {
        inventoryDto.upload(fileData);
    }

    @ApiOperation(value = "Gets inventory of a product by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        return inventoryDto.get(id);
    }

    @ApiOperation(value = "Gets list of all product inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException{
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates inventory of product by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.update(id, inventoryForm);
    }
}
