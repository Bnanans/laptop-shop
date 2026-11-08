package com.shop.repository;

import com.shop.entity.Admin;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdminRepository implements PanacheRepository<Admin> {

    public Admin findByUsername(String username) {
        return find("username", username).firstResult();
    }
}
