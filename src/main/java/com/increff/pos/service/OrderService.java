package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrdersPojo;
import com.increff.pos.model.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public int add(){
        OrdersPojo ordersPojo = new OrdersPojo();
        ordersPojo.setTime(ZonedDateTime.now());
        ordersPojo.setStatus(OrderStatus.CREATED);
        return orderDao.insert(ordersPojo);
    }

    public OrdersPojo getCheck(int orderId) throws ApiException{
        OrdersPojo ordersPojo = orderDao.select(orderId);
        if (ordersPojo == null) {
            throw new ApiException("Order with given ID does not exists!");
        }
        return ordersPojo;
    }

    public void update(int orderId, OrderStatus status) throws ApiException {
        OrdersPojo exOrdersPojo = getCheck(orderId);
        exOrdersPojo.setStatus(status);
    }

    public List<OrdersPojo> getAll() {
        return orderDao.selectAll();
    }

    public int getOrderCount(ZonedDateTime dateTime){
        ZonedDateTime dateTimeStartOfDay = dateTime.toLocalDate().atStartOfDay(dateTime.getZone());
        ZonedDateTime dateTimeStartOfNextDay = dateTime.toLocalDate().plusDays(1).atStartOfDay(dateTime.getZone());
        return orderDao.selectOrderCount(dateTimeStartOfDay, dateTimeStartOfNextDay);
    }

    public void getCheckInvoicedStatus(int orderId) throws ApiException {
        OrdersPojo ordersPojo = getCheck(orderId);
        if(ordersPojo.getStatus().equals(OrderStatus.INVOICED)){
            throw new ApiException("Order is invoiced!");
        }
    }

    public void getCheckCancelledStatus(int orderId) throws ApiException {
        OrdersPojo ordersPojo = getCheck(orderId);
        if(ordersPojo.getStatus().equals(OrderStatus.CANCELLED)){
            throw new ApiException("Order is cancelled!");
        }
    }

    public void cancelOrder(int orderId) throws ApiException {
        update(orderId, OrderStatus.CANCELLED);
    }
}
