package com.increff.pos.service;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class InvoiceService {
    @Value("${invoiceAppUrl}")
    private String invoiceAppUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Resource> getInvoice(InvoiceData invoiceData) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InvoiceData> requestEntity = new HttpEntity<>(invoiceData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(invoiceAppUrl, requestEntity, String.class);
        String base64String = response.getBody();
        return FileUtil.convertBase64ToPdf(base64String);
    }

}
