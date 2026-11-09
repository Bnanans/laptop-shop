package com.shop.service;

import com.shop.entity.Laptop;
import com.shop.repository.LaptopRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@ApplicationScoped
public class LaptopService {

    @Inject
    LaptopRepository repository;

    public List<Laptop> getAll() {
        return repository.listAll();
    }

    public Laptop getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void add(Laptop laptop) {
        validateQuantity(laptop);
        repository.persist(laptop);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void update(Long id, Laptop newLaptop) {
        validateQuantity(newLaptop);

        Laptop laptop = repository.findById(id);

        if (laptop != null) {
            laptop.name = newLaptop.name;
            laptop.brand = newLaptop.brand;
            laptop.price = newLaptop.price;
            laptop.quantity = newLaptop.quantity;
            laptop.description = newLaptop.description;
            laptop.imageUrl = newLaptop.imageUrl;
        }
    }

    private void validateQuantity(Laptop laptop) {
        if (laptop == null || laptop.quantity < 0) {
            throw new BadRequestException("Số lượng tồn kho không được âm");
        }
    }

    public List<Laptop> search(String keyword) {
        return repository.searchByName(keyword);
    }
}