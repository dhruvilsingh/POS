package com.increff.pos.controller;

import com.increff.pos.dto.InvoiceSenderDto;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Api
@RestController
public class InvoiceSender {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InvoiceSenderDto invoiceSenderDto;

    @PostMapping("/api/send-invoice")
    public ResponseEntity<String> sendInvoice(@RequestBody InvoiceData invoiceData){
        String invoiceAppUrl = "http://localhost:6969/api/generate-invoice";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InvoiceData> requestEntity = new HttpEntity<>(invoiceData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(invoiceAppUrl, requestEntity, String.class);
        System.out.println(response.getBody());
        String base64String = response.getBody();
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Invoice sent successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending invoice: " + response.getBody());
        }

        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64String);
            String fileName = "invoice" + invoiceData.getNumber() + ".pdf";
            String filePath = "/Users/dhruvilsingh/Downloads/" + fileName;
            System.out.println("Invoice will be downloaded to: " + filePath);
            Files.write(Paths.get(filePath), pdfBytes);
            return ResponseEntity.ok("Invoice downloaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading invoice");
        }

    }

    @ApiOperation(value = "Gets invoice data for an order by ID")
    @RequestMapping(path = "/api/invoicesender/{id}", method = RequestMethod.GET)
    public InvoiceData getInvoiceItem(@PathVariable int id) throws ApiException {
        return invoiceSenderDto.getInvoiceItem(id);
    }


}
