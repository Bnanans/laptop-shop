package com.shop.resource;

import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderDetail;
import com.shop.entity.Laptop;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        if (request == null || request.customer == null || request.cart == null || request.cart.isEmpty()) {
            throw new BadRequestException("Thông tin đơn hàng không hợp lệ");
        }

        Map<Long, Integer> quantitiesByLaptop = new TreeMap<>();
        for (CartItem item : request.cart) {
            if (item == null || item.id == null || item.qty <= 0) {
                throw new BadRequestException("Số lượng sản phẩm phải lớn hơn 0");
            }
            try {
                quantitiesByLaptop.merge(item.id, item.qty, (current, added) -> Math.addExact(current, added));
            } catch (ArithmeticException e) {
                throw new BadRequestException("Số lượng sản phẩm không hợp lệ");
            }
        }

        Map<Laptop, Integer> laptopsToOrder = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : quantitiesByLaptop.entrySet()) {
            Laptop laptop = Laptop.findById(entry.getKey(), LockModeType.PESSIMISTIC_WRITE);
            if (laptop == null) {
                throw new NotFoundException("Không tìm thấy sản phẩm");
            }
            if (laptop.quantity < entry.getValue()) {
                throw new BadRequestException(
                        "Sản phẩm \"" + laptop.name + "\" chỉ còn " + Math.max(laptop.quantity, 0) + " sản phẩm");
            }
            laptopsToOrder.put(laptop, entry.getValue());
        }

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
        for (Map.Entry<Laptop, Integer> entry : laptopsToOrder.entrySet()) {
            Laptop laptop = entry.getKey();
            int quantity = entry.getValue();
            OrderDetail detail = new OrderDetail();
            detail.order = order;
            detail.laptop = laptop;
            detail.quantity = quantity;
            detail.unitPrice = laptop.price;
            detail.persist();

            total += laptop.price * quantity;
            laptop.quantity -= quantity;
        }
        order.totalAmount = total;
        return order;
    }
}