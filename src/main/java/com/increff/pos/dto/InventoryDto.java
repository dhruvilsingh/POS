package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public void add(InventoryForm inventoryForm) throws ApiException {
        String productBarcode = inventoryForm.getProductBarcode();
        update(productService.getId(productBarcode),inventoryForm);
    }

    public List<Map<String, Object>>  upload(List<Map<String, Object>> fileData){
        List<Map<String, Object>> errorData = new ArrayList<>();
        for(Map<String,Object> row : fileData){
            try {
                InventoryPojo inventoryPojo = convert(row);
                inventoryService.update(inventoryPojo.getProductId(), inventoryPojo);
            } catch (ApiException e) {
                row.put("error", e.getMessage());
                errorData.add(row);
            }
        }
        return errorData;
    }

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

    public List<InventoryReportData> getReport(){
        List<Object[]> resultList = inventoryService.getReport();;
        List<InventoryReportData> reportDataList = new ArrayList<>();
        for (Object[] result : resultList) {
            reportDataList.add(convert(result));
        }
        return reportDataList;
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

    private InventoryPojo convert(Map<String,Object> row) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        int productId = productService.getId(row.get("productBarcode").toString());
        inventoryPojo.setProductQuantity(inventoryService.get(productId).getProductQuantity() + Integer.valueOf(row.get("productQuantity").toString()).intValue());
        inventoryPojo.setProductId(productService.getId(row.get("productBarcode").toString()));
        return inventoryPojo;
    }

    private InventoryReportData convert(Object object[]){
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand((String) object[0]);
        inventoryReportData.setCategory((String) object[1]);
        inventoryReportData.setQuantity(((Long) object[2]).intValue());
        return inventoryReportData;
    }

    private InventoryPojo convert(InventoryForm inventoryForm){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductQuantity(inventoryForm.getProductQuantity());
        int productId = productService.getId(inventoryForm.getProductBarcode());
        inventoryPojo.setProductId(productId);
        return inventoryPojo;
    }

}
