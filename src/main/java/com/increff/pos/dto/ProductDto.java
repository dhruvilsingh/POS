package com.increff.pos.dto;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void add(ProductForm productForm) throws ApiException {
        NormalizeUtil.normalize(productForm);
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandService.getBrandCategory(productForm.getBrand(), productForm.getCategory());
        ProductPojo productPojo = ConversionUtil.convert(productForm, ProductPojo.class);
        productPojo.setBrandCategoryId(brandPojo.getId());
        productService.add(productPojo);
        inventoryService.addEmptyInventory(productPojo.getId());
    }

    public void upload(List<ProductForm> fileData) throws ApiException {
        List<Map<String, String>> errorList = ValidationUtil.normalizeValidateFormList(fileData);
        checkDuplicateForms(fileData,errorList);
        if (!errorList.isEmpty()){
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        addList(fileData);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addList(List<ProductForm> productFormList) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();
        validateListBarcodes(productFormList, errorList);
        Map<String,Integer> brandPojoMap = validateListBrandCategory(productFormList, errorList);
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (int i = 0; i < productFormList.size(); i++) {
            ProductForm productForm = productFormList.get(i);
            String brandString = productForm.getBrand() + "_" + productForm.getCategory();
            ProductPojo productPojo = ConversionUtil.convert(productForm, ProductPojo.class);
            productPojo.setBrandCategoryId(brandPojoMap.get(brandString));
            productPojoList.add(productPojo);
        }
        List<Integer> idList = productService.addList(productPojoList);
        inventoryService.addEmptyInventoryList(idList);
    }

    private void validateListBarcodes(List<ProductForm> productFormList, List<Map<String, String>> errorList) {
        List<String> barcodeList = new ArrayList<>();
        for (int i = 0; i < productFormList.size(); i++) {
            ProductForm productForm = productFormList.get(i);
            barcodeList.add(productForm.getBarcode());
        }
        productService.validateListBarcodes(barcodeList, errorList, 1);
    }

    private Map<String,Integer> validateListBrandCategory(List<ProductForm> productFormList, List<Map<String, String>> errorList){
        List<BrandPojo> brandPojoList = new ArrayList<>();
        for (int i = 0; i < productFormList.size(); i++) {
            ProductForm productForm = productFormList.get(i);
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand(productForm.getBrand());
            brandPojo.setCategory(productForm.getCategory());
            brandPojoList.add(brandPojo);
        }
        return brandService.validateBrandPojoList(brandPojoList, errorList,2);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.getCheck(id);
        BrandPojo brandPojo = brandService.getCheck(productPojo.getBrandCategoryId());
        ProductData productData = ConversionUtil.convert(productPojo, ProductData.class);
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        return productData;
    }

    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            BrandPojo brandPojo = brandService.getCheck(productPojo.getBrandCategoryId());
            ProductData productData = ConversionUtil.convert(productPojo, ProductData.class);
            productData.setBrand(brandPojo.getBrand());
            productData.setCategory(brandPojo.getCategory());
            list2.add(productData);
        }
        return list2;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        NormalizeUtil.normalize(productForm);
        ValidationUtil.checkValid(productForm);
        productService.update(id, productForm.getName(), productForm.getMrp());
    }

    private void checkDuplicateForms(List<ProductForm> productFormList, List<Map<String, String>> errorList) {
        Set<String> productFormSet = new HashSet<>();
        for (int i=0; i<productFormList.size(); i++) {
            ProductForm productForm = productFormList.get(i);
            String productFormString = productForm.getBarcode() + "_" + productForm.getBrand() + "_" + productForm.getCategory()
                        + "_" + productForm.getName() + "_" + productForm.getMrp();
            if (!productFormSet.add(productFormString)) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", "Duplicate entry!");
                errorList.add(row);
            }
        }
    }

}

//todo: refactor invoice app and other code
//todo: remove unnecessary transactional
//todo: complete unit testing
//todo: put scheduler in job package
//todo: read about criteria builder
//todo: read about indexing