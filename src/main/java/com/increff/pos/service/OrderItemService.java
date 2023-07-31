package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.OrderItemPojo;
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

    public OrderItemPojo get(int itemId) throws ApiException {//todo: remove get or getcheck
        return getCheck(itemId);
    }

    public OrderItemPojo getCheckOrderItem(int productId, int orderId) throws ApiException {//todo: remove get or getcheck
        OrderItemPojo orderItemPojo = orderItemDao.selectOrderItem(productId, orderId);
        if (orderItemPojo == null) {
            throw new ApiException("Item with given ID does not exist!");
        }
        return orderItemPojo;
    }

    public void delete(int id) throws ApiException {
        getCheck(id);
        if(getAll(get(id).getOrderId()).size() == 1){
            throw new ApiException("Order cannot be empty!");
        }
        orderItemDao.deleteId(id);
    }

    public void update(int itemId, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo exOrderItemPojo = getCheck(itemId);
        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        exOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
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

    public OrderItemPojo getCheck(int itemNo) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectId(itemNo);
        if (orderItemPojo == null) {
            throw new ApiException("Item with given ID does not exist!");
        }
        return orderItemPojo;
    }

}
