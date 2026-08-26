package com.emma.miniragent.rag.storage;

import com.emma.miniragent.framework.exception.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class LocalFileStorageService {
    private final Path uploadRoot;

    public LocalFileStorageService(String uploadDir) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public StoredFile store(long kbId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ClientException("Upload file cannot be empty");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            originalName = "upload.txt";
        }

        String lower = originalName.toLowerCase();
        if (!lower.endsWith(".txt") && !lower.endsWith(".md")) {
            throw new ClientException("Only .md and .txt file support");
        }

        try {
            Path kbDir = uploadRoot.resolve(String.valueOf(kbId));
            Files.createDirectories(kbDir);

            String storedName = UUID.randomUUID() + "_" + sanitizeFilename(originalName);
            Path target = kbDir.resolve(storedName);
            file.transferTo(target);

            String contentType = lower.endsWith(".md") ? "text/markdown" : "text/plain";
            return new StoredFile(target.toString(), originalName, contentType);
        } catch (IOException ex) {
            throw new ClientException("File store failed: " + ex.getMessage());
        }
    }


    public String readUtf8(String filePath) {
        try {
            return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new ClientException("Failed to read file: " + ex.getMessage());
        }
    }

    private static String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public record StoredFile(String filePath, String filename, String contentType) {
    }
}
