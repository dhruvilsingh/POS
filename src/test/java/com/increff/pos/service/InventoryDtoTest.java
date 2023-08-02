package com.increff.pos.service;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    ProductDto productDto;
    @Autowired
    BrandDto brandDto;
    @Autowired
    InventoryDto inventoryDto;

    @Before
    public void addTestData() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(" Brand1 ");
        brandForm.setCategory("cAt1   ");
        brandDto.add(brandForm);
        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        for(int i=0; i<5 ; i++){
            productForm.setName("Product"+i);
            productForm.setBarcode("Barcode"+i);
            productForm.setMrp(Double.valueOf(100 + i*10));
            productDto.add(productForm);
        }
    }

    @Test
    public void testAddInventory() throws ApiException{
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(" BarcODe4 ");
        inventoryForm.setQuantity(100);
        inventoryDto.addInventory(inventoryForm);
        inventoryDto.addInventory(inventoryForm);

        int productId = inventoryDto.getAll().get(0).getProductId();

        assertEquals("barcode4",inventoryDto.get(productId).getBarcode());
        assertEquals(200, inventoryDto.get(productId).getQuantity(), 0);
    }

    @Test
    public void testAddInvalidInventory(){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("invalidBarcode ");
        inventoryForm.setQuantity(100);
        try{
            inventoryDto.addInventory(inventoryForm);
        }catch (ApiException exception){
            assertEquals("Invalid Barcode!", exception.getMessage());
        }
    }

    @Test
    public void testAddZeroInventory(){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("barcode4");
        inventoryForm.setQuantity(0);
        try{
            inventoryDto.addInventory(inventoryForm);
        }catch (ApiException exception){
            assertEquals("Quantity to add should be more than 0", exception.getMessage());
        }
    }

    @Test
    public void testGetByInvalidId(){
        try{
            inventoryDto.get(0);
        }catch(ApiException exception){
            assertEquals("Product with given ID does not exist!", exception.getMessage());
        }
    }

    @Test
    public void testGetAll() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm();
        for(int i=0; i<5 ; i++){
            inventoryForm.setBarcode("Barcode"+i);
            inventoryForm.setQuantity(100 + i*10);
            inventoryDto.addInventory(inventoryForm);
        }

        List<InventoryData> inventoryData = inventoryDto.getAll();
        int size = inventoryData.size();
        for(int i=0; i<size ; i++){
            assertEquals(100 + i*10, inventoryData.get(size-1-i).getQuantity(), 0);
            assertEquals("barcode"+i, inventoryData.get(size-1-i).getBarcode());
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setQuantity(100);
        inventoryForm.setBarcode("barcode1");
        int productId = inventoryDto.getAll().get(0).getProductId();
        inventoryDto.update(productId, inventoryForm);

        assertEquals("barcode4",inventoryDto.get(productId).getBarcode());
        assertEquals(100,inventoryDto.get(productId).getQuantity(), 0);
    }

//    @Test
//    public void testUpload() throws ApiException {
//        List<Map<String,Object>> inventoryList = new ArrayList<>();
//        for(int i = 0; i < 5; i++) {
//            Map<String, Object> inventoryMap = new HashMap<>();
//            inventoryMap.put("productBarcode","barcode"+i);
//            inventoryMap.put("productQuantity", 100);
//            inventoryList.add(inventoryMap);
//        }
//        Map<String, Object> inventoryMap = new HashMap<>();
//        inventoryMap.put("productBarcode", "barcode1");
//        inventoryMap.put("productQuantity", 0);
//        inventoryList.add(inventoryMap);
//
//        List<Map<String,Object>> errorList = inventoryDto.upload(inventoryList);
//        assertEquals("Quantity to add should be more than 0", errorList.get(0).get("error"));
//
//        List<InventoryData> inventoryData = inventoryDto.getAll();
//        int size = inventoryData.size();
//        for(int i=0; i<size ; i++){
//            assertEquals("barcode"+i, inventoryData.get(size-1-i).getBarcode());
//            assertEquals(100, inventoryData.get(size-1-i).getQuantity(), 0);
//        }
//    }

}
