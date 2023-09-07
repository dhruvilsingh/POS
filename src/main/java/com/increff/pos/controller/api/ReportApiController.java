package com.increff.pos.controller.api;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.BrandReportData;
import com.increff.pos.model.data.DailySalesData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SaleReportData;
import com.increff.pos.model.form.SaleReportForm;
import com.increff.pos.service.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@Api
public class ReportApiController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Gets report of sales on each day")
    @RequestMapping(path = "/api/reports/daily-sales", method = RequestMethod.GET)
    public List<DailySalesData> getDailySaleReport() throws ApiException {
        return reportDto.getDailySalesReport();
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/api/reports/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return reportDto.getInventoryReport();
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/api/reports/brand", method = RequestMethod.GET)
    public List<BrandReportData> getBrandReport() throws ApiException {
        return reportDto.getBrandReport();
    }

    @ApiOperation(value = "Gets sale report")
    @RequestMapping(path = "/api/reports/sale", method = RequestMethod.POST)
    public List<SaleReportData> getSaleReport(@RequestBody SaleReportForm saleReportForm) throws ParseException, ApiException {
        System.out.println(saleReportForm.getStartDate());
        return reportDto.getSaleReport(saleReportForm);
    }

}
