package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

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

    public List<Integer> addList(List<ProductPojo> productPojoList){
        List<Integer> idList = new ArrayList<>();
        for(ProductPojo productPojo: productPojoList){
            productDao.insert(productPojo);
            idList.add(productPojo.getId());
        }
        return idList;
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    public void update(int productId, String name, Double mrp) throws ApiException {
        ProductPojo exProductPojo = getCheck(productId);
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

    public Map<String,Integer> validateListBarcodes(List<String> barcodeList, List<Map<String, String>> errorList, int type){
        List<ProductPojo> productPojos = getAll();
        Map<String,Integer> barcodeMap = new HashMap<>();
        for (ProductPojo productPojo : productPojos) {
            barcodeMap.put(productPojo.getBarcode(), productPojo.getId());
        }
        for (int i = 0; i < barcodeList.size(); i++) {
            String barcode = barcodeList.get(i);
            if (type==2 && barcodeMap.get(barcode) == null) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", "Barcode ( " + barcode + " ) does not exists!");
                errorList.add(row);
            }
            else if(type==1 && barcodeMap.get(barcode) != null){
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", "Barcode ( " + barcode + " ) already exists!");
                errorList.add(row);
            }
        }
        return barcodeMap;
    }

}
