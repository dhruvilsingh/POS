package com.increff.pos.controller.api;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductApiController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds an product")
    @RequestMapping(path = "/api/products", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm) throws ApiException {
        productDto.add(productForm);
    }

    @ApiOperation(value = "Adds multiple products")
    @RequestMapping(path = "/api/products/upload", method = RequestMethod.POST)
    public void processData(@RequestBody List<ProductForm> fileData) throws ApiException {
        productDto.upload(fileData);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException {
        return productDto.get(id);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/api/products", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        return productDto.getAll();
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
        productDto.update(id, productForm);
    }
}
