package org.example.util;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.example.model.OrderFOPObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Base64;

public class InvoiceFOP {

    public static ByteArrayOutputStream getXMLSource(OrderFOPObject orderFop) throws Exception {
        JAXBContext context;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            context = JAXBContext.newInstance(OrderFOPObject.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(orderFop, outStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return outStream;
    }


    public static String generatePdf(StreamSource streamSource)
            throws FOPException, TransformerException, IOException {
        File xsltFile = new File("C:\\Users\\Shreyansh\\Desktop\\increff2\\point-of-sale\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\template.xsl");

        FopFactory fopFactory = FopFactory.newInstance();
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        //create a temporary output stream to store the pdf and later convert it to a base64 string
        String base64String = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outStream);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(streamSource, res);
        } finally {
            //convert the pdf to a base64 string
            base64String = Base64.getEncoder().encodeToString(outStream.toByteArray());
            outStream.close();

        }
    return base64String;
    }
}
