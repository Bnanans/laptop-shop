package com.shop.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Admin extends PanacheEntity {
    public String username;
    public String password;
    public String fullName;
}