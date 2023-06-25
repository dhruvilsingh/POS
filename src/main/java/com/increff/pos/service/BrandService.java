package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        if(StringUtil.isEmpty(brandPojo.getBrandName()) || StringUtil.isEmpty(brandPojo.getCategory())) {
            throw new ApiException("Fields cannot be empty");
        }
        if(getBrandCategory(brandPojo.getBrandName(), brandPojo.getCategory()) != -1){
            throw new ApiException("Category already exist in this brand");
        }
        brandDao.insert(brandPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int brandId) throws ApiException {
        return getCheck(brandId);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int brandId, BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        BrandPojo exBrandPojo = getCheck(brandId);
        if(getBrandCategory(brandPojo.getBrandName(), brandPojo.getCategory()) != -1) {
            throw new ApiException("Category already exist in this brand");
        }
        exBrandPojo.setBrandName(brandPojo.getBrandName());
        exBrandPojo.setCategory(brandPojo.getCategory());
        brandDao.update(exBrandPojo);
    }

    @Transactional
    public int getBrandCategory(String brandName, String category){
        return brandDao.selectBrandCategory(brandName,category);
    }

    @Transactional
    public BrandPojo getCheck(int brandId) throws ApiException {
        BrandPojo brandPojo = brandDao.select(brandId);
        if (brandPojo == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + brandId);
        }
        return brandPojo;
    }

    protected static void normalize(BrandPojo brandPojo) {
        brandPojo.setBrandName(brandPojo.getBrandName().trim());
        brandPojo.setCategory(brandPojo.getCategory().trim());
        brandPojo.setBrandName(StringUtil.toLowerCase(brandPojo.getBrandName()));
        brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()));
    }

}
