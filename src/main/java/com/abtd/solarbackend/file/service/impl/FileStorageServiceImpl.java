package com.abtd.solarbackend.file.service.impl;

import com.abtd.solarbackend.file.config.FileStorageProperties;
import com.abtd.solarbackend.file.dto.FileResponse;
import com.abtd.solarbackend.file.exception.FileNotFoundException;
import com.abtd.solarbackend.file.exception.FileStorageException;
import com.abtd.solarbackend.file.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties properties;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {

        this.fileStorageLocation =
                Paths.get(properties.getUploadDir())
                        .toAbsolutePath()
                        .normalize();

        try {

            Files.createDirectories(fileStorageLocation);

        } catch (IOException ex) {

            throw new FileStorageException(
                    "Could not create upload directory.",
                    ex);

        }
    }

    @Override
    public FileResponse uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new FileStorageException("File cannot be empty.");
        }

        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename());

        String extension = "";

        int index = originalFileName.lastIndexOf('.');

        if (index > 0) {
            extension = originalFileName.substring(index);
        }

        String fileName =
                UUID.randomUUID() + extension;

        try {

            Path targetLocation =
                    fileStorageLocation.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);

            return FileResponse.builder()
                    .fileName(fileName)
                    .originalFileName(originalFileName)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .downloadUrl("/api/files/download/" + fileName)
                    .build();

        } catch (IOException ex) {

            throw new FileStorageException(
                    "Could not store file.",
                    ex);

        }
    }

    @Override
    public Resource downloadFile(String fileName) {

        try {

            Path path =
                    fileStorageLocation.resolve(fileName).normalize();

            Resource resource =
                    new UrlResource(path.toUri());

            if (resource.exists()) {
                return resource;
            }

            throw new FileNotFoundException(
                    "File not found : " + fileName);

        } catch (MalformedURLException ex) {

            throw new FileNotFoundException(
                    "File not found : " + fileName);

        }
    }

    @Override
    public void deleteFile(String fileName) {

        try {

            Path path =
                    fileStorageLocation.resolve(fileName).normalize();

            Files.deleteIfExists(path);

        } catch (IOException ex) {

            throw new FileStorageException(
                    "Unable to delete file.",
                    ex);

        }
    }
}