package com.increff.pos.schedulers;

import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@Transactional
public class DailySalesReportScheduler {

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Scheduled(fixedDelay = 10000)
    public void scheduleDailySale() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime date = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        DailySalesPojo dailySalesPojo = new DailySalesPojo();
        dailySalesPojo.setDate(date);
        dailySalesPojo.setOrderCount(orderService.getOrderCount(currentDateTime));
        dailySalesPojo.setOrderItemCount(orderItemService.getItemCount(currentDateTime));
        dailySalesPojo.setRevenue(orderItemService.getRevenue(currentDateTime));
        DailySalesPojo existingDailySalesPojo = reportService.getSaleByDate(date);
        if (existingDailySalesPojo != null) {
            existingDailySalesPojo.setDate(dailySalesPojo.getDate());
            existingDailySalesPojo.setOrderCount(dailySalesPojo.getOrderCount());
            existingDailySalesPojo.setOrderItemCount(dailySalesPojo.getOrderItemCount());
            existingDailySalesPojo.setRevenue(dailySalesPojo.getRevenue());
        } else {
            reportService.addDailySales(dailySalesPojo);
        }
    }
}
