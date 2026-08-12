package com.abtd.solarbackend.file.controller;

import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.file.dto.FileResponse;
import com.abtd.solarbackend.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file) {

        FileResponse response = fileStorageService.uploadFile(file);

        return ResponseBuilder.created(
                "File uploaded successfully",
                response
        );
    }

    @GetMapping("/download/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName) {

        Resource resource = fileStorageService.downloadFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFile(
            @PathVariable String fileName) {

        fileStorageService.deleteFile(fileName);

        return ResponseBuilder.ok(
                "File deleted successfully",
                null
        );
    }
}