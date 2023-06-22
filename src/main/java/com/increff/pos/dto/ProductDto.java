package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public void add(ProductForm productForm) throws ApiException {
        if(checkBrandCategory(productForm) == -1){
            throw new ApiException("Entered brand or category does not exist");
        }
        ProductPojo productPojo = convert(productForm);
        productService.add(productPojo);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        return convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            list2.add(convert(productPojo));
        }
        return list2;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        if(checkBrandCategory(productForm) == -1){
            throw new ApiException("Entered brand or category does not exist");
        }
        ProductPojo productPojo= convert(productForm);
        productService.update(id, productPojo);
    }

    private BrandPojo getBrandCategory(ProductPojo productPojo) throws ApiException{
        return brandService.get(productPojo.getProductBrandCategory());
    }

    private ProductData convert(ProductPojo productPojo) throws ApiException {
        ProductData productData = new ProductData();
        productData.setProductName(productPojo.getProductName());
        productData.setProductBarcode(productPojo.getProductBarcode());
        productData.setProductId(productPojo.getProductId());
        productData.setProductMrp(productPojo.getProductMrp());
        BrandPojo brandPojo;
        brandPojo = getBrandCategory(productPojo);
        if(brandPojo == null)
            throw new ApiException("brand pojo is null");
        productData.setProductBrand(brandPojo.getBrandName());
        productData.setProductCategory(brandPojo.getCategory());
        return productData;
    }

    private int checkBrandCategory(ProductForm productForm){
        return brandService.getBrandCategory(productForm.getProductBrand(), productForm.getProductCategory());
    }

    private ProductPojo convert(ProductForm productForm){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setProductBarcode(productForm.getProductBarcode());
        productPojo.setProductName(productForm.getProductName());
        productPojo.setProductMrp(productForm.getProductMrp());
        productPojo.setProductBrandCategory(checkBrandCategory(productForm));
        return productPojo;
    }
}
