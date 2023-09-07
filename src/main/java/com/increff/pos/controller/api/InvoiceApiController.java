package com.increff.pos.controller.api;

import com.increff.pos.dto.InvoiceDto;
import com.increff.pos.service.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api
@RestController
public class InvoiceApiController {

    @Autowired
    private InvoiceDto invoiceDto;

    @ApiOperation(value = "Gets invoice data for an order by ID")
    @RequestMapping(path = "/api/invoices/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getInvoice(@PathVariable int id) throws ApiException, IOException {
        return invoiceDto.getInvoice(id);
    }


}
