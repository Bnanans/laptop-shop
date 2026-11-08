package com.shop.resource;

import java.util.List;

import com.shop.entity.Laptop;
import com.shop.service.LaptopService;

import jakarta.inject.Inject;
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

@Path("/api/laptops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LaptopResource {

    @Inject
    LaptopService service;

    @GET
    public List<Laptop> getAll() {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public Laptop getById(@PathParam("id") Long id) {

        Laptop laptop = service.getById(id);

        if (laptop == null) {
            throw new NotFoundException("Laptop not found");
        }

        return laptop;
    }

    @POST
    public String add(Laptop laptop) {
        service.add(laptop);
        return "Laptop added";
    }

    @DELETE
    @Path("/{id}")
    public String delete(@PathParam("id") Long id) {
        service.delete(id);
        return "Laptop deleted";
    }

    @PUT
    @Path("/{id}")
    public String update(@PathParam("id") Long id, Laptop laptop) {

        service.update(id, laptop);

        return "Laptop updated";
    }

    @GET
    @Path("/search/{keyword}")
    public List<Laptop> search(@PathParam("keyword") String keyword) {

        return service.search(keyword);
    }
}