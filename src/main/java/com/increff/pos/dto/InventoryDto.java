package com.increff.pos.dto;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public void addInventory(InventoryForm inventoryForm) throws ApiException {
        normalize(inventoryForm);
        ValidationUtil.checkValid(inventoryForm);
        String productBarcode = inventoryForm.getBarcode();
        ProductPojo productPojo = productService.getCheckBarcode(productBarcode);
        if(inventoryForm.getQuantity() == 0){
            throw new ApiException("Quantity to add should be more than zero!");
        }
        InventoryPojo inventoryPojo =  convert(inventoryForm, productPojo.getId());
        inventoryService.addInventory(inventoryPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void upload(List<InventoryForm> fileData) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();
        for(InventoryForm inventoryForm: fileData){
            try {
                addInventory(inventoryForm);
            } catch (ApiException e) {
                Map<String,String> row = new HashMap<>();
                row.put("Barcode", inventoryForm.getBarcode());
                row.put("Quantity", inventoryForm.getQuantity().toString());
                row.put("Error", e.getMessage());
                errorList.add(row);
            }
        }
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
    }

    public InventoryData get(int id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.get(id);
        String productBarcode = productService.get(inventoryPojo.getProductId()).getBarcode();
        return convert(inventoryPojo, productBarcode);
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : list) {
            String productBarcode = productService.get(inventoryPojo.getProductId()).getBarcode();
            list2.add(convert(inventoryPojo, productBarcode));
        }
        return list2;
    }

    public void update(int id, InventoryForm inventoryForm) throws ApiException {
        ValidationUtil.checkValid(inventoryForm);
        ProductPojo productPojo = productService.getCheckBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo= convert(inventoryForm, productPojo.getId());
        inventoryService.update(id, inventoryPojo);
    }


    //Convert functions

    private static InventoryData convert(InventoryPojo inventoryPojo, String productBarcode){
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductId(inventoryPojo.getProductId());
        inventoryData.setBarcode(productBarcode);
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    private static InventoryForm convert(Map<String,Object> row){
        InventoryForm inventoryForm = new InventoryForm();
        String productBarcode = row.get("barcode").toString().trim();
        inventoryForm.setBarcode(productBarcode);
        inventoryForm.setQuantity(Integer.valueOf(row.get("quantity").toString()).intValue());
        return inventoryForm;
    }

    private static InventoryPojo convert(InventoryForm inventoryForm, int productId){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setProductId(productId);
        return inventoryPojo;
    }

    private static void normalize(InventoryForm inventoryForm){
        inventoryForm.setBarcode(StringUtil.toLowerCase(inventoryForm.getBarcode()));
    }

}
