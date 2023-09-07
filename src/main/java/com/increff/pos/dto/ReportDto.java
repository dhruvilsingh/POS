package com.increff.pos.dto;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.SaleReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.*;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDto {

    @Autowired
    private ReportService reportService;

    @Autowired
    private BrandService brandService;

    public List<DailySalesData> getDailySalesReport() throws ApiException {
        List<DailySalesPojo> list = reportService.getDailySalesReport();
        List<DailySalesData> list2 = new ArrayList<DailySalesData>();
        for (DailySalesPojo dailySalesPojo : list) {
            list2.add(ConversionUtil.convert(dailySalesPojo, DailySalesData.class));
        }
        return list2;
    }

    public List<InventoryReportData> getInventoryReport(){
        List<Object[]> resultList = reportService.getInventoryReport();;
        List<InventoryReportData> reportDataList = new ArrayList<>();
        for (Object[] result : resultList) {
            reportDataList.add(convertInventory(result));
        }
        return reportDataList;
    }

    public List<BrandReportData> getBrandReport() throws ApiException {
        List<BrandPojo> list = brandService.getAll();
        List<BrandReportData> list2 = new ArrayList<BrandReportData>();
        for (BrandPojo brandPojo : list) {
            list2.add(ConversionUtil.convert(brandPojo, BrandReportData.class));
        }
        return list2;
    }

    public List<SaleReportData> getSaleReport(SaleReportForm saleReportForm) throws ParseException, ApiException {
        List<Object[]> resultList = reportService.getSaleReport(saleReportForm);;
        List<SaleReportData> saleReportDataList = new ArrayList<>();
        for (Object[] result : resultList) {
            saleReportDataList.add(convertSale(result));
        }
        return saleReportDataList;
    }

    private static InventoryReportData convertInventory(Object object[]){
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand((String) object[0]);
        inventoryReportData.setCategory((String) object[1]);
        inventoryReportData.setQuantity(((Long) object[2]).intValue());
        return inventoryReportData;
    }

    private static SaleReportData convertSale(Object object[]){
        SaleReportData saleReportData = new SaleReportData();
        saleReportData.setCategory(object[3].toString());
        saleReportData.setBrand(object[2].toString());
        saleReportData.setQuantity(Integer.valueOf(object[0].toString()));
        saleReportData.setRevenue(Double.valueOf(object[1].toString()));
        return saleReportData;
    }
}
