package com.shop.service;

import com.shop.entity.Admin;
import com.shop.repository.AdminRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdminService {

    @Inject
    AdminRepository repository;

    public Admin findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional
    public void add(Admin admin) {
        repository.persist(admin);
    }
}
