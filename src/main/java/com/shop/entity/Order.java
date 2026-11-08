package com.shop.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
public class Order extends PanacheEntity {
    @ManyToOne
    public Customer customer;
    public LocalDateTime orderDate;
    public double totalAmount;
    public String status; // Ví dụ: "PENDING", "COMPLETED"
}