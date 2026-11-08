package com.shop.repository;

import com.shop.entity.Laptop;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class LaptopRepository implements PanacheRepository<Laptop> {
    public List<Laptop> searchByName(String keyword) {

        return find("lower(name) like ?1",
                "%" + keyword.toLowerCase() + "%")
                .list();
    }
}