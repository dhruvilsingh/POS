package com.increff.pos.dto;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public void increaseInventory(InventoryForm inventoryForm) throws ApiException {
        NormalizeUtil.normalize(inventoryForm);
        ValidationUtil.checkValid(inventoryForm);
        if(inventoryForm.getQuantity() == 0){
            throw new ApiException("Quantity to add should be more than zero!");
        }
        ProductPojo productPojo = productService.getCheckBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo =  ConversionUtil.convert(inventoryForm, InventoryPojo.class);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.increaseInventory(inventoryPojo);
    }

    public void upload(List<InventoryForm> fileData) throws ApiException {
        List<Map<String, String>> errorList = ValidationUtil.normalizeValidateFormList(fileData);
        for (int i=0; i<fileData.size(); i++) {
            InventoryForm inventoryForm = fileData.get(i);
            if (inventoryForm.getQuantity() == 0) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", "Quantity to add should be more than zero!");
                errorList.add(row);
            }
        }
        if (!errorList.isEmpty()){
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        addList(fileData);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addList(List<InventoryForm> inventoryFormList) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();
        Map<String,Integer> productPojoMap = validateBarcodes(inventoryFormList, errorList);
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        for (int i = 0; i < inventoryFormList.size(); i++) {
            InventoryForm inventoryForm = inventoryFormList.get(i);
            InventoryPojo inventoryPojo = ConversionUtil.convert(inventoryForm, InventoryPojo.class);
            inventoryPojo.setProductId(productPojoMap.get(inventoryForm.getBarcode()));
            inventoryService.increaseInventory(inventoryPojo);
        }
    }

    private Map<String,Integer> validateBarcodes(List<InventoryForm> inventoryFormList , List<Map<String, String>> errorList){
        List<String> barcodeList = new ArrayList<>();
        for (int i = 0; i < inventoryFormList.size(); i++) {
            InventoryForm inventoryForm = inventoryFormList.get(i);
            barcodeList.add(inventoryForm.getBarcode());
        }
        return productService.validateListBarcodes(barcodeList, errorList,2);
    }

    public InventoryData get(int id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getCheck(id);
        String productBarcode = productService.getCheck(inventoryPojo.getProductId()).getBarcode();
        InventoryData inventoryData = ConversionUtil.convert(inventoryPojo, InventoryData.class);
        inventoryData.setBarcode(productBarcode);
        return inventoryData;
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : list) {
            String productBarcode = productService.getCheck(inventoryPojo.getProductId()).getBarcode();
            InventoryData inventoryData = ConversionUtil.convert(inventoryPojo, InventoryData.class);
            inventoryData.setBarcode(productBarcode);
            list2.add(inventoryData);
        }
        return list2;
    }

    public void update(int id, InventoryForm inventoryForm) throws ApiException {
        ValidationUtil.checkValid(inventoryForm);
        inventoryService.update(id, inventoryForm.getQuantity());
    }

}
