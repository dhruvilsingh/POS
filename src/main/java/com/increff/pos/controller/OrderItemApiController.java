package com.increff.pos.controller;

import com.increff.pos.dto.CartDto;
import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.CartData;
import com.increff.pos.model.CartForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderItemApiController {
    @Autowired
    private OrderItemDto orderItemDto;

    @ApiOperation(value = "Adds an brand")
    @RequestMapping(path = "/api/orderItem", method = RequestMethod.POST)
    public void add() throws ApiException {
        orderItemDto.add();
    }

}
