package com.increff.pos.controller;

import com.increff.pos.dto.SaleReportDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.SaleReportData;
import com.increff.pos.model.SaleReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@Api
public class SaleReportApiController {

    @Autowired
    private SaleReportDto saleReportDto;

    @ApiOperation(value = "Gets sale report")
    @RequestMapping(path = "/api/sale-report", method = RequestMethod.POST)
    public List<SaleReportData> getReport(@RequestBody SaleReportForm saleReportForm) throws ParseException {
        System.out.println(saleReportForm.getStartDate());
        return saleReportDto.getReport(saleReportForm);
    }

}
