package com.shop.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@ApplicationScoped
public class UploadService {

    private final Path uploadDir = Path.of("uploads");

    public UploadService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            // ignore
        }
    }

    public String saveFile(String fileName, java.io.InputStream data) throws IOException {
        Path target = uploadDir.resolve(fileName);
        Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName;
    }
}
