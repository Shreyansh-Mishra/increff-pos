package com.increff.pos.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import com.increff.pos.service.OrderItemsService;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderFOPObject;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;

@Component
public class InvoiceFOP {
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductService productService;

	@Autowired
	OrderItemsService orderItemsService;
	
	public String generatePdf(int id) throws Exception {
		OrderPojo order = orderService.selectOrderById(id);
		List<OrderItemPojo> items = orderItemsService.selectItems(id);
		OrderFOPObject orderFop = new OrderFOPObject();
		orderFop.setOrderId(order.getId());
		orderFop.setDate(order.getTime().toString().split("T")[0]);
		List<OrderItemData> fopItems = new ArrayList<OrderItemData>();
		double total = 0;
		for(OrderItemPojo item: items) {
			OrderItemData i = new OrderItemData();
			ProductPojo product = productService.selectById(item.getProductId());
			i.setItemName(product.getName());
			i.setBarcode(product.getBarcode());
			i.setOrderId(item.getOrderId());
			i.setQuantity(item.getQuantity());
			i.setSellingPrice(item.getSellingPrice());
			i.setCost(item.getSellingPrice()*item.getQuantity());
			total += i.getCost();
			fopItems.add(i);
		}
		orderFop.setOrderItems(fopItems);
		orderFop.setTotal(total);
		ByteArrayOutputStream xmlSource = getXMLSource(orderFop);
		StreamSource streamSource = new StreamSource(new ByteArrayInputStream(xmlSource.toByteArray()));
		String date = order.getTime().toString();
		//keep only the date and time seperated by a '-'
		date = date.split("T", 2)[0] + "-" + date.split("T", 2)[1].split(":")[0] + "-" + date.split("T", 2)[1].split(":")[1];
		String fileName = "Invoice-"+order.getId()+"-"+date+".pdf";
		generatePdf(streamSource,fileName);
		return "C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName;
	}
	
	private static ByteArrayOutputStream getXMLSource(OrderFOPObject orderFop) throws Exception {
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


	private static void generatePdf(StreamSource streamSource,String fileName)
            throws FOPException, TransformerException, IOException {
        File xsltFile = new File("C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\template.xsl");

        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance();
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out = new java.io.FileOutputStream("C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName);

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(streamSource, res);
        } finally {
            out.close();
        }
    }
	
}
