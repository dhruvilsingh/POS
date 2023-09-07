package com.increff.pos.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtil {

    @Value("${filePath}")
    private static String filePath;

    public static ResponseEntity<Resource> convertBase64ToPdf(String base64String) throws IOException {
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
