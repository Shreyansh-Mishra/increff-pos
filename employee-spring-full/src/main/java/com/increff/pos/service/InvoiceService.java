package com.increff.pos.service;

import com.increff.pos.dao.InvoiceDao;
import com.increff.pos.pojo.InvoicePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Transactional
    public void insertInvoice(InvoicePojo invoice){
        invoiceDao.insert(invoice);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InvoicePojo selectInvoice(int id) throws ApiException {
        InvoicePojo invoice = invoiceDao.selectId(id);
        if(invoice==null)
            throw new ApiException("Invoice with id "+id+" does not exist");
        return invoice;
    }
}
