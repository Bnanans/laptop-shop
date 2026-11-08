package com.shop.service;

import com.shop.entity.Customer;
import com.shop.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository repository;

    public List<Customer> getAll() {
        return repository.listAll();
    }

    public Customer getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void add(Customer customer) {
        repository.persist(customer);
    }

    @Transactional
    public void update(Long id, Customer newCustomer) {
        Customer c = repository.findById(id);
        if (c != null) {
            c.fullName = newCustomer.fullName;
            c.email = newCustomer.email;
            c.phone = newCustomer.phone;
            c.address = newCustomer.address;
        }
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
