package com.shop.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.nio.file.Files;

@Path("/uploads")
public class ImageResource {

    @GET
    @Path("/{fileName}")
    public Response getImage(@PathParam("fileName") String fileName) {
        java.nio.file.Path path = java.nio.file.Path.of("uploads", fileName);
        if (!Files.exists(path)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String mimeType = "image/jpeg";
        if (fileName.toLowerCase().endsWith(".png")) mimeType = "image/png";
        else if (fileName.toLowerCase().endsWith(".gif")) mimeType = "image/gif";
        else if (fileName.toLowerCase().endsWith(".webp")) mimeType = "image/webp";
        return Response.ok(path.toFile()).type(mimeType).build();
    }
}