package com.shop.repository;

import com.shop.entity.OrderDetail;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderDetailRepository implements PanacheRepository<OrderDetail> {

}
