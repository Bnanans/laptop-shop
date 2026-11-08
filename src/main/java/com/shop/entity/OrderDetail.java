package com.shop.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderDetail extends PanacheEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    public Order order;
    @ManyToOne
    public Laptop laptop;
    public int quantity;
    public double unitPrice;
}