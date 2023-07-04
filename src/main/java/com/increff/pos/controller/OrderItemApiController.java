package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
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

    @ApiOperation(value = "Adds an item")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add() throws ApiException {
        orderItemDto.add();
    }

    @ApiOperation(value = "Updates an item")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id , @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderItemDto.update(id , orderItemForm);
    }

    @ApiOperation(value = "Gets all items in an order by ID")
    @RequestMapping(path = "/api/orderitems/{id}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable int id) throws ApiException {
        List<OrderItemData> list= orderItemDto.getAll(id);
        return list;
    }

    @ApiOperation(value = "Deletes an item from order")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        orderItemDto.delete(id);
    }

    @ApiOperation(value = "Gets an item by ID")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public OrderItemData getItem(@PathVariable int id) throws ApiException {
        return orderItemDto.get(id);
    }

}
