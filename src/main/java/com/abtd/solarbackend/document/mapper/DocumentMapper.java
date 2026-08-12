package com.abtd.solarbackend.document.mapper;

import com.abtd.solarbackend.document.dto.response.DocumentResponse;
import com.abtd.solarbackend.document.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document document) {

        return DocumentResponse.builder()
                .id(document.getId())
                .moduleType(document.getModuleType())
                .referenceId(document.getReferenceId())
                .originalFileName(document.getOriginalFileName())
                .storedFileName(document.getStoredFileName())
                .contentType(document.getContentType())
                .fileSize(document.getFileSize())
                .downloadUrl("/api/files/download/" + document.getStoredFileName())
                .build();
    }

}