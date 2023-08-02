package com.increff.pos.service;

import com.increff.pos.model.data.InvoiceData;
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
    @Value("${filePath}")
    private String filePath;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Resource> getInvoice(InvoiceData invoiceData) throws IOException { //TODO : to create a clientWrapper class and implement all external apis in that
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InvoiceData> requestEntity = new HttpEntity<>(invoiceData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(invoiceAppUrl, requestEntity, String.class);
        String base64String = response.getBody();
        return convertBase64ToPdf(base64String);
    }

    private ResponseEntity<Resource> convertBase64ToPdf(String base64String) throws IOException { //TODO to create a separate helper function
        byte[] pdfBytes = Base64.getDecoder().decode(base64String);
        File outputFile = new File(filePath);
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(pdfBytes);
        fos.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", outputFile.getName());
        ResponseEntity<Resource> responseOut = new ResponseEntity<>(
                new FileSystemResource(filePath),
                headers,
                HttpStatus.OK
        );
        return responseOut;
    }


}
