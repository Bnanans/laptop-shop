package com.shop.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Customer extends PanacheEntity {
    public String fullName;
    public String email;
    public String phone;
    public String address;
    public String password;
}