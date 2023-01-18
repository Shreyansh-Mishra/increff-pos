package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class InvoicePojo {
    @Id
    private int id;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
