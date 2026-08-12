package com.abtd.solarbackend.document.service.impl;

import com.abtd.solarbackend.document.dto.response.DocumentResponse;
import com.abtd.solarbackend.document.entity.Document;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.document.enums.ModuleType;
import com.abtd.solarbackend.document.mapper.DocumentMapper;
import com.abtd.solarbackend.document.repository.DocumentRepository;
import com.abtd.solarbackend.document.service.DocumentService;
import com.abtd.solarbackend.file.dto.FileResponse;
import com.abtd.solarbackend.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final FileStorageService fileStorageService;

    @Override
    public DocumentResponse uploadDocument(
            ModuleType moduleType,
            Long referenceId,
            DocumentType documentType,
            String description,
            MultipartFile file) {

        FileResponse uploadedFile = fileStorageService.uploadFile(file);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String uploadedBy =
                authentication == null
                        ? "SYSTEM"
                        : authentication.getName();

        Document document = Document.builder()
                .moduleType(moduleType)
                .referenceId(referenceId)
                .documentType(documentType)
                .description(description)
                .originalFileName(uploadedFile.getOriginalFileName())
                .storedFileName(uploadedFile.getFileName())
                .contentType(uploadedFile.getContentType())
                .fileSize(uploadedFile.getSize())
                .filePath(uploadedFile.getDownloadUrl())
                .uploadedBy(uploadedBy)
                .verified(false)
                .build();

        Document savedDocument = documentRepository.save(document);

        return documentMapper.toResponse(savedDocument);
    }

    @Override
    public List<DocumentResponse> getDocuments(
            ModuleType moduleType,
            Long referenceId) {

        return documentRepository
                .findByModuleTypeAndReferenceId(
                        moduleType,
                        referenceId)
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found with id : " + id));

        fileStorageService.deleteFile(document.getStoredFileName());

        documentRepository.delete(document);
    }
}