package com.abtd.solarbackend.document.dto.request;

import com.abtd.solarbackend.document.enums.ModuleType;
import lombok.Data;

@Data
public class UploadDocumentRequest {

    private ModuleType moduleType;

    private Long referenceId;
}