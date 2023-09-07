package com.increff.pos.controller.api;

import com.increff.pos.dto.CartDto;
import com.increff.pos.model.data.CartData;
import com.increff.pos.model.form.CartForm;
import com.increff.pos.service.exception.ApiException;
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

    @ApiOperation(value = "Adds an item to cart")
    @RequestMapping(path = "/api/carts", method = RequestMethod.POST)
    public void add(@RequestBody CartForm cartForm) throws ApiException {
        cartDto.add(cartForm);
    }

    @ApiOperation(value = "Gets a cart item by ID")
    @RequestMapping(path = "/api/carts/{id}", method = RequestMethod.GET)
    public CartData get(@PathVariable int id) throws ApiException {
        return cartDto.get(id);
    }

    @ApiOperation(value = "Gets list of all items in cart")
    @RequestMapping(path = "/api/carts", method = RequestMethod.GET)
    public List<CartData> getAll() throws ApiException{
        return cartDto.getAll();
    }

    @ApiOperation(value = "Deletes an item from cart")
    @RequestMapping(path = "/api/carts/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        cartDto.delete(id);
    }

    @ApiOperation(value = "Updates an item in the cart")
    @RequestMapping(path = "/api/carts/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody CartForm cartForm) throws ApiException {
        cartDto.update(id, cartForm);
    }
}

