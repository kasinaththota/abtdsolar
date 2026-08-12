package com.abtd.solarbackend.document.service;

import com.abtd.solarbackend.document.dto.response.DocumentResponse;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.document.enums.ModuleType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse uploadDocument(
            ModuleType moduleType,
            Long referenceId,
            DocumentType documentType,
            String description,
            MultipartFile file);

    List<DocumentResponse> getDocuments(
            ModuleType moduleType,
            Long referenceId);

    void deleteDocument(Long id);

}