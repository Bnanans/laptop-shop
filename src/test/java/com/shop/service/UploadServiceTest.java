package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

class UploadServiceTest {

    @Test
    void emptyFileIsRejectedBeforeUploading() {
        UploadService service = new UploadService("", "", "");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.saveFile(
                        "laptop.png",
                        new ByteArrayInputStream(new byte[0])
                )
        );
    }
}
