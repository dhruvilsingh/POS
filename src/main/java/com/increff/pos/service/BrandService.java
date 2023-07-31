package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    public void add(BrandPojo brandPojo) throws ApiException {
        if(brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory()) != null){
            throw new ApiException(brandPojo.getBrand()+" and "+brandPojo.getCategory()+" pair already exist!");
        }
        brandDao.insert(brandPojo);
    }

     //TODO : to remove the get method.
    public BrandPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public void update(int id, BrandPojo brandPojo) throws ApiException { //TODO: to send only the updatable fields in the function
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
            throw new ApiException("Brand with given ID does not exist!"); //TODO : to mention ID in error msg.
        }
        return brandPojo;
    }



}
