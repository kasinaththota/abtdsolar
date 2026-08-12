package com.abtd.solarbackend.document.controller;

import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.document.enums.ModuleType;
import com.abtd.solarbackend.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadDocument(

            @RequestParam ModuleType moduleType,

            @RequestParam Long referenceId,

            @RequestParam DocumentType documentType,

            @RequestParam(required = false) String description,

            @RequestParam("file") MultipartFile file) {

        return ResponseBuilder.created(
                "Document uploaded successfully",

                documentService.uploadDocument(
                        moduleType,
                        referenceId,
                        documentType,
                        description,
                        file));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getDocuments(

            @RequestParam ModuleType moduleType,

            @RequestParam Long referenceId) {

        return ResponseBuilder.ok(
                "Documents fetched successfully",

                documentService.getDocuments(
                        moduleType,
                        referenceId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long id) {

        documentService.deleteDocument(id);

        return ResponseBuilder.ok(
                "Document deleted successfully",
                null);
    }
}