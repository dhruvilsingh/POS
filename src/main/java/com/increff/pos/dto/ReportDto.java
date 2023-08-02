package com.increff.pos.dto;

import com.increff.pos.model.data.*;
import com.increff.pos.model.forms.SaleReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.*;
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
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BrandService brandService;

    @Transactional
    @Scheduled(fixedDelay = 30000) //TODO to use app.properties and make it confi.
    //TODO : to add schedulers in separate package.
    public void addDailySalesReport() throws ApiException {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime date = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
        DailySalesPojo dailySalesPojo = new DailySalesPojo();
        dailySalesPojo.setDate(date);
        dailySalesPojo.setOrderCount(orderService.getOrderCount(currentDateTime));
        dailySalesPojo.setOrderItemCount(orderItemService.getItemCount(currentDateTime));
        dailySalesPojo.setRevenue(orderItemService.getRevenue(currentDateTime)); //todo : to merge getItemCount, getRevenue to a single function.
        DailySalesPojo existingDailySalesPojo = reportService.getSaleByDate(date);
        if(existingDailySalesPojo != null){
            existingDailySalesPojo.setDate(dailySalesPojo.getDate());
            existingDailySalesPojo.setOrderCount(dailySalesPojo.getOrderCount());
            existingDailySalesPojo.setOrderItemCount(dailySalesPojo.getOrderItemCount());
            existingDailySalesPojo.setRevenue(dailySalesPojo.getRevenue());
            return;
        }
        reportService.addDailySales(dailySalesPojo);
    }

    public List<DailySalesData> getDailySalesReport() {
        List<DailySalesPojo> list = reportService.getDailySalesReport();
        List<DailySalesData> list2 = new ArrayList<DailySalesData>();
        for (DailySalesPojo dailySalesPojo : list) {
            list2.add(convertDailySale(dailySalesPojo));
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

    public List<BrandReportData> getBrandReport() {
        List<BrandPojo> list = brandService.getAll();
        List<BrandReportData> list2 = new ArrayList<BrandReportData>();
        for (BrandPojo brandPojo : list) {
            list2.add(convertBrand(brandPojo));
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

    private static DailySalesData convertDailySale(DailySalesPojo dailySalesPojo) {
        DailySalesData dailySalesData = new DailySalesData();
        dailySalesData.setDate(dailySalesPojo.getDate());
        dailySalesData.setOrderCount(dailySalesPojo.getOrderCount());
        dailySalesData.setOrderItemCount(dailySalesPojo.getOrderItemCount());
        dailySalesData.setRevenue(dailySalesPojo.getRevenue());
        return dailySalesData;
    }

    private static InventoryReportData convertInventory(Object object[]){
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand((String) object[0]);
        inventoryReportData.setCategory((String) object[1]);
        inventoryReportData.setQuantity(((Long) object[2]).intValue());
        return inventoryReportData;
    }

    private static BrandReportData convertBrand(BrandPojo brandPojo){
        BrandReportData brandReportData = new BrandReportData();
        brandReportData.setBrand(brandPojo.getBrand());
        brandReportData.setCategory(brandPojo.getCategory());
        return brandReportData;
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
