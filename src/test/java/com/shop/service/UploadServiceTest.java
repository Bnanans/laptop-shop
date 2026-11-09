package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

class UploadServiceTest {

    @Test
    void missingCloudinaryConfigurationIsReportedWhenUploading() {
        UploadService service = new UploadService("", "", "");

        UploadService.CloudinaryConfigurationException exception = assertThrows(
                UploadService.CloudinaryConfigurationException.class,
                () -> service.saveFile(
                        "laptop.png",
                        new ByteArrayInputStream(new byte[0])
                )
        );

        assertTrue(exception.getMessage().contains("CLOUDINARY_CLOUD_NAME"));
    }
}
