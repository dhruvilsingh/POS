package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private ProductDao productDao;

    @Before
    public void addTestBrand() throws ApiException {
        brandDao.insert(ConversionUtil.convert(TestUtil.getBrandForm("brand1","category1"), BrandPojo.class));
    }


    // Tests if normalization works properly by converting to lower case and remove trailing spaces
    @Test
    public void testAddProduct() throws ApiException {
        ProductForm productForm = TestUtil.getProductForm(" BarCode1     ","brand1","category1",
                                "ProducT1",1000.7584);

        productDto.add(productForm);
        ProductData productData = productDto.getAll().get(0);
        assertEquals("barcode1",productData.getBarcode());
        assertEquals("brand1",productData.getBrand());
        assertEquals("category1",productData.getCategory());
        assertEquals("product1",productData.getName());
        assertEquals(1000.76,productData.getMrp(), 0);
    }

    //Test invalid brand and category in product
    @Test
    public void testAddInvalidProduct(){
        ProductForm productForm = TestUtil.getProductForm(" BarCode1     ","invalidBrand","invalidCategory",
                                            "ProducT1",1000.7584);
        try{
            productDto.add(productForm);
            fail();
        }catch (ApiException exception){
            assertEquals("invalidbrand and invalidcategory pair does not exist!", exception.getMessage());
        }
    }

    //Test get product by id
    @Test
    public void testGetById() throws ApiException {
        ProductForm productForm = TestUtil.getProductForm("barcode1","brand1","category1",
                "product1",1000.0);

        ProductPojo productPojo  =  ConversionUtil.convert(productForm, ProductPojo.class);
        int brandCategoryId = brandDao.selectAll().get(0).getId();
        productPojo.setBrandCategoryId(brandCategoryId);

        productDao.insert(productPojo);

        int productId = productDao.selectAll().get(0).getId();
        ProductData productData = productDto.get(productId);
        assertEquals("product1", productData.getName());
        assertEquals(1000.0, productData.getMrp(), 0);
        assertEquals("barcode1", productData.getBarcode());
        assertEquals("brand1", productData.getBrand());
        assertEquals("category1", productData.getCategory());

    }

    //Test get all
    @Test
    public void testGetAll() throws ApiException{
        int brandCategoryId = brandDao.selectAll().get(0).getId();
        for(int i=0; i<10 ; i++){
            ProductForm productForm = TestUtil.getProductForm("barcode"+i,"brand1","category1",
                                        "product"+i, (double) (100 + i*10));
            ProductPojo productPojo = ConversionUtil.convert(productForm, ProductPojo.class);
            productPojo.setBrandCategoryId(brandCategoryId);
            productDao.insert(productPojo);
        }
        List<ProductData> productData = productDto.getAll();
        int size = productData.size();
        for(int i=0; i<size ; i++){
            assertEquals("product"+i, productData.get(size-1-i).getName());
            assertEquals(100 + i*10, productData.get(size-1-i).getMrp(), 0);
            assertEquals("barcode"+i, productData.get(size-1-i).getBarcode());
            assertEquals("brand1", productData.get(size-1-i).getBrand());
            assertEquals("category1", productData.get(size-1-i).getCategory());
        }
    }

    //Test update of product name and MRP
    @Test
    public void testUpdate() throws ApiException{
        ProductForm productForm = TestUtil.getProductForm("barcode1","brand1","category1",
                "product1",1000.0);

        int brandCategoryId = brandDao.selectAll().get(0).getId();
        ProductPojo productPojo = ConversionUtil.convert(productForm, ProductPojo.class);
        productPojo.setBrandCategoryId(brandCategoryId);
        productDao.insert(productPojo);

        int productId = productDto.getAll().get(0).getId();
        ProductForm updatedProductForm = TestUtil.getProductForm("barcode1","brand1","category1",
                "updatedName", 100.0);
        productDto.update(productId, updatedProductForm);

        assertEquals("updatedname",productDao.select(productId).getName());
        assertEquals(100.0,productDao.select(productId).getMrp(), 0);
    }

    //Test upload of duplicate data
    @Test
    public void testInvalidUpload() throws ApiException {
        List<ProductForm> productFormList = TestUtil.getProductFormList();
        ProductForm productForm = TestUtil.getProductForm(" BarCode0    ","brand1","category1",
                "Product0",100.0);
        productFormList.add(productForm);

        try {
            productDto.upload(productFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("Duplicate entry!", e.getErrorList().get(0).get("error"));
        }
    }

    //Test upload of product with invalid brand category
    @Test
    public void testInvalidBrandCategoryUpload() throws ApiException {
        List<ProductForm> productFormList = TestUtil.getProductFormList();
        ProductForm productForm = TestUtil.getProductForm(" BarCode0    ","InvalidBrand","InvalidCategory",
                "Product0",100.0);
        productFormList.add(productForm);

        try {
            productDto.upload(productFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("invalidbrand and invalidcategory pair does not exist!", e.getErrorList().get(0).get("error"));
        }
    }

    //Test upload of product with existing barcode
    @Test
    public void testExistingBarcodeUpload() throws ApiException {
        List<ProductForm> productFormList = TestUtil.getProductFormList();
        try {
            productDto.upload(productFormList);
            productFormList.clear();
            productFormList.add(TestUtil.getProductForm("barcode1","brand1","category1","product1",100.0));
            productDto.upload(productFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("Barcode ( barcode1 ) already exists!", e.getErrorList().get(0).get("error"));
        }
    }

    //Test upload of valid data
    @Test
    public void testUpload() throws ApiException {
        List<ProductForm> productFormList = TestUtil.getProductFormList();
        productDto.upload(productFormList);

        int brandCategoryId = brandDao.selectAll().get(0).getId();

        List<ProductPojo> productPojoList = productDao.selectAll();
        int size = productPojoList.size();
        for(int i=0; i<size ; i++){
            assertEquals("product"+i, productPojoList.get(size-1-i).getName());
            assertEquals(100 + i*10, productPojoList.get(size-1-i).getMrp(), 0);
            assertEquals("barcode"+i, productPojoList.get(size-1-i).getBarcode());
            assertEquals(brandCategoryId, (int)productPojoList.get(size-1-i).getBrandCategoryId());
        }
    }
}
