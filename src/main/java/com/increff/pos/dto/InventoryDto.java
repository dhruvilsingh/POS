package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public InventoryData get(int id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.get(id);
        return convert(inventoryPojo);
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : list) {
            list2.add(convert(inventoryPojo));
        }
        return list2;
    }

    public void update(int id, InventoryForm inventoryForm) throws ApiException {
        if(productService.getId(inventoryForm.getProductBarcode()) == -1){
            throw new ApiException("Invalid barcode!!");
        }
        InventoryPojo inventoryPojo= convert(inventoryForm);
        inventoryService.update(id, inventoryPojo);
    }

    private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductId(inventoryPojo.getProductId());
        String productBarcode = productService.get(inventoryPojo.getProductId()).getProductBarcode();
        inventoryData.setProductBarcode(productBarcode);
        inventoryData.setProductQuantity(inventoryPojo.getProductQuantity());
        return inventoryData;
    }

    private InventoryPojo convert(InventoryForm inventoryForm){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductQuantity(inventoryForm.getProductQuantity());
        int productId = productService.getId(inventoryForm.getProductBarcode());
        inventoryPojo.setProductId(productId);
        return inventoryPojo;
    }

}
