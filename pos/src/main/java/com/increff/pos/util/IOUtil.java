package com.increff.pos.util;

import com.increff.pos.model.StatusReport;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class IOUtil {

	public static void closeQuietly(Closeable c) {
		if (c == null) {
			return;
		}

		try {
			c.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	public static ByteArrayResource generateExceptionTsv(List<StatusReport> exceptions) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out);
		for (StatusReport exception : exceptions) {
			//run the below line only once for this loop
			if (exceptions.indexOf(exception) == 0) {
				if(exception.getBrand() != null)
					writer.append("Brand").append('\t');
				if(exception.getCategory() != null)
					writer.append("Category").append('\t');
				if(exception.getBarcode() != null)
					writer.append("Barcode").append('\t');
				if(exception.getMrp() != null)
					writer.append("MRP").append('\t');
				if(exception.getName()!=null)
					writer.append("Name").append('\t');
				if(exception.getQuantity()!=null)
					writer.append("Quantity").append('\t');
				if(exception.getMessage() != null)
					writer.append("Error").append('\t');
				writer.append('\n');
			}
			if(exception.getBrand() != null)
				writer.append(exception.getBrand()).append('\t');
			if(exception.getCategory() != null)
				writer.append(exception.getCategory()).append('\t');
			if(exception.getBarcode() != null)
				writer.append(exception.getBarcode()).append('\t');
			if(exception.getMrp() != null)
				writer.append(exception.getMrp().toString()).append('\t');
			if(exception.getName()!=null)
				writer.append(exception.getName()).append('\t');
			if(exception.getQuantity()!=null)
				writer.append(exception.getQuantity().toString()).append('\t');
			if(exception.getMessage() != null)
				writer.append(exception.getMessage()).append('\n');
		}
		writer.close();
		return new ByteArrayResource(out.toByteArray());
	}






}
