package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = brandService.get(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo brandPojo : list) {
            list2.add(convert(brandPojo));
        }
        return list2;
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        brandService.update(id, brandPojo);
    }


    private static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setBrandName(brandPojo.getBrandName());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setBrandId(brandPojo.getBrandId());
        return brandData;
    }

    private static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setCategory(brandForm.getCategory());
        brandPojo.setBrandName(brandForm.getBrandName());
        return brandPojo;
    }

}
