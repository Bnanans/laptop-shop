package com.shop.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Laptop extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    public String brand;

    public double price;

    public int quantity;

    public String description;

    public String imageUrl;
}