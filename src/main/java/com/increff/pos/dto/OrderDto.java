package com.increff.pos.dto;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {

    @Autowired
    private OrderService orderService;

    public OrderData get(int id) throws ApiException {
        OrderPojo orderPojo = orderService.get(id);
        return convert(orderPojo);
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list = orderService.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OrderPojo orderPojo : list) {
            list2.add(convert(orderPojo));
        }
        return list2;
    }

    private static OrderData convert(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setOrderId(orderPojo.getOrderId());
        orderData.setOrderTime(orderPojo.getOrderTime());
        return orderData;
    }

}
