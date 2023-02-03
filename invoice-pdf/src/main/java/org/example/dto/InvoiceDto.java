package org.example.dto;

import org.example.model.OrderFOPObject;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


import static org.example.util.InvoiceFOP.generatePdf;
import static org.example.util.InvoiceFOP.getXMLSource;


@Component
public class InvoiceDto {
    public String generateInvoice(OrderFOPObject orderFop) throws Exception {
        ByteArrayOutputStream xmlSource = getXMLSource(orderFop);
        StreamSource streamSource = new StreamSource(new ByteArrayInputStream(xmlSource.toByteArray()));
        return generatePdf(streamSource);
    }
}
