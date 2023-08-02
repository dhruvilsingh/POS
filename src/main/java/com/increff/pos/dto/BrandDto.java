package com.increff.pos.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        normalize(brandForm);
        ValidationUtil.checkValid(brandForm);
        BrandPojo brandPojo = convert(brandForm);
        brandService.add(brandPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void upload(List<BrandForm> fileData) throws ApiException{
        List<Map<String,String>> errorList = new ArrayList<>();
        for(BrandForm brandForm : fileData){
            try {
                add(brandForm);
            } catch (Exception e) {
                Map<String,String> row = new HashMap<>();
                row.put("Brand", brandForm.getBrand());
                row.put("Category", brandForm.getCategory());
                row.put("Error", e.getMessage());
                errorList.add(row);
            }
        }
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
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
        normalize(brandForm);
        ValidationUtil.checkValid(brandForm);
        BrandPojo brandPojo = convert(brandForm);
        brandService.update(id, brandPojo);
    }


    //convert functions

    private static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    private static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setCategory(brandForm.getCategory());
        brandPojo.setBrand(brandForm.getBrand());
        return brandPojo;
    }

    protected static void normalize(BrandForm brandForm) {
        brandForm.setBrand(StringUtil.toLowerCase(brandForm.getBrand()));
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory().trim().toLowerCase()));
    }

}
