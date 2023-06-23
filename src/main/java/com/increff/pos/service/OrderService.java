package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public int add(OrderPojo orderPojo) throws ApiException {
        return orderDao.insert(orderPojo);
    }
}
