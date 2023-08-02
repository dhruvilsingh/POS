package com.increff.pos.dto;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        normalize(productForm);
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandService.getBrandCategory(productForm.getBrand(), productForm.getCategory());
        ProductPojo productPojo = convert(productForm, brandPojo.getId());
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void upload(List<ProductForm> fileData) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();
        for(ProductForm productForm : fileData){
            try {
                add(productForm);
            } catch (ApiException e) {
                Map<String,String> row = new HashMap<>();
                row.put("Barcode", productForm.getBarcode());
                row.put("Brand", productForm.getBrand());
                row.put("Category", productForm.getCategory());
                row.put("Name", productForm.getName());
                row.put("MRP", productForm.getMrp().toString());
                row.put("Error", e.getMessage());
                errorList.add(row);
            }
        }
        if (!errorList.isEmpty()) {
            throw new ApiException("One or more errors occurred while processing the data!\nDownload error list to view errors", errorList);
        }
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandPojo brandPojo = brandService.get(productPojo.getBrandCategoryId());
        return convert(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategoryId());
            list2.add(convert(productPojo, brandPojo.getBrand(), brandPojo.getCategory()));
        }
        return list2;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        normalize(productForm);
        ValidationUtil.checkValid(productForm);
        productService.update(id, productForm.getName(), productForm.getMrp());
    }

    //Convert functions

    private static ProductData convert(ProductPojo productPojo, String brand, String category){
        ProductData productData = new ProductData();
        productData.setName(productPojo.getName());
        productData.setBarcode(productPojo.getBarcode());
        productData.setId(productPojo.getId());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(brand);
        productData.setCategory(category);
        return productData;
    }

    private static ProductPojo convert(ProductForm productForm, int brandId){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandCategoryId(brandId);
        return productPojo;
    }

    private static void normalize(ProductForm productForm){
        productForm.setBarcode(StringUtil.toLowerCase(productForm.getBarcode()));
        productForm.setBrand(StringUtil.toLowerCase(productForm.getBrand()));
        productForm.setCategory(StringUtil.toLowerCase(productForm.getCategory()));
        productForm.setName(StringUtil.toLowerCase(productForm.getName()));
        productForm.setMrp(StringUtil.roundOff(productForm.getMrp()));
    }
}
