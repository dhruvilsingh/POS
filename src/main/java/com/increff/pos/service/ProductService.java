package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
        if(StringUtil.isEmpty(productPojo.getProductName()) || StringUtil.isEmpty(productPojo.getProductBarcode())) {
            throw new ApiException("Fields cannot be empty");
        }
        if(getId(productPojo.getProductBarcode())!=-1){
            throw new ApiException("Barcode already exists") ;
        }
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getProductId());
        inventoryPojo.setProductQuantity(0);
        inventoryDao.insert(inventoryPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int productId) throws ApiException {
        return getCheck(productId);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int productId, ProductPojo productPojo ) throws ApiException {
        normalize(productPojo);
        ProductPojo exProductPojo = getCheck(productId);
        exProductPojo.setProductName(productPojo.getProductName());
        exProductPojo.setProductBarcode(productPojo.getProductBarcode());
        exProductPojo.setProductBrandCategory(productPojo.getProductBrandCategory());
        exProductPojo.setProductMrp(productPojo.getProductMrp());
        productDao.update(exProductPojo);
    }

    @Transactional
    public ProductPojo getCheck(int productId) throws ApiException {
        ProductPojo productPojo = productDao.select(productId);
        if (productPojo == null) {
            throw new ApiException("Product with given ID does not exit, id: " + productId);
        }
        return productPojo;
    }

    public int getId(String productBarcode){
        ProductPojo productPojo = productDao.select(productBarcode);
        if(productPojo == null)
            return -1;
        return productPojo.getProductId();
    }

    protected static void normalize(ProductPojo productPojo) {
        productPojo.setProductName(productPojo.getProductName().trim());
        productPojo.setProductBarcode(productPojo.getProductBarcode().trim());
        productPojo.setProductName(StringUtil.toLowerCase(productPojo.getProductName()));
    }

}
