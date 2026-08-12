package com.abtd.solarbackend.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {

    private String fileName;

    private String originalFileName;

    private String contentType;

    private long size;

    private String downloadUrl;
}