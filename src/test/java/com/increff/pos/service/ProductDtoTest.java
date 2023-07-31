package com.increff.pos.service;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;

    @Before
    public void addTestBrand() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(" Brand1 ");
        brandForm.setCategory("cAt1   ");
        brandDto.add(brandForm);
    }

    @Test
    public void testAddProduct() throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(" BarCode1     "); //TODO to create a private function for getting forms.
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        productForm.setName("  ProducT1   ");
        productForm.setMrp(10000.75465764);
        productDto.add(productForm);

        int productId = productDto.getAll().get(0).getId();

        assertEquals("barcode1",productDto.get(productId).getBarcode());
        assertEquals("brand1",productDto.get(productId).getBrand());
        assertEquals("cat1",productDto.get(productId).getCategory());
        assertEquals("product1",productDto.get(productId).getName());
        assertEquals(10000.75,productDto.get(productId).getMrp(), 0);
    }

    @Test
    public void testAddExistingProduct(){
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(" BarCode1     ");
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        productForm.setName("  ProducT1   ");
        productForm.setMrp(10000.75465764);
        try{
            productDto.add(productForm);
            productDto.add(productForm);
        }catch (ApiException exception){
            assertEquals("Barcode already exists!", exception.getMessage());
        }
    }

    @Test
    public void testAddInvalidProduct(){
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(" BarCode1     ");
        productForm.setBrand("invalidBrand");
        productForm.setCategory("invalidBrand");
        productForm.setName("  ProducT1   ");
        productForm.setMrp(10000.75465764);
        try{
            productDto.add(productForm);
        }catch (ApiException exception){
            assertEquals("Entered brand category pair does not exist!", exception.getMessage());
        }
    }

    @Test
    public void testGetByInvalidId(){
        try{
            productDto.get(0);
        }catch(ApiException exception){
            assertEquals("Product with given ID does not exist", exception.getMessage());
        }
    }

    @Test
    public void testGetAll() throws ApiException{
        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        for(int i=0; i<10 ; i++){
            productForm.setName("Product"+i);
            productForm.setBarcode("Barcode"+i);
            productForm.setMrp(Double.valueOf(100 + i*10));
            productDto.add(productForm);
        }
        List<ProductData> productData = productDto.getAll();
        int size = productData.size();
        for(int i=0; i<size ; i++){
            assertEquals("product"+i, productData.get(size-1-i).getName());
            assertEquals(100 + i*10, productData.get(size-1-i).getMrp(), 0);
            assertEquals("barcode"+i, productData.get(size-1-i).getBarcode());
            assertEquals("brand1", productData.get(size-1-i).getBrand());
            assertEquals("cat1", productData.get(size-1-i).getCategory());
        }
    }

    @Test
    public void testUpdate() throws ApiException{
        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandDto.getAll().get(0).getBrand());
        productForm.setCategory(brandDto.getAll().get(0).getCategory());
        productForm.setName("Product1");
        productForm.setBarcode("Barcode1");
        productForm.setMrp(Double.valueOf(100));
        productDto.add(productForm);

        int productId = productDto.getAll().get(0).getId();

        productForm.setBarcode("updatedBarcode");
        productForm.setBrand("brand1");
        productForm.setCategory("cat1");
        productForm.setName("updatedProduct");
        productForm.setMrp(Double.valueOf(1000));
        productDto.update(productId,productForm);

        assertEquals("updatedbarcode",productDto.get(productId).getBarcode());
        assertEquals("brand1",productDto.get(productId).getBrand());
        assertEquals("cat1",productDto.get(productId).getCategory());
        assertEquals("updatedproduct",productDto.get(productId).getName());
        assertEquals(1000,productDto.get(productId).getMrp(), 0);
    }

    @Test
    public void testExistingUpdate(){
        ProductForm productForm = new ProductForm();
        try{
            productForm.setBrand(brandDto.getAll().get(0).getBrand());
            productForm.setCategory(brandDto.getAll().get(0).getCategory());
            for(int i=0; i<2 ; i++){
                productForm.setName("Product"+i);
                productForm.setBarcode("Barcode"+i);
                productForm.setMrp(Double.valueOf(100 + i*10));
                productDto.add(productForm);
            }

            int productId = productDto.getAll().get(0).getId();

            productForm.setBarcode("barcode0");
            productDto.update(productId,productForm);
        }catch(ApiException exception){
            assertEquals("Barcode already exists!", exception.getMessage());
        }
    }

//    @Test
//    public void testUpload() throws ApiException {
//        List<Map<String,Object>> productlist = new ArrayList<>();
//        for(int i = 0; i < 5; i++) {
//            Map<String, Object> productMap = new HashMap<>();
//            productMap.put("productName","product"+i);
//            productMap.put("productBarcode","barcode"+i);
//            productMap.put("productBrand",brandDto.getAll().get(0).getBrand());
//            productMap.put("productCategory", brandDto.getAll().get(0).getCategory());
//            productMap.put("productMrp", 100);
//            productlist.add(productMap);
//        }
//        Map<String, Object> productMap = new HashMap<>();
//        productMap.put("productName","product");
//        productMap.put("productBarcode", "barcode1");
//        productMap.put("productBrand",brandDto.getAll().get(0).getBrand());
//        productMap.put("productCategory", brandDto.getAll().get(0).getCategory());
//        productMap.put("productMrp", 100);
//        productlist.add(productMap);
//
//        List<Map<String,Object>> errorList = productDto.upload(productlist);
//        assertEquals("Barcode already exists!",errorList.get(0).get("error"));
//
//        List<ProductData> productData = productDto.getAll();
//        int size = productData.size();
//        for(int i=0; i<size; i++){
//            assertEquals("product"+i, productData.get(size-1-i).getName());
//            assertEquals(100, productData.get(size-1-i).getMrp(), 0);
//            assertEquals("barcode"+i, productData.get(size-1-i).getBarcode());
//            assertEquals("brand1", productData.get(size-1-i).getBrand());
//            assertEquals("cat1", productData.get(size-1-i).getCategory());
//        }
//    }
}
