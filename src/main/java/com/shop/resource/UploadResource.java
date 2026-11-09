package com.shop.resource;

import java.io.InputStream;

import com.shop.service.UploadService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

@Path("/api/upload")
@ApplicationScoped
public class UploadResource {

    private static final Logger LOG = Logger.getLogger(UploadResource.class);

    @Inject
    UploadService uploadService;

    public static class FileForm {

        @RestForm("fileName")
        @PartType(MediaType.TEXT_PLAIN)
        public String fileName;

        @RestForm("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public InputStream data;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartForm FileForm form) {
        if (form == null || form.data == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Vui lòng chọn ảnh cần upload")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        try {
            String imageUrl = uploadService.saveFile(form.fileName, form.data);
            return Response.ok(imageUrl)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } catch (Exception e) {
            // Details go to Render logs; do not expose credentials or internals to the client.
            LOG.error("Image upload failed", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Không thể upload ảnh. Hãy kiểm tra log của server và cấu hình Cloudinary.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
