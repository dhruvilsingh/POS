package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class BrandDtoTest extends AbstractUnitTest{

	@Autowired
	private BrandDto brandDto;

	//TODO : to create a separate class for creating master data.
	@Test
	public void testBrandAdd() throws ApiException {
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand(" Brand1 ");
		brandForm.setCategory("cAt1   ");
		brandDto.add(brandForm);
		assertEquals("brand1",brandDto.getAll().get(0).getBrand());
		assertEquals("cat1",brandDto.getAll().get(0).getCategory());
	}

	@Test
	public void addExistingBrandCategory(){ //TODO to add @test annotation for all tests.
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand(" Brand1 ");
		brandForm.setCategory("cAt1   ");
		try{
			brandDto.add(brandForm); //TODO : to use dao layer to create master data.
			brandDto.add(brandForm); //TODO : use fail for all api exceptions tests.
			fail();
		}catch(ApiException exception){
			assertEquals("Brand and category pair already exist!", exception.getMessage());
		}
	}

	@Test
	public void testEmptyBrand(){
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand("");
		brandForm.setCategory("cat1");
		try{
			brandDto.add(brandForm);
		}catch (ApiException exception){
			assertEquals("Brand name cannot be empty\n", exception.getMessage());
		}
	}

	@Test
	public void testEmptyBrandCategory(){
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand("brand1");
		brandForm.setCategory("");
		try{
			brandDto.add(brandForm);
		}catch (ApiException exception){
			assertEquals("Category cannot be empty\n", exception.getMessage());
		}
	}



	@Test
	public void testGetById() throws ApiException {
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand(" Brand1 ");
		brandForm.setCategory("cAt1   ");
		brandDto.add(brandForm);
		int brandId = brandDto.getAll().get(0).getId();
		assertEquals("brand1",brandDto.get(brandId).getBrand());
		assertEquals("cat1",brandDto.get(brandId).getCategory());
	}

	@Test
	public void testGetByInvalidId(){
		try{
			brandDto.get(0);
		}catch(ApiException exception){
			assertEquals("Brand with given ID does not exist!", exception.getMessage());
		}
	}

	@Test
	public void testGetAll() throws ApiException{
		BrandForm brandForm = new BrandForm();
		for(int i=0; i<10 ; i++){
			brandForm.setBrand("BRAND"+i);
			brandForm.setCategory("Cat"+i);
			brandDto.add(brandForm);
		}
		List<BrandData> brandData = brandDto.getAll();
		for(int i=0; i<brandData.size() ; i++){
			assertEquals("brand"+i, brandData.get(brandData.size()-1-i).getBrand());
			assertEquals("cat"+i, brandData.get(brandData.size()-1-i).getCategory());
		}
	}

	@Test
	public void testUpdate() throws ApiException{
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand(" Brand1 ");
		brandForm.setCategory("cAt1   ");
		brandDto.add(brandForm);
		int brandId = brandDto.getAll().get(0).getId();
		brandForm.setBrand("Updated Brand");
		brandForm.setCategory("Updated Cat");
		brandDto.update(brandId, brandForm);
		assertEquals("updated brand", brandDto.getAll().get(0).getBrand());
		assertEquals("updated cat", brandDto.getAll().get(0).getCategory());
	}

	@Test
	public void testExistingUpdate(){
		BrandForm brandForm = new BrandForm();
		try {
			for (int i = 0; i < 2; i++) {
				brandForm.setBrand("BRAND" + i);
				brandForm.setCategory("Cat" + i);
				brandDto.add(brandForm);
			}

			int brandId = brandDto.getAll().get(0).getId();

			brandForm.setBrand("Brand0");
			brandForm.setCategory("cat0");
			brandDto.update(brandId, brandForm);
		}catch (ApiException exception){
			assertEquals("Brand and category pair already exist!", exception.getMessage());
		}
	}

//	@Test
//	public void testUpload() throws ApiException {
//		List<Map<String, Object>> brandList = new ArrayList<>();
//
//		for (int i = 0; i < 5; i++) {
//			Map<String, Object> brandMap = new HashMap<>();
//			brandMap.put("brand", "brand" + i);
//			brandMap.put("category", "cat" + i);
//			brandList.add(brandMap);
//		}
//
//		Map<String, Object> brandMap = new HashMap<>();
//		brandMap.put("brand", "brand1");
//		brandMap.put("category", "cat1");
//		brandList.add(brandMap);
//
//		List<Map<String, Object>> errorList = brandDto.upload(brandList);
//		List<BrandData> brandData = brandDto.getAll();
//		int size =brandData.size();
//		assertEquals("Brand and category pair already exist!", errorList.get(0).get("error"));
//
//		for (int i = 0; i < size; i++) {
//			assertEquals("brand" + i, brandData.get(size- 1 - i).getBrand());
//			assertEquals("cat" + i, brandData.get(size- 1- i).getCategory());
//		}
//	}



}
