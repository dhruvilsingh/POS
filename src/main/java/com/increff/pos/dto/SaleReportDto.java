package com.increff.pos.dto;

import com.increff.pos.model.DailySalesData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SaleReportData;
import com.increff.pos.model.SaleReportForm;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.DailySalesService;
import com.increff.pos.service.SaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaleReportDto {

    @Autowired
    private SaleReportService saleReportService;

    public List<SaleReportData> getReport(SaleReportForm saleReportForm) throws ParseException {
        List<Object[]> resultList = saleReportService.getReport(saleReportForm);;
        List<SaleReportData> saleReportDataList = new ArrayList<>();
        for (Object[] result : resultList) {
            saleReportDataList.add(convert(result));
        }
        return saleReportDataList;
    }

    private SaleReportData convert(Object object[]){
        SaleReportData saleReportData = new SaleReportData();
        saleReportData.setCategory(object[3].toString());
        saleReportData.setBrandName(object[2].toString());
        saleReportData.setQuantity(Integer.valueOf(object[0].toString()).intValue());
        saleReportData.setRevenue(Double.valueOf(object[1].toString()).doubleValue());
        return saleReportData;
    }

}
