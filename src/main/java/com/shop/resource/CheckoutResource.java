package com.shop.resource;

import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderDetail;
import com.shop.entity.Laptop;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@Path("/checkout")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutResource {

    public static class CheckoutRequest {
        public CustomerPayload customer;
        public List<CartItem> cart;
    }

    public static class CustomerPayload {
        public String fullName;
        public String email;
        public String phone;
        public String address;
    }

    public static class CartItem {
        public Long id;
        public int qty;
    }

    @POST
    @Transactional
    public Order placeOrder(CheckoutRequest request) {
        Customer customer = Customer.find("email", request.customer.email).firstResult();
        if (customer == null) {
            customer = new Customer();
            customer.email = request.customer.email;
        }
        customer.fullName = request.customer.fullName;
        customer.phone = request.customer.phone;
        customer.address = request.customer.address;
        customer.persist();

        Order order = new Order();
        order.customer = customer;
        order.orderDate = LocalDateTime.now();
        order.status = "Đang xử lý";
        order.totalAmount = 0;
        order.persist();

        double total = 0;
        for (CartItem item : request.cart) {
            Laptop laptop = Laptop.findById(item.id);
            if (laptop != null) {
                OrderDetail detail = new OrderDetail();
                detail.order = order;
                detail.laptop = laptop;
                detail.quantity = item.qty;
                detail.unitPrice = laptop.price;
                detail.persist();

                total += (laptop.price * item.qty);
                laptop.quantity -= item.qty;
            }
        }
        order.totalAmount = total;
        return order;
    }
}