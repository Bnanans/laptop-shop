package com.shop.resource;

import java.util.List;

import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderDetail;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @GET
    public List<Customer> getAll() {
        return Customer.listAll();
    }

    @GET
    @Path("/{id}")
    public Customer getById(@PathParam("id") Long id) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        return customer;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Customer update(@PathParam("id") Long id, Customer updatedCustomer) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        customer.fullName = updatedCustomer.fullName;
        customer.email = updatedCustomer.email;
        customer.phone = updatedCustomer.phone;
        customer.address = updatedCustomer.address;
        return customer;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        List<Order> orders = Order.list("customer", customer);
        for (Order order : orders) {
            OrderDetail.delete("order", order);
            order.delete();
        }

        Customer.deleteById(id);
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        Customer customer = Customer.find("email", request.email).firstResult();
        if (customer == null || !customer.password.equals(request.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(customer).build();
    }

}