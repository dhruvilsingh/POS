package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    public void add(InventoryPojo inventoryPojo) throws ApiException {
        inventoryDao.insert(inventoryPojo);
    }

    public InventoryPojo get(int productId) throws ApiException {
        return getCheck(productId);
    }

    public void addInventory(InventoryPojo inventoryPojo) throws ApiException {
        int presentQuantity = get(inventoryPojo.getProductId()).getQuantity();
        int updatedQuantity = presentQuantity + inventoryPojo.getQuantity();
        if(updatedQuantity<0){
            throw new ApiException("Value exceeded " + Integer.MAX_VALUE);
        }

        inventoryPojo.setQuantity(updatedQuantity);
        update(inventoryPojo.getProductId(), inventoryPojo);
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    public void update(int productId, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo exInventoryPojo = getCheck(productId);
        if(inventoryPojo.getQuantity()< 0){
            throw new ApiException("Value exceeded " + Integer.MAX_VALUE);
        }
        exInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

    public void updateInventory(int productId, int oldQuantity, int newQuantity) throws ApiException {
        int updatedInventory;
        int availableInventory = getCheck(productId).getQuantity();
        if(oldQuantity == newQuantity){
            updatedInventory = oldQuantity;
        }
        else if(newQuantity < oldQuantity){
            updatedInventory = availableInventory + oldQuantity - newQuantity;
        }
        else if((newQuantity - oldQuantity) <= availableInventory) {
            updatedInventory = availableInventory - newQuantity + oldQuantity;
        }
        else
            throw new ApiException((oldQuantity+availableInventory)+ " items left in inventory!");
        InventoryPojo inventoryPojo = get(productId);
        if(inventoryPojo.getQuantity()< 0){
            throw new ApiException("Value exceeded " + Integer.MAX_VALUE);
        }
        inventoryPojo.setQuantity(updatedInventory);
    }

    public void checkInputQuantity(int productId, int inputQuantity, String barcode) throws ApiException {
        int availableInventory = getCheck(productId).getQuantity();
        if(inputQuantity > availableInventory){
            throw new ApiException(availableInventory + " items are left for " + barcode);
        }
        int updatedInventory = availableInventory - inputQuantity;
        InventoryPojo inventoryPojo = get(productId);
        inventoryPojo.setQuantity(updatedInventory);
    }

    public InventoryPojo getCheck(int productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (inventoryPojo == null) {
            throw new ApiException("Product with given ID does not exist!");
        }
        return inventoryPojo;
    }
}
