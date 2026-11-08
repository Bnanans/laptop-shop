package com.shop.resource;

import com.shop.entity.Admin;
import com.shop.entity.Customer;
import com.shop.service.AdminService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@ApplicationScoped
public class AuthResource {

    @Inject
    AdminService adminService;

    public static class Login {
        public String username;
        public String password;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login login) {
        Admin a = adminService.findByUsername(login.username);
        if (a != null && a.password.equals(login.password)) {
            return Response.ok(a).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public static class Register {
        public String fullName;
        public String email;
        public String phone;
        public String address;
        public String password;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response register(Register reg) {
        try {
            Customer existing = Customer.find("email", reg.email).firstResult();
            if (existing != null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Email đã tồn tại\"}").build();
            }

            Customer customer = new Customer();
            customer.fullName = reg.fullName;
            customer.email = reg.email;
            customer.phone = reg.phone;
            customer.address = reg.address;
            customer.password = reg.password;
            customer.persist();

            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
