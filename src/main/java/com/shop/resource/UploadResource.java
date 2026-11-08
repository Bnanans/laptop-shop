package com.shop.resource;

import com.shop.service.UploadService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.MultipartForm;
import jakarta.ws.rs.FormParam;

import java.io.InputStream;

@Path("/api/upload")
@ApplicationScoped
public class UploadResource {

    @Inject
    UploadService uploadService;

    public static class FileForm {
        @FormParam("fileName")
        public String fileName;

        @FormParam("file")
        public InputStream data;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartForm FileForm form) {
        try {
            String url = uploadService.saveFile(form.fileName, form.data);
            return Response.ok(url).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
