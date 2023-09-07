package com.increff.pos.controller.api;


import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.service.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BrandApiController {

    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Adds an brand")
    @RequestMapping(path = "/api/brands", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        brandDto.add(brandForm);
    }

    @ApiOperation(value = "Adds multiple brands")
    @RequestMapping(path = "/api/brands/upload", method = RequestMethod.POST)
    public void processData(@RequestBody List<BrandForm> fileData) throws ApiException {
        brandDto.upload(fileData);
    }

    @ApiOperation(value = "Gets a brand by ID")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return brandDto.get(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brands", method = RequestMethod.GET)
    public List<BrandData> getAll() throws ApiException {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm brandForm) throws ApiException {
        brandDto.update(id, brandForm);
    }

}
