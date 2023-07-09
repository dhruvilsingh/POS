package com.increff.pos.service;

import com.increff.pos.dao.CartDao;
import com.increff.pos.dao.DailySalesDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailySalesService {

    @Autowired
    private DailySalesDao dailySalesDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    //(cron = "0 0 0 * * *")
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        System.out.println(currentDate);
        DailySalesPojo dailySalesPojo = new DailySalesPojo();
        dailySalesPojo.setDate(currentDate);
        dailySalesPojo.setOrderCount(orderDao.selectcount(currentDate));
        dailySalesPojo.setOrderItemCount(orderItemDao.selectItemCount(currentDate));
        dailySalesPojo.setRevenue(orderItemDao.selectRevenue(currentDate));
        dailySalesDao.insert(dailySalesPojo);
    }

    @Transactional
    public List<DailySalesPojo> getAll() {
        return dailySalesDao.selectAll();
    }

}
