package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        orderItemDao.insert(orderItemPojo);
    }

    public List<OrderItemPojo> getAll(int orderId) {
        return orderItemDao.selectAll(orderId);
    }

    public OrderItemPojo getOrderItem(int productId, int orderId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectOrderItem(productId, orderId);
        return orderItemPojo;
    }

    public void delete(int id) throws ApiException {
        getCheck(id);
        if(getAll(getCheck(id).getOrderId()).size() == 1){
            throw new ApiException("Order cannot be empty!");
        }
        orderItemDao.deleteId(id);
    }

    public void update(int id, double sellingPrice, int quantity) throws ApiException {
        OrderItemPojo exOrderItemPojo = getCheck(id);
        exOrderItemPojo.setSellingPrice(sellingPrice);
        exOrderItemPojo.setQuantity(quantity);
    }

    public int getItemCount(ZonedDateTime dateTime){
        ZonedDateTime dateTimeStartOfDay = dateTime.toLocalDate().atStartOfDay(dateTime.getZone());
        ZonedDateTime dateTimeStartOfNextDay = dateTime.toLocalDate().plusDays(1).atStartOfDay(dateTime.getZone());
        return orderItemDao.selectItemCount(dateTimeStartOfDay, dateTimeStartOfNextDay);
    }

    public double getRevenue(ZonedDateTime dateTime){
        ZonedDateTime dateTimeStartOfDay = dateTime.toLocalDate().atStartOfDay(dateTime.getZone());
        ZonedDateTime dateTimeStartOfNextDay = dateTime.toLocalDate().plusDays(1).atStartOfDay(dateTime.getZone());
        return orderItemDao.selectRevenue(dateTimeStartOfDay, dateTimeStartOfNextDay);
    }

    public OrderItemPojo getCheck(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectId(id);
        if (orderItemPojo == null) {
            throw new ApiException("Item with given ID does not exist!");
        }
        return orderItemPojo;
    }

}
