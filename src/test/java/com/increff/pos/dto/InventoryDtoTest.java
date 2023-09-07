package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    ProductService productService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    InventoryDto inventoryDto;
    @Autowired
    InventoryDao inventoryDao;
    @Autowired
    InventoryService inventoryService;

    private List<Integer> productIdList = new ArrayList<>();

    @Before
    public void addTestData() throws ApiException {
        brandDao.insert(ConversionUtil.convert(TestUtil.getBrandForm("brand1","category1"), BrandPojo.class));

        List<ProductForm> productFormList = TestUtil.getProductFormList();
        int brandCategoryId = brandDao.selectAll().get(0).getId();
        List<ProductPojo> productPojoList = ConversionUtil.convert(productFormList, ProductPojo.class);;
        for(int i=0; i<5 ; i++){
            productPojoList.get(i).setBrandCategoryId(brandCategoryId);
        }
        productIdList = productService.addList(productPojoList);
        for(int i=0; i<5; i++){
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productIdList.get(i));
            inventoryPojo.setQuantity(0);
            inventoryDao.insert(inventoryPojo);
        }
    }

    //Test inventory increase
    @Test
    public void testAddInventory() throws ApiException{
        InventoryForm inventoryForm = TestUtil.getInventoryForm("barcode0", 100);
        inventoryDto.increaseInventory(inventoryForm);
        inventoryDto.increaseInventory(inventoryForm);

        InventoryPojo inventoryPojo = inventoryDao.selectAll().get(4);

        assertEquals(inventoryPojo.getProductId() ,productIdList.get(0));
        assertEquals(200, (int)inventoryPojo.getQuantity());
    }

    //Test negative inventory validation
    @Test
    public void testAddInvalidInventory(){
        InventoryForm inventoryForm =  TestUtil.getInventoryForm("barcode0", -100);
        try{
            inventoryDto.increaseInventory(inventoryForm);
            fail();
        }catch (ApiException exception){
            assertEquals("Quantity cannot be negative", exception.getMessage());
        }
    }

    //Test adding zero inventory validation
    @Test
    public void testAddZeroInventory(){
        InventoryForm inventoryForm =  TestUtil.getInventoryForm("barcode0", 0);
        try{
            inventoryDto.increaseInventory(inventoryForm);
            fail();
        }catch (ApiException exception){
            assertEquals("Quantity to add should be more than zero!", exception.getMessage());
        }
    }

    //Test get inventory by ID
    @Test
    public void testGetById() throws ApiException {
        InventoryForm inventoryForm = TestUtil.getInventoryForm("barcode0", 100);
        InventoryPojo inventoryPojo = ConversionUtil.convert(inventoryForm, InventoryPojo.class);
        int productId = productIdList.get(0);
        inventoryPojo.setProductId(productId);
        inventoryService.increaseInventory(inventoryPojo);

        InventoryData inventoryData = inventoryDto.get(productId);
        assertEquals("barcode0" ,inventoryData.getBarcode());
        assertEquals(100, (int)inventoryData.getQuantity());
    }


    //Test get inventory of all items
    @Test
    public void testGetAll() throws ApiException {
        List<InventoryForm> inventoryFormList = TestUtil.getInventoryFormList();
        for(int i=0; i<5 ; i++){
            InventoryPojo inventoryPojo = ConversionUtil.convert(inventoryFormList.get(i), InventoryPojo.class);
            inventoryPojo.setProductId(productIdList.get(i));
            inventoryService.increaseInventory(inventoryPojo);
        }

        List<InventoryData> inventoryData = inventoryDto.getAll();
        int size = inventoryData.size();
        for(int i=0; i<size ; i++){
            assertEquals(100 + i*10, inventoryData.get(size-1-i).getQuantity(), 0);
            assertEquals("barcode"+i, inventoryData.get(size-1-i).getBarcode());
        }
    }

    //Test update inventory
    @Test
    public void testUpdate() throws ApiException {
        InventoryForm inventoryForm = TestUtil.getInventoryForm("barcode0", 120);
        int productId = productIdList.get(0);
        inventoryDto.update(productId, inventoryForm);

        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        assertEquals(productId,(int)inventoryPojo.getProductId());
        assertEquals(120,inventoryPojo.getQuantity(), 0);
    }

    //Test inventory upload
    @Test
    public void testUpload() throws ApiException {
        List<InventoryForm> inventoryFormList = TestUtil.getInventoryFormList();
        inventoryDto.upload(inventoryFormList);

        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        int size = inventoryPojoList.size();
        for(int i=0; i<size ; i++){
            assertEquals(productIdList.get(i), inventoryPojoList.get(size-1-i).getProductId());
            assertEquals(100 + i*10, (int)inventoryPojoList.get(size-1-i).getQuantity());
        }
    }

    //Test inventory upload with invalid barcode
    @Test
    public void testInvalidBarcodeUpload(){
        List<InventoryForm> inventoryFormList = TestUtil.getInventoryFormList();
        inventoryFormList.get(0).setBarcode("invalidBarcode");
        try {
            inventoryDto.upload(inventoryFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("Barcode ( invalidbarcode ) does not exists!", e.getErrorList().get(0).get("error"));
        }

    }

}
