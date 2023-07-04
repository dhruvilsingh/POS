package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        inventoryDao.insert(inventoryPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(int productId) throws ApiException {
        return getCheck(productId);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional
    public List<InventoryReportData> getReport() {
        return inventoryDao.selectReport();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int productId, InventoryPojo inventoryPojo ) throws ApiException {
        InventoryPojo exInventoryPojo = getCheck(productId);
        exInventoryPojo.setProductId(inventoryPojo.getProductId());
        exInventoryPojo.setProductQuantity(inventoryPojo.getProductQuantity());
        inventoryDao.update(exInventoryPojo);
    }

    @Transactional
    public InventoryPojo getCheck(int productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (inventoryPojo == null) {
            throw new ApiException("Inventory for product with given ID does not exit, id: " + productId);
        }
        return inventoryPojo;
    }
}
