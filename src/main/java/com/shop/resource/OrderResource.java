package com.shop.resource;

import java.util.List;

import com.shop.entity.Order;
import com.shop.entity.OrderDetail;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    public List<Order> getAll() {
        return Order.listAll();
    }

    @PUT
    @Path("/{id}/status")
    @Transactional
    public Order updateStatus(@PathParam("id") Long id, String status) {
        Order order = Order.findById(id);
        if (order != null) {
            if (status.startsWith("\""))
                status = status.substring(1, status.length() - 1);
            order.status = status;
        }
        return order;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteOrder(@PathParam("id") Long id) {
        Order order = Order.findById(id);
        if (order != null) {
            OrderDetail.delete("order", order);
            order.delete();
        }
    }
}