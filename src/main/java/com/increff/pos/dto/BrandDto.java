package com.increff.pos.dto;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        NormalizeUtil.normalize(brandForm);
        ValidationUtil.checkValid(brandForm);
        brandService.add(ConversionUtil.convert(brandForm, BrandPojo.class));
    }

    @Transactional(rollbackOn = ApiException.class)
    public void upload(List<BrandForm> fileData) throws ApiException {
        List<Map<String, String>> errorList = ValidationUtil.normalizeValidateFormList(fileData);
        checkDuplicateForms(fileData,errorList);
        if (!errorList.isEmpty()){
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        brandService.addList(ConversionUtil.convert(fileData, BrandPojo.class));
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
        return ConversionUtil.convert(brandPojo, BrandData.class);
    }

    public List<BrandData> getAll() throws ApiException {
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo brandPojo : list) {
            list2.add(ConversionUtil.convert(brandPojo, BrandData.class));
        }
        return list2;
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        NormalizeUtil.normalize(brandForm);
        ValidationUtil.checkValid(brandForm);
        BrandPojo brandPojo = ConversionUtil.convert(brandForm, BrandPojo.class);
        brandService.update(id, brandPojo);
    }

    private void checkDuplicateForms(List<BrandForm> brandFormList, List<Map<String, String>> errorList) {
        Set<String> brandFormSet = new HashSet<>();
        for (int i=0; i<brandFormList.size(); i++) {
            BrandForm brandForm = brandFormList.get(i);
            String brandFormString = brandForm.getBrand() + "_" + brandForm.getCategory();
            if (!brandFormSet.add(brandFormString)) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", "Duplicate entry!");
                errorList.add(row);
            }
        }
    }


}
