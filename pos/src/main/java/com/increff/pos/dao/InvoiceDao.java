package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InvoiceDao extends AbstractDao{
    private static final String SELECT_ID = "select i from InvoicePojo i where i.id=:id";

    public void insert(InvoicePojo i){
        em().persist(i);
    }

    public InvoicePojo selectId(Integer id){
        TypedQuery<InvoicePojo> query = getQuery(SELECT_ID, InvoicePojo.class);
        query.setParameter("id", id);
        InvoicePojo invoice = getSingle(query);
        System.out.println(invoice.getPath());
        return invoice;
    }
}
