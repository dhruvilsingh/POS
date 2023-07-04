package com.increff.pos.controller;

import com.increff.pos.dao.DailySalesDao;
import com.increff.pos.dto.DailySalesDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.DailySalesData;
import com.increff.pos.service.DailySalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api
public class DailySalesApiController {

    @Autowired
    DailySalesDto dailySalesDto;

    @ApiOperation(value = "Gets report of sales on each day")
    @RequestMapping(path = "/api/dailysales", method = RequestMethod.GET)
    public List<DailySalesData> getAll() {
        return dailySalesDto.getAll();
    }
}
