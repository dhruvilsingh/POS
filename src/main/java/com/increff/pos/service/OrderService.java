package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public int add(OrderPojo orderPojo) throws ApiException {
        return orderDao.insert(orderPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo get(int orderId) throws ApiException {
        return getCheck(orderId);
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    @Transactional
    public OrderPojo getCheck(int orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.select(orderId);
        if (orderPojo == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + orderId);
        }
        return orderPojo;
    }

}
