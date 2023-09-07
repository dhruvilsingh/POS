package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.TestUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class BrandDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDao brandDao;

    @Autowired
    private BrandDto brandDto;

    // Tests if normalization works properly by converting to lower case and remove trailing spaces
    @Test
	public void testBrandAdd() throws ApiException {
        String brand = "    brAnD1  ", category = " CategOry1   ";
        BrandForm brandForm = TestUtil.getBrandForm(brand, category);
		brandDto.add(brandForm);
        BrandPojo brandPojo = brandDao.selectAll().get(0);
        assertEquals(brandPojo.getBrand(), "brand1");
        assertEquals(brandPojo.getCategory(), "category1");
	}

    // Tests empty validation on brand and category
    @Test
	public void testEmptyBrandCategoryAdd(){
        String brand = "      ", category = "    ";
        BrandForm brandForm = TestUtil.getBrandForm(brand, category);
		try{
			brandDto.add(brandForm);
            fail();
		}catch (ApiException exception){
            String errorMessage = exception.getMessage();
            assertTrue(errorMessage.contains("Category cannot be empty"));
            assertTrue(errorMessage.contains("Brand name cannot be empty"));
		}
	}

    // Tests invalid characters validation on brand and category
    @Test
    public void testInvalidBrandCategoryAdd(){
        String brand = "  brand~!    ", category = " cat\uD83D   ";
        BrandForm brandForm = TestUtil.getBrandForm(brand, category);
        try{
            brandDto.add(brandForm);
            fail();
        }catch (ApiException exception){
            assertEquals("Invalid characters in category", exception.getMessage());
        }
    }

    //Tests get by id
	@Test
	public void testGetById() throws ApiException {
        String brand = "brand1", category = "category1";
        BrandForm brandForm = TestUtil.getBrandForm(brand, category);
        brandDao.insert(ConversionUtil.convert(brandForm, BrandPojo.class));
        int id = brandDao.selectAll().get(0).getId();
        BrandData brandData = brandDto.get(id);
        assertEquals("brand1", brandData.getBrand());
        assertEquals("category1", brandData.getCategory());
	}

    //Test get all
	@Test
	public void testGetAll() throws ApiException{
		for(int i=0; i<10 ; i++){
            BrandForm brandForm = TestUtil.getBrandForm("brand"+i, "cat"+i);
			brandDao.insert(ConversionUtil.convert(brandForm, BrandPojo.class));
		}
		List<BrandData> brandDataList = brandDto.getAll();
        int size = brandDataList.size();
		for(int i=0; i<size ; i++){
			assertEquals("brand"+i, brandDataList.get(size-1-i).getBrand());
			assertEquals("cat"+i, brandDataList.get(size-1-i).getCategory());
		}
	}

    //Test update of brand and category
	@Test
	public void testUpdate() throws ApiException{
		BrandForm brandForm = TestUtil.getBrandForm("brand", "category");
		brandDao.insert(ConversionUtil.convert(brandForm, BrandPojo.class));
		int brandId = brandDao.selectAll().get(0).getId();
        brandForm = TestUtil.getBrandForm("Updated Brand  ", "  Updated Cat");
		brandDto.update(brandId, brandForm);
        BrandPojo brandPojo = brandDao.selectAll().get(0);
		assertEquals("updated brand", brandPojo.getBrand());
		assertEquals("updated cat", brandPojo.getCategory());
	}

    //Test upload of duplicate data
	@Test
	public void testInvalidUpload(){
		List<BrandForm> brandFormList = TestUtil.getBrandFormList();

        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand1");
        brandForm.setCategory("category1");
        brandFormList.add(brandForm);
        try {
            brandDto.upload(brandFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("Duplicate entry!", e.getErrorList().get(0).get("error"));
        }
	}

    //Test upload of existing data
    @Test
    public void testExistingUpload(){
        try {
            brandDao.insert(ConversionUtil.convert(TestUtil.getBrandForm("brand1","category1"), BrandPojo.class));
            List<BrandForm> brandFormList = TestUtil.getBrandFormList();
            brandDto.upload(brandFormList);
            fail();
        } catch (ApiException e) {
            assertEquals("One or more errors occurred while processing the data!\nDownload error list to view errors", e.getMessage());
            assertEquals("brand1 and category1 pair already exist!", e.getErrorList().get(0).get("error"));
        }
    }

    //Test upload of valid data
    @Test
    public void testUpload() throws ApiException {
            List<BrandForm> brandFormList = TestUtil.getBrandFormList();
            brandDto.upload(brandFormList);

            List<BrandPojo> brandPojoList = brandDao.selectAll();
            int size = brandPojoList.size();
            for(int i=0; i<size ; i++){
                assertEquals("brand"+i, brandPojoList.get(size-1-i).getBrand());
                assertEquals("category"+i, brandPojoList.get(size-1-i).getCategory());
            }
    }



}
