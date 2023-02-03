package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InvoiceDao extends AbstractDao{
    String select_id = "select i from InvoicePojo i where i.id=:id";
    public void insert(InvoicePojo i){
        em().persist(i);
    }

    public InvoicePojo selectId(int id){
        TypedQuery<InvoicePojo> query = getQuery(select_id, InvoicePojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
}
