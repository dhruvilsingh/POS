package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.*;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        orderItemDao.insert(orderItemPojo);
    }

    @Transactional
    public List<OrderItemPojo> getAll(int orderId) {
        return orderItemDao.selectAll(orderId);
    }

    @Transactional
    public OrderItemPojo get(int id) throws ApiException {
        return orderItemDao.selectId(id);
    }

    @Transactional
    public void delete(int id) {
        orderItemDao.deleteId(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int itemNo, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo exOrderItemPojo = getCheck(itemNo);
        exOrderItemPojo.setOrderItemId(orderItemPojo.getOrderItemId());
        exOrderItemPojo.setOrderId(orderItemPojo.getOrderId());
        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        exOrderItemPojo.setProductQuantity(orderItemPojo.getProductQuantity());
        orderItemDao.update(exOrderItemPojo);
    }


    @Transactional
    public OrderItemPojo getCheck(int itemNo) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectId(itemNo);
        if (orderItemPojo == null) {
            throw new ApiException("Item with given ID does not exit, id: " + itemNo);
        }
        return orderItemPojo;
    }
}
