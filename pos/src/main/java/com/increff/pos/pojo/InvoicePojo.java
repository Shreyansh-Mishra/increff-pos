package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@Table(name="Invoice")
public class InvoicePojo {
    @Id
    private Integer id;
    @NonNull
    @Column(nullable = false)
    private String path;

}
