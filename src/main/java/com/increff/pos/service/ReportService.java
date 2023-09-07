package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ReportDao;
import com.increff.pos.model.form.SaleReportForm;
import com.increff.pos.pojo.DailySalesPojo;
import com.increff.pos.service.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public void addDailySales(DailySalesPojo dailySalesPojo){
        reportDao.insertDailySales(dailySalesPojo);
    }

    public List<Object[]> getInventoryReport() {
        return reportDao.selectInventoryReport();
    }

    public List<DailySalesPojo> getDailySalesReport() {
        return reportDao.selectDailySalesReport();
    }

    public DailySalesPojo getSaleByDate(ZonedDateTime date){
        return reportDao.selectSaleByDate(date);
    }

    public List<Object[]> getSaleReport(SaleReportForm saleReportForm) throws ApiException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String brand = saleReportForm.getBrand();
        String category = saleReportForm.getCategory();
        if(brand == "")
            brand = null;
        if(category == "")
            category = null;
        Date convertedStartDate = sdf.parse(saleReportForm.getStartDate());
        Date convertedEndDate = sdf.parse(saleReportForm.getEndDate());
        if(convertedEndDate.before(convertedStartDate)){
            throw new ApiException("End date cannot be before start date!");
        }
        return reportDao.selectSaleReport(brand, category, convertedStartDate, convertedEndDate);
    }

}
