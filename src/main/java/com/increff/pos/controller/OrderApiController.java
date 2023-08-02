package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class OrderApiController { //TODO : to create a api to create order
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Creates an order from existing cart")
    @RequestMapping(path = "/api/orders", method = RequestMethod.POST)
    public void createOrder() throws ApiException {
        orderDto.createOrder();
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "/api/orders", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Cancels an order")
    @RequestMapping(path = "/api/orders/{id}", method = RequestMethod.PUT)
    public void cancelOrder(@PathVariable int id) throws ApiException {
        orderDto.cancelOrder(id);
    }

}
