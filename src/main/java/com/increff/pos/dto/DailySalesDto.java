package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.DailySalesData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.DailySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DailySalesDto {

    @Autowired
    private DailySalesService dailySalesService;

    public List<DailySalesData> getAll() {
        List<DailySalesPojo> list = dailySalesService.getAll();
        List<DailySalesData> list2 = new ArrayList<DailySalesData>();
        for (DailySalesPojo dailySalesPojo : list) {
            list2.add(convert(dailySalesPojo));
        }
        return list2;
    }

    private static DailySalesData convert(DailySalesPojo dailySalesPojo) {
        DailySalesData dailySalesData = new DailySalesData();
        dailySalesData.setDate(dailySalesPojo.getDate());
        dailySalesData.setOrderCount(dailySalesPojo.getOrderCount());
        dailySalesData.setOrderItemCount(dailySalesPojo.getOrderItemCount());
        dailySalesData.setRevenue(dailySalesPojo.getRevenue());
        return dailySalesData;
    }
}
