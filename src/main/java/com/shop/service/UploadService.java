package com.shop.service;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UploadService {

    private final Cloudinary cloudinary;

    @Inject
    public UploadService(
            @ConfigProperty(name = "cloudinary.cloud-name")
            String cloudName,

            @ConfigProperty(name = "cloudinary.api-key")
            String apiKey,

            @ConfigProperty(name = "cloudinary.api-secret")
            String apiSecret
    ) {
        this.cloudinary = new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret,
                        "secure", true
                )
        );
    }

    public String saveFile(String fileName, InputStream data)
            throws Exception {

        if (data == null) {
            throw new IllegalArgumentException("File không được để trống");
        }

        // Tạo tên duy nhất để tránh trùng ảnh
        String publicId = UUID.randomUUID().toString();

        // Upload ảnh lên Cloudinary
        Map<?, ?> result = cloudinary.uploader().upload(
                data,
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "laptop-shop/products",
                        "public_id", publicId,
                        "overwrite", false
                )
        );

        // Lấy URL HTTPS của ảnh
        String imageUrl = (String) result.get("secure_url");

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalStateException(
                    "Không lấy được URL ảnh từ Cloudinary"
            );
        }

        return imageUrl;
    }
}
