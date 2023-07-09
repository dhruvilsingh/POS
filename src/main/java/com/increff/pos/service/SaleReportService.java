package com.increff.pos.service;

import com.increff.pos.dao.SaleReportDao;
import com.increff.pos.model.SaleReportData;
import com.increff.pos.model.SaleReportForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SaleReportService {

    @Autowired
    private SaleReportDao saleReportDao;

    @Transactional
    public List<Object[]> getReport(SaleReportForm saleReportForm) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = saleReportForm.getEndDate();
        String startDate = saleReportForm.getStartDate();
        String brandName = saleReportForm.getBrandName();
        String category = saleReportForm.getCategory();
        if(endDate == ""){
            endDate = (LocalDate.now().toString());
            if(startDate == ""){
                startDate = (LocalDate.now().minusDays(90).toString());
            }
        }
        else {
            if (startDate == "") {
                startDate = (LocalDate.parse(saleReportForm.getEndDate()).minusDays(90).toString());
            }
        }
        if(brandName == "")
            brandName = null;
        if(category == "")
            category = null;
        Date convertedStartDate = sdf.parse(startDate);
        Date convertedEndDate = sdf.parse(endDate);
        return saleReportDao.selectReport(brandName, category, convertedStartDate, convertedEndDate);
    }

}


