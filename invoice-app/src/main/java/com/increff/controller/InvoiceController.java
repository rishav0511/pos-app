package com.increff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.model.InvoiceData;
import com.increff.util.PDFUtil;
import com.increff.util.XMLUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;


@RestController
public class InvoiceController {
    @PostMapping(value = "", consumes = "application/json")
    public String encodeInvoiceData
            (@RequestBody String invoiceData) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InvoiceData data = mapper.readValue(invoiceData, InvoiceData.class);
        XMLUtil.createXml(data);
        PDFUtil.createPDF();
        byte[] encodedBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File("bill.pdf"));
//        PDFUtil.createResponse(response, encodedBytes);

//        String BasicBase64format = Base64.getEncoder().encodeToString(invoiceData.getBytes());
//        System.out.println("Encoded String:\n" + BasicBase64format);
        String b64PDF = Base64.getEncoder().encodeToString(encodedBytes);
//        System.out.println(b64PDF);
        return b64PDF;
    }
}
