package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void add(ProductPojo productPojo) throws ApiException {
        if(productDao.select(productPojo.getBarcode()) != null){
            throw new ApiException("Barcode ( "+productPojo.getBarcode()+" ) already exists!") ;
        }
        productDao.insert(productPojo);
    }

    public ProductPojo get(int productId) throws ApiException {
        return getCheck(productId);
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    public void update(int productId, String name, Double mrp) throws ApiException {
        ProductPojo exProductPojo = getCheck(productId); //TODO: change variable name ex to existing
        exProductPojo.setName(name);
        exProductPojo.setMrp(mrp);
    }

    public ProductPojo getCheck(int productId) throws ApiException {
        ProductPojo productPojo = productDao.select(productId);
        if (productPojo == null) {
            throw new ApiException("Product with given ID does not exist");
        }
        return productPojo;
    }

    public ProductPojo getCheckBarcode(String productBarcode) throws ApiException {
        ProductPojo productPojo = productDao.select(productBarcode);
        if(productPojo != null){
            return productPojo;
        }
        throw new ApiException("Invalid Barcode ( "+productBarcode+" )!");
    }

}
