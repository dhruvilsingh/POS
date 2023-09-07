package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;

import com.increff.pos.service.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    public void add(BrandPojo brandPojo) throws ApiException {
        getCheckExistingPojo(brandPojo);
        brandDao.insert(brandPojo);
    }

    public void addList(List<BrandPojo> brandPojoList) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();
        validateBrandPojoList(brandPojoList, errorList,1);
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        for(BrandPojo brandPojo: brandPojoList){
                brandDao.insert(brandPojo);
        }
    }

    public Map<String,Integer> validateBrandPojoList(List<BrandPojo> brandPojoList, List<Map<String, String>> errorList, int type){
        Map<String,Integer> brandPojoMap = new HashMap<>();
        List<BrandPojo> brandPojos = getAll();
        for(BrandPojo brandPojo : brandPojos){
            String brandPojoString = brandPojo.getBrand() + "_" + brandPojo.getCategory();
            brandPojoMap.put(brandPojoString, brandPojo.getId());
        }
        for (int i=0; i<brandPojoList.size(); i++) {
            BrandPojo brandPojo = brandPojoList.get(i);
            String brandPojoString = brandPojo.getBrand() + "_" + brandPojo.getCategory();
            if (type==2 && brandPojoMap.get(brandPojoString) == null) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", brandPojo.getBrand() + " and " + brandPojo.getCategory() + " pair does not exist!");
                errorList.add(row);
            }
            else if (type==1 && brandPojoMap.get(brandPojoString) != null) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", brandPojo.getBrand() + " and " + brandPojo.getCategory() + " pair already exist!");
                errorList.add(row);
            }
        }
        return brandPojoMap;
    }

    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public void update(int id, BrandPojo brandPojo) throws ApiException {
        BrandPojo exBrandPojo = getCheck(id);
        BrandPojo brandPojo1 = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if( brandPojo1 != null && brandPojo1.getId() != id) {
            throw new ApiException(brandPojo.getBrand()+" and "+brandPojo.getCategory()+" pair already exist!");
        }
        exBrandPojo.setBrand(brandPojo.getBrand());
        exBrandPojo.setCategory(brandPojo.getCategory());
    }

    public BrandPojo getBrandCategory(String brand, String category) throws ApiException{
        BrandPojo brandPojo = brandDao.selectBrandCategory(brand,category);
        if(brandPojo == null){
            throw new ApiException(brand +" and "+category+" pair does not exist!");
        }
        return brandPojo;
    }

    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if (brandPojo == null) {
            throw new ApiException("Brand with given ID does not exist!");
        }
        return brandPojo;
    }

    private void getCheckExistingPojo(BrandPojo brandPojo) throws ApiException {
        BrandPojo existingbrandPojo = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if(existingbrandPojo != null) {
            throw new ApiException(brandPojo.getBrand()+" and "+brandPojo.getCategory()+" pair already exist!");
        }
    }

}
