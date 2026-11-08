
package com.shop.resource;

import com.shop.service.UploadService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.MultipartForm;

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
            if (form == null || form.data == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Vui lòng chọn ảnh cần upload")
                        .build();
            }

            // Gửi ảnh lên Cloudinary
            String imageUrl = uploadService.saveFile(
                    form.fileName,
                    form.data
            );

            // Trả URL ảnh về frontend
            return Response.ok(imageUrl)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

        } catch (Exception e) {

            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Không thể upload ảnh lên Cloudinary")
                    .build();
        }
    }
}
