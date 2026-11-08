package com.shop.service;

import com.shop.entity.Order;
import com.shop.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepo;

    public List<Order> getAll() {
        return orderRepo.listAll();
    }

    public Order getById(Long id) {
        return orderRepo.findById(id);
    }
}
