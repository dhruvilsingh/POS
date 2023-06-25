package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
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
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add() throws ApiException {
        orderItemDto.add();
    }

    @ApiOperation(value = "Gets a brand by ID")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable int id) throws ApiException {
        System.out.println("in order item get");
        List<OrderItemData> list= orderItemDto.getAll(id);
        for (OrderItemData orderItemData : list) {
            System.out.println("  " + orderItemData.getProductBarcode());
            System.out.println("  "+orderItemData.getProductName());
            System.out.println("  "  +orderItemData.getProductQuantity());
            System.out.println("  "+ orderItemData.getProductMrp());
            System.out.println("  " + orderItemData.getProductSP());
        }
        return list;
    }

}
