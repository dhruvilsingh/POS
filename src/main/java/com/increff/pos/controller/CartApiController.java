package com.increff.pos.controller;

import com.increff.pos.dto.CartDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class CartApiController {
    @Autowired
    private CartDto cartDto;

    @ApiOperation(value = "Adds an brand")
    @RequestMapping(path = "/api/cart", method = RequestMethod.POST)
    public void add(@RequestBody CartForm cartForm) throws ApiException {
        cartDto.add(cartForm);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/api/cart/{id}", method = RequestMethod.GET)
    public CartData get(@PathVariable int id) throws ApiException {
        return cartDto.get(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/cart", method = RequestMethod.GET)
    public List<CartData> getAll() throws ApiException{
        return cartDto.getAll();
    }

    @ApiOperation(value = "Deletes an item")
    @RequestMapping(path = "/api/cart/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        cartDto.delete(id);
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/cart/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody CartForm cartForm) throws ApiException {
        cartDto.update(id, cartForm);
    }
}

