package com.abtd.solarbackend.document.dto.response;

import com.abtd.solarbackend.document.enums.ModuleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResponse {

    private Long id;

    private ModuleType moduleType;

    private Long referenceId;

    private String originalFileName;

    private String storedFileName;

    private String contentType;

    private Long fileSize;

    private String downloadUrl;
}