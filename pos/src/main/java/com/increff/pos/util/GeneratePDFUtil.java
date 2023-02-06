package com.increff.pos.util;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.service.ApiException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;

public class GeneratePDFUtil {
    private static final String SAVING_LOCATION = File.separator + "Users" + File.separator + "rishav" + File.separator
            + "Documents" + File.separator + "bills" + File.separator;

    public static String generatePDF(InvoiceData invoiceData, Integer orderId) throws ApiException {
        try {
            String orderIdStr = orderId.toString();
            RestTemplate restTemplate = new RestTemplate();
            final String uri = "http://localhost:8080/invoice-app/";
            String base64EndcodedString = restTemplate.postForObject(uri, invoiceData, String.class);
            String path = SAVING_LOCATION + orderIdStr + ".pdf";
            File file = new File(path);
            byte[] decodedBytes = Base64.decodeBase64(base64EndcodedString.getBytes());
            FileOutputStream fop = new FileOutputStream(file);
            fop.write(decodedBytes);
            fop.flush();
            fop.getFD().sync();
            fop.close();
            return path;
        } catch (Exception e) {
            throw new ApiException("Failed to generate Invoice");
        }
    }
}
